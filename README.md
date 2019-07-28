# money-transfer
SparkJava-Jooq-H2 standalone project for implementing RESTful API (including data model and the backing implementation) for money transfers between accounts and other operations. 

# Technology Stack
Java 8
[Maven](https://maven.apache.org/)
[Spark Framework](http://sparkjava.com) (with embedded Jetty)
[Jooq](https://www.jooq.org)
[H2-In memory database](https://www.h2database.com)
[Lombok](https://projectlombok.org)
[google/gson](https://github.com/google/gson)
[JUnit 4](https://junit.org/junit4/)
[Apache HttpClient](https://hc.apache.org/index.html) (for unit testing)

# Http Status
200 OK
400 Bad request
404 Not found
500 Internal Server Error

# Available Services
Request:
PUT- http://localhost:8080/credit/432178538321
{
	"amount": "200"
}
Response:
{
    "status": "SUCCESS",
    "statusCode": "200",
    "message": "Acct xxxxxxxx8321 credited with INR 200.0 on Mon Jul 29 01:16:34 IST 2019"
}
Request:
PUT- http://localhost:8080/debit/432178538321/debit
{
	"amount": "200"
}
Response:
{
    "status": "SUCCESS",
    "statusCode": "200",
    "message": "Acct xxxxxxxx8321 debited with INR 200.0 on Mon Jul 29 01:17:42 IST 2019"
}
Request:
PUT- http://localhost:8080/transfer/432178538321
{
	"accNumber": "541387692467",
	"amount": "500"
}
Response:
{
    "status": "SUCCESS",
    "statusCode": "200",
    "message": "Acct xxxxxxxx8321 debited with INR 500.0 on Mon Jul 29 01:19:03 IST 2019 & Acct xxxxxxxx2467 	credited with INR 500.0"
}
Request:
GET- http://localhost:8080/balance/432178538321
Response:
{
    "status": "SUCCESS",
    "statusCode": "200",
    "account": [
        {
            "balance": 1400
        }
    ]
}
Request:
GET- http://localhost:8080/accountDetails/432178538321
Response:
{
    "status": "SUCCESS",
    "statusCode": "200",
    "account": [
        {
            "status": "A",
            "balance": 500
        }
    ]
}




