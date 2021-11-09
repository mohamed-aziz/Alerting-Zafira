import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.joda.time.DateTime;
import org.elasticsearch.index.query.QueryBuilders;
import entity.auditbeat.AuditBeatDocument;

class InterestingCommands implements utils.IScriptAuditbeat {
    AbstractQueryBuilder search() {
        QueryBuilders.boolQuery()
            .must(QueryBuilders.rangeQuery("@timestamp").from(new DateTime().minusSeconds(60)).to(new DateTime()))
            .must(QueryBuilders.matchQuery("event.dataset", "process"))
            .must(QueryBuilders.matchQuery("event.action", "process_started"))
            .must(
                QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("process.executable", "*top*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*bin/ls*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*bin/whoami*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*bin/vi*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*bin/emacs*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*sudo*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*socat*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*strace*"))
                .should(QueryBuilders.wildcardQuery("process.executable", "*ltrace*"))
                .should(QueryBuilders.matchQuery("process.name", "nmap"))
                .should(QueryBuilders.matchQuery("process.name", "tcpdump"))
                .should(QueryBuilders.matchQuery("process.name", "base16"))
                .should(QueryBuilders.matchQuery("process.name", "base32"))
                .should(QueryBuilders.matchQuery("process.name", "base32plain"))
                .should(QueryBuilders.matchQuery("process.name", "base32hex"))
                .should(QueryBuilders.matchQuery("process.name", "base64"))
                .should(QueryBuilders.matchQuery("process.name", "base64plain"))
                .should(QueryBuilders.matchQuery("process.name", "base64mtime"))
                .should(QueryBuilders.matchQuery("process.name", "base64pem"))
            )
    }

    int getPeriod() {
        1
    }

    String getAuthor() {
        "Mohamed Aziz Knani <www.aziz.tn>"
    }

    String getDescription() {
        "Some interesting commands are run on servers, we want to know these even if false positives."
    }
    HashMap<String, Object> getContext() {
        [
            user: "user.name",
            host: "host.name",
            dir: "process.working_directory",
            command: "process.executable",
            args: "process.args"
        ]    
    }

    String getTemplate() {
        '''🎈 Command run:
{{user}}@{{host}}:{{dir}}${{command}}\n
Process args 🤟: {% for arg in args %}{{arg}}{% endfor %}'''
    }
}
new InterestingCommands()