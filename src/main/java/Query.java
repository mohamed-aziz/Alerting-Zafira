import dao.QueryDAO;
import entity.Document;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import utils.EventManager;

import java.util.List;

public class Query {
    private QueryDAO _dao;
    public EventManager events;

    public Query (QueryDAO _dao) {
        this._dao = _dao;
        this.events = new EventManager("search");
    }

    List<? extends Document> search(AbstractQueryBuilder query) {
        List<? extends Document> result = _dao.executeQuery(query);
        this.events.notify("search", result);
        return result;
    }
}
