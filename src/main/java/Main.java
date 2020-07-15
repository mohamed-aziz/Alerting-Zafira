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

        Index myindex1 = new Index(
                // "filebeat-*",
                "auditbeat-*"
        );

        ConfigProps myprops1 = new ConfigProps();
        myprops1.setIndex(myindex1);


        Index myindex2 = new Index(
                // "filebeat-*",
                "filebeat-*"
        );

        ConfigProps myprops2 = new ConfigProps();
        myprops2.setIndex(myindex2);


        QueryDAO mydao1 =  new QueryDAO(
                myclient,
                sourceBuilder,
                myprops1,
                new Gson()
        );

        Query myquery1 = new Query(mydao1);

        QueryDAO mydao2 =  new QueryDAO(
                myclient,
                sourceBuilder,
                myprops2,
                new Gson()
        );

        Query myquery2 = new Query(mydao2);


        // BOT stuff


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {

            UpdateSecurityHandler sec_handler =  new UpdateSecurityHandler();
            myquery1.events.subscribe("search", sec_handler);
            myquery2.events.subscribe("search", sec_handler);
            telegramBotsApi.registerBot(sec_handler);

        }catch (TelegramApiException e) {
            e.printStackTrace();
        }




        System.out.println("end");
    }
}
