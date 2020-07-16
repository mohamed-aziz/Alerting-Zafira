import com.google.gson.Gson;
import dao.QueryDAO;

import groovy.lang.Binding;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import props.ConfigProps;
import props.Index;
import updatehandlers.UpdateSecurityHandler;
import utils.GroovyIntegration;
import utils.IScriptAuditbeat;
import utils.IScriptFilebeat;
import utils.Tasks;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // mock example

        RestHighLevelClient myclient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("***REMOVED***",
                        9200,
                        "http")));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        Index auditbeat_index = new Index("auditbeat-*");
        ConfigProps auditbeat_props = new ConfigProps();
        auditbeat_props.setIndex(auditbeat_index);

        Index filebeat_index = new Index("filebeat-*");
        ConfigProps filebeat_props = new ConfigProps();
        filebeat_props.setIndex(filebeat_index);

        QueryDAO auditbeat_dao =  new QueryDAO(myclient, sourceBuilder, auditbeat_props, new Gson());
        Query auditbeat_query = new Query(auditbeat_dao);

        QueryDAO filebeat_dao =  new QueryDAO(myclient, sourceBuilder, filebeat_props, new Gson());

        Query filebeat_query = new Query(filebeat_dao);

        System.out.println(AbstractQueryBuilder.class);
        // BOT stuff
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {

            UpdateSecurityHandler sec_handler =  new UpdateSecurityHandler();
            auditbeat_query.events.subscribe("search", sec_handler);
            filebeat_query.events.subscribe("search", sec_handler);
            telegramBotsApi.registerBot(sec_handler);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }

        Binding sharedData = new Binding();

        sharedData.setProperty("IScriptFilebeat", IScriptFilebeat.class);
        sharedData.setProperty("IScriptAuditbeat", IScriptAuditbeat.class);

        Path pth = Paths.get(System.getProperty("user.home"), "my_scripts");
        URL url = null;
        try {
            url = pth.toUri().toURL();
        } catch (MalformedURLException e) {
            log.error("Couldn't convert to url ", e);
        }

        GroovyIntegration gri = new GroovyIntegration(sharedData, url);

        List<String> scripts_names = null;
        try (Stream<Path> walk = Files.walk(pth)) {
            scripts_names = walk.filter(Files::isRegularFile).map(x -> x.getFileName().toString()).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            log.error("Couldn't walk files ", e);
        }
        ArrayList<Tasks> tasks = new ArrayList<>();
        for (String script: scripts_names) {
            try {
                Object out = gri.runScript(script);
                String interfaceName = out.getClass().getInterfaces()[0].getSimpleName();

                if (interfaceName.equals("IScriptFilebeat")) {
                    IScriptFilebeat myscript = (IScriptFilebeat) out;
                    tasks.add(new Tasks(() -> { filebeat_query.search(myscript.search(), myscript.getTemplate(), myscript.getContext());}, myscript.getPeriod(), TimeUnit.SECONDS));

                } else if (interfaceName.equals("IScriptAuditbeat")) {
                    IScriptAuditbeat myscript = (IScriptAuditbeat) out;
                    tasks.add(new Tasks(() -> { auditbeat_query.search(myscript.search(), myscript.getTemplate(), myscript.getContext());}, myscript.getPeriod(), TimeUnit.SECONDS));
                }
            } catch (ResourceException e) {
                log.error("Couldn't read resource ", e);
            } catch (ScriptException e) {
                log.error("Script error ", e);
            }
        }
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        for (Tasks task: tasks) {
            executor.scheduleAtFixedRate(task.getTask(), 0, task.getPeriod(), task.getUnit());
        }
    }
}
