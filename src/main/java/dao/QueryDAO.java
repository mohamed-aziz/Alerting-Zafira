package dao;

import com.google.gson.Gson;
import entity.Document;
import entity.auditbeat.AuditBeatDocument;
import entity.filebeat.FileBeatDocument;
//import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import props.ConfigProps;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// @Slf4j
public class QueryDAO {

    private final RestHighLevelClient client;
    private final SearchSourceBuilder sourceBuilder;
    private final ConfigProps props;
    private final Gson gson;


    public QueryDAO(RestHighLevelClient client, SearchSourceBuilder sourceBuilder,
                    ConfigProps props, Gson gson){
        this.client = client;
        this.sourceBuilder = sourceBuilder;
        this.props = props;
        this.gson = gson;
    }

    /**
     *
     * @param doc
     * @return
     */
    public String indexRequest(final Document doc){


        return null;
    }

    /**
     *
     * @param fileBeatDocument
     * @return
     */
    public String updateDocument(FileBeatDocument fileBeatDocument){
        return null;
    }

    /**
     *
     * @return
     */
    public List<? extends Document> matchAllQuery() {

        List<? extends Document> result = new ArrayList<Document>();

        try {
            refreshRequest();
            result = getDocuments(QueryBuilders.matchAllQuery());
        } catch (Exception ex){
            // log.error("The exception was thrown in matchAllQuery method.", ex);
        }

        return result;
    }

    /**
     *
     * @param query
     * @return
     */
    public List<? extends Document> wildcardQuery(String query){

        List<? extends Document> result = new ArrayList<Document>();

        try {
            result = getDocuments(QueryBuilders.queryStringQuery("*" + query.toLowerCase() + "*"));
        } catch (Exception ex){
            System.out.println(ex);
            // log.error("The exception was thrown in wildcardQuery method.", ex);
        }

        return result;
    }


    public List<? extends Document> executeQuery(AbstractQueryBuilder query){

        List<? extends Document> result = new ArrayList<Document>();

        try {
            result = getDocuments(query);
        } catch (Exception ex){
            System.out.println(ex);
            // log.error("The exception was thrown in wildcardQuery method.", ex);
        }

        return result;
    }
    /**
     *
     * @param id
     * @throws IOException
     */
    public void deleteDocument(String id){
        try {
            final DeleteRequest deleteRequest = new DeleteRequest(props.getIndex().getName(), id);
            client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception ex){
            // log.error("The exception was thrown in deleteDocument method.", ex);
        }
    }

    /**
     *
     * @return
     */
    private SearchRequest getSearchRequest(){
        SearchRequest searchRequest = new SearchRequest(props.getIndex().getName());
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     *
     * @param builder
     * @return
     * @throws IOException
     */
    private List<? extends Document> getDocuments(AbstractQueryBuilder builder) throws IOException {
        List<Document> result = new ArrayList<>();

        sourceBuilder.query(builder);
        SearchRequest searchRequest = getSearchRequest();

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            if (this.props.getIndex().getName() == "auditbeat-*") {
                AuditBeatDocument doc = gson.fromJson(hit.getSourceAsString(), AuditBeatDocument.class);
                doc.setId(hit.getId());
                result.add(doc);

            } else if (this.props.getIndex().getName() == "filebeat-*") {
                FileBeatDocument doc = gson.fromJson(hit.getSourceAsString(), FileBeatDocument.class);
                doc.setId(hit.getId());
                result.add(doc);

            }
        }

        return result;
    }

    public void refreshRequest() throws IOException {
        final RefreshRequest refreshRequest = new RefreshRequest(props.getIndex().getName());
        client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
    }
}
