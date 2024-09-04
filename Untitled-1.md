Maths Genealogy scraper API:

Make GET request to API server with id of maths genalogy page.
Optional parameters: How many generations forward (children), How many generations back (ancestors). By default both are set to 10.

Retrieved data will be cached in a PostgreSQL database in a table with the following entries:

ID | Genealogy ID (index) | Name | PhD title (PhD, DPhil, etc.) | University | Year of Completion | Dissertation title | Subject classification number | Advisor 1 | Advisor 2 | Number of Descendents | Students (List of Genalogy ID) | Last updated

Using the cached database, or scraped data if the database entry does not exist, a JSON file representing a directed graph of the genealogy tree will be generated and returned to the API call. 

If the cached data has not been updated in one month, the data will be re-scraped an updated. 

A request can be submitted via API to force updating the cache of a particular genealogy entry (rate limited to 1 time per day). 