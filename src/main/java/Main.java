import com.google.gson.Gson;
import dao.QueryDAO;

import entity.auditbeat.AuditBeatDocument;
import entity.filebeat.FileBeatDocument;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import props.ConfigProps;
import props.Index;
import updatehandlers.UpdateSecurityHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

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

        Index auditbeat_index = new Index(
                // "filebeat-*",
                "auditbeat-*"
        );

        ConfigProps auditbeat_props = new ConfigProps();
        auditbeat_props.setIndex(auditbeat_index);


        Index filebeat_index = new Index(
                // "filebeat-*",
                "filebeat-*"
        );

        ConfigProps filebeat_props = new ConfigProps();
        filebeat_props.setIndex(filebeat_index);


        QueryDAO auditbeat_dao =  new QueryDAO(
                myclient,
                sourceBuilder,
                auditbeat_props,
                new Gson());
        Query auditbeat_query = new Query(auditbeat_dao);


        QueryDAO filebeat_dao =  new QueryDAO(
                myclient,
                sourceBuilder,
                filebeat_props,
                new Gson());

        Query filebeat_query = new Query(filebeat_dao);


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




        System.out.println("end");
    }
}
