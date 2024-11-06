INSERT INTO nodes (genealogy_id, lastupdated, name, numberofdescendents, scraped) VALUES
        (293462, current_timestamp, 'John Benjamin McCarthy', 0, TRUE),
        (292875, current_timestamp, 'Michael Hallam', 0, TRUE),
        (217413, current_timestamp, 'Ruadhaí Dervan', 3, TRUE),
        (36909, current_timestamp, 'Simon Kirwan Donaldson', 250, TRUE),
        (93925, current_timestamp, 'Julius Ross', 6, TRUE);

INSERT INTO edges (from_node_id, to_node_id) VALUES
         (217413,293462),
         (36909,293462),
         (217413,292875),
         (93925, 217413),
         (36909, 93925);

INSERT INTO dissertations (id, dissertationtitle, mscnumber, phdprefix, university, yearofcompletion, node_id) VALUES
        (1, 'Stability conditions and canonical metrics','53','Ph.D.','Imperial College London','2023',293462);

INSERT INTO advisors (dissertation_id, advisor_edge_from_node_id, advisor_edge_to_node_id, name, advisor_number) VALUES
        (1, 36909, 293462, 'Simon Kirwan Donaldson', 1),
        (1, 217413, 293462, 'Ruadhaí Dervan', 2);





