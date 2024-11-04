# Maths Genealogy API

This is an API which accesses the [Mathematics Genealogy Project](https://www.mathgenealogy.org) and returns a directed graph detailing the mathematical genealogy of a given person. 

------------------

Spring Boot, Hibernate, Postgres, Docker, Docker Compose, Gradle, Log4j2, Testcontroller, Jsoup

------------------

The API responds to a GET request for a family tree centred at some person by recursively scraping and caching in a database the information of ancestors and descendents to that person from the genealogy project. Future requests for trees which intersect with already retrieved nodes on the tree will only rescrape those nodes after 30 days. Individual nodes on the graph can be force-rescraped by a GET request to `/api/node` with `forceupdate=true`.

------------------
To run in a docker container:

```docker compose up java_db```

```docker compose up java_app```

------------------

API endpoints
-----

| URL | Parameters                                                                                                                         | Return                                                                                                                                                                                                                                                  | Description                                                                                                                                                                                                                                                                                                                                                                   |
|-----|------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `http://localhost:8080/api/graph` | `id`:`Integer`<br/>`maxGenerationsUp`:`Integer` (Optional. Default = `5`)<br/>`maxGenerationsDown`:`Integer` (Optional. Default = `5`) | Returns a list of `nodes` and `edges` representing the family tree centred at `id` as a directed graph.                                                                                                                                                 | Returns a graph centred at the person with base `id` according to their https://www.mathgenealogy.org url (for example Isaac Newton has id `74313` corresponding to entry https://www.mathgenealogy.org/id.php?id=74313). `maxGenerationsUp` and `maxGenerationsDown` set the maximum number of steps up or down the family tree which will be returned as part of the graph. |
| `http://localhost:8080/api/node`    | `id`:`Integer`<br/>`forceupdate`:`Boolean` (Optional. Default = `false`)                                                           | Returns a single node.                                                                                                                                                                                                                                  | Returns a single node from the cached scraped information if available, or if out of date or `forceupdate` is set to `true`, scrapes from the genealogy project before returning up-to-date data.                                                                                                                                                                             |




