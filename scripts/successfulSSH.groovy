import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.joda.time.DateTime;
import org.elasticsearch.index.query.QueryBuilders;
import entity.auditbeat.AuditBeatDocument;

class SuccessfulSSHLogin implements utils.IScriptAuditbeat {
    AbstractQueryBuilder search() {
        QueryBuilders.boolQuery()
            .must(QueryBuilders.rangeQuery("@timestamp").from(new DateTime().minusSeconds(60)).to(new DateTime()))
            .must(QueryBuilders.matchQuery("event.dataset", "login"))
            .must(QueryBuilders.matchQuery("event.outcome", "success"))
            .mustNot(
                QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("source.ip", "**REMOVED**"))
                .must(
                    QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("user.name", "qtsftp"))
                    .should(QueryBuilders.matchQuery("user.name", "qtftp"))))
    }

    int getPeriod() {
        1
    }

    String getAuthor() {
        "Mohamed Aziz Knani <www.aziz.tn>"
    }

    String getDescription() {
        "Get latest successful ssh logins in the last 60 seconds."
    }
    HashMap<String, Object> getContext() {
        [
            ip: "source.ip",
            user: "user.name",
            host: "host.name"
        ]    
    }

    String getTemplate() {
        '''🧨 Alert ssh login:
on host 🖥   {{host}} from {{ip}}\n
with user 👩 {{user}}'''
    }
}
new SuccessfulSSHLogin()