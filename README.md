# Maths Genealogy API

This is an API which accesses the [Mathematics Genealogy Project](https://www.mathgenealogy.org) and returns a directed graph detailing the mathematical genealogy of a given person. 

------------------

Technologies used: 

Spring, Spring Boot, JUnit5, Hibernate, Postgres, Docker, Docker Compose, Gradle, Log4j2, Jsoup for web scraping, Testcontainers for containerized integration testing with PostgreSQL for native recursive queries

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

------------------

For example, the query

```http://localhost:8080/api/graph?id=293462&maxGenerationsUp=2&maxGenerationsDown=2```

returns a partial directed graph of the Maths Genealogy family tree centred at Node 293462 (John Benjamin McCarthy): (Student data has been removed for conciseness, but a full list of students and their IDs is returned for each node up or down the tree)

![image](https://github.com/user-attachments/assets/2e1d56ce-e4f2-428b-bfb7-9bf8fcf7f7ba)

(A graphical representation of the tree)


```
{
    "base": 293462,
    "generationsUp": 2,
    "generationsDown": 0,
    "numberOfNodes": 6,
    "numberOfEdges": 10,
    "nodes": [
        {
            "id": 30949,
            "name": "Michael Francis Atiyah",
            "dissertations": [
                {
                    "nodeId": 30949,
                    "nodeName": "Michael Francis Atiyah",
                    "phdprefix": "Ph.D.",
                    "university": "University of Cambridge",
                    "yearofcompletion": "1955",
                    "dissertationtitle": "Some Applications of Topological Methods in Algebraic Geometry",
                    "mscnumber": null,
                    "advisors": [
                        {
                            "advisorId": 18583,
                            "advisorName": "William Vallance Douglas Hodge",
                            "advisorNumber": 1
                        }
                    ]
                }
            ],
            "students": [
                ...
            ],
            "numberofdescendents": 1066
        },
        {
            "id": 93925,
            "name": "Julius Ross",
            "dissertations": [
                {
                    "nodeId": 93925,
                    "nodeName": "Julius Ross",
                    "phdprefix": "Ph.D.",
                    "university": "University of London",
                    "yearofcompletion": "2004",
                    "dissertationtitle": "Instability of Polarised Algebraic Varieties",
                    "mscnumber": null,
                    "advisors": [
                        {
                            "advisorId": 36909,
                            "advisorName": "Simon Kirwan Donaldson",
                            "advisorNumber": 1
                        },
                        {
                            "advisorId": 93812,
                            "advisorName": "Richard Paul Thomas",
                            "advisorNumber": 2
                        }
                    ]
                }
            ],
            "students": [
                ...
            ],
            "numberofdescendents": 6
        },
        {
            "id": 43911,
            "name": "Nigel James Hitchin",
            "dissertations": [
                {
                    "nodeId": 43911,
                    "nodeName": "Nigel James Hitchin",
                    "phdprefix": "D.Phil.",
                    "university": "University of Oxford",
                    "yearofcompletion": "1972",
                    "dissertationtitle": "Differentiable Manifolds: The Space of Harmonic Spinors",
                    "mscnumber": "53",
                    "advisors": [
                        {
                            "advisorId": 22477,
                            "advisorName": "Brian F. Steer",
                            "advisorNumber": 1
                        },
                        {
                            "advisorId": 30949,
                            "advisorName": "Michael Francis Atiyah",
                            "advisorNumber": 2
                        }
                    ]
                }
            ],
            "students": [
                ...
            ],
            "numberofdescendents": 455
        },
        {
            "id": 36909,
            "name": "Simon Kirwan Donaldson",
            "dissertations": [
                {
                    "nodeId": 36909,
                    "nodeName": "Simon Kirwan Donaldson",
                    "phdprefix": "D.Phil.",
                    "university": "University of Oxford",
                    "yearofcompletion": "1983",
                    "dissertationtitle": "The Yang-Mills Equations on Kähler Manifolds",
                    "mscnumber": null,
                    "advisors": [
                        {
                            "advisorId": 30949,
                            "advisorName": "Michael Francis Atiyah",
                            "advisorNumber": 1
                        },
                        {
                            "advisorId": 43911,
                            "advisorName": "Nigel James Hitchin",
                            "advisorNumber": 2
                        }
                    ]
                }
            ],
            "students": [
                ..
            ],
            "numberofdescendents": 250
        },
        {
            "id": 293462,
            "name": "John Benjamin McCarthy",
            "dissertations": [
                {
                    "nodeId": 293462,
                    "nodeName": "John Benjamin McCarthy",
                    "phdprefix": "Ph.D.",
                    "university": "Imperial College London",
                    "yearofcompletion": "2023",
                    "dissertationtitle": "Stability conditions and canonical metrics",
                    "mscnumber": "53",
                    "advisors": [
                        {
                            "advisorId": 36909,
                            "advisorName": "Simon Kirwan Donaldson",
                            "advisorNumber": 1
                        },
                        {
                            "advisorId": 217413,
                            "advisorName": "Ruadhaí Dervan",
                            "advisorNumber": 2
                        }
                    ]
                }
            ],
            "students": [],
            "numberofdescendents": 0
        },
        {
            "id": 217413,
            "name": "Ruadhaí Dervan",
            "dissertations": [
                {
                    "nodeId": 217413,
                    "nodeName": "Ruadhaí Dervan",
                    "phdprefix": "Ph.D.",
                    "university": "University of Cambridge",
                    "yearofcompletion": "2016",
                    "dissertationtitle": "Canonical Kähler metrics and K-stability",
                    "mscnumber": "14",
                    "advisors": [
                        {
                            "advisorId": 93925,
                            "advisorName": "Julius Ross",
                            "advisorNumber": 1
                        }
                    ]
                }
            ],
            "students": [
                ...
            ],
            "numberofdescendents": 3
        }
    ],
    "edges": [
        {
            "fromNodeId": 93812,
            "toNodeId": 93925
        },
        {
            "fromNodeId": 217413,
            "toNodeId": 293462
        },
        {
            "fromNodeId": 18583,
            "toNodeId": 30949
        },
        {
            "fromNodeId": 36909,
            "toNodeId": 93925
        },
        {
            "fromNodeId": 30949,
            "toNodeId": 36909
        },
        {
            "fromNodeId": 93925,
            "toNodeId": 217413
        },
        {
            "fromNodeId": 22477,
            "toNodeId": 43911
        },
        {
            "fromNodeId": 30949,
            "toNodeId": 43911
        },
        {
            "fromNodeId": 43911,
            "toNodeId": 36909
        },
        {
            "fromNodeId": 36909,
            "toNodeId": 293462
        }
    ]
}
```


