package com.mathsgenealogyapi.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/graph")
public class GraphController {

    final GraphService graphService;

    private static final Logger logger = LogManager.getLogger(GraphController.class);

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public ResponseEntity<GraphDto> getGraph(
            @RequestParam Integer id, //Base id of requested graph
            @RequestParam(defaultValue = "5") Integer maxGenerationsUp, //Maximum number of generations up the tree from the base. 0 = only from the base down
            @RequestParam(defaultValue = "5") Integer maxGenerationsDown //Maximum number of generations down the tree from the base. 0 = only from the base up
        ) {
        if (maxGenerationsUp < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum number of generations up the tree must be greater than or equal to 0.");
        }
        if (maxGenerationsDown < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum number of generations down the tree must be greater than or equal to 0.");
        }
        logger.info("Received request for graph with base node " + id + " going " + maxGenerationsUp + " generations up and " + maxGenerationsDown + " generations down.");
        try {
            return ResponseEntity.ok()
                    .body(graphService.getGraph(id, maxGenerationsUp, maxGenerationsDown));
        }
        /*catch (IOException e) {
            logger.error("IOException from JSoup scraping when completing graph request with base " + id + " with message: \n" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during the scraping of data from the maths genealogy project and has been reported. Please try again later.");
        }
        catch (NodeDoesNotExistException e) {
            logger.info("Requested base node " + id + " does not exist in the maths genealogy project so request failed with 404 with message: \n " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A node on the genealogy family tree with id " + id + " could not be found.");
        }*/
        catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
