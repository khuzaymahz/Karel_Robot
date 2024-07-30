# Karel_Robot
As part of this project, I was responsible for creating optimized algorithms in Java to automate Karel to divide any possible grid into four equal areas. was built using Java

GET traces-apm-default/_eql/search
{
  "query": """
    sequence by trace.id 
    [ transaction where service.name == "ehsanEndUserWeb" and transaction.name == "POST Tyassarat/Donate" ] 
    [ span where span.name == "GET app.ehsan.sa" ]
    [ transaction where transaction.name == "GET CustomSettings/GetByKeyDecrypted {key}"]
    [ span where span.name == "GET app.ehsan.sa" ]
    [ transaction where  transaction.name == "GET TanfeethBillRequest/GetTanfeethBillByIdEndUser {id}"]
    [ span where span.name == "SELECT FROM TanfeethBillRequest" ]
    [ span where span.name == "SET" ]
    [ span where span.name == "POST dpw.alrajhibank.com.sa" ] 
    [ span where span.name == "SELECT FROM RajhiSadadPaymentLog" ] 
    [ span where span.name == "SET" ]
    [ transaction where transaction.name == "GET unknown route"]
    [ span where span.name == "POST app.ehsan.sa" ]
    [ transaction where transaction.name == "POST Donation/CreatePre"]
    [ span where span.name == "POST eps-app.ehsan.sa" ] 
    [ transaction where transaction.name == "POST PreDonationTransaction/Create"]
    [ span where span.name == "SET" ]
  """,
  "fields": [
    "transaction.name",
    "span.name",
    "transaction.duration.us",
    "span.duration.us",
    "event.outcome",
    "span.db.statement"
    
    ],
    "filter": {
      "range": {
        "@timestamp": {
          "gte" : "now-15m",
          "lte": "now"
        }
      }
    },
    "size" : "100"
}
