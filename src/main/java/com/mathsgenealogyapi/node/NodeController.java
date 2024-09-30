package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    @Autowired NodeService service;

    private static final Logger logger = LogManager.getLogger(NodeController.class);

    @GetMapping("")
    public String getTestMessage() {
        return "Hello World!";
    }

    @GetMapping("/test/{id}")
    public String testGetMapping(@PathVariable Integer id) {
        return id.toString();
    }

    @GetMapping("/node/{id}")
    public ResponseEntity<NodeDto> getEntryById(@PathVariable Integer id) {
        logger.info("Received request for single node " + id);
        try {
            return ResponseEntity.ok()
                    .body(service.getSingleNode(id));
        }
        catch (IOException e) {
            logger.error("IOException from JSoup scraping at id " + id + " with message: \n" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        catch (NodeDoesNotExistException e) {
            logger.info("Requested node " + id + " does not exist in the maths genealogy project so request failed with 404");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A node on the genealogy family tree with id " + id + " does not exist.");
        }
    }



}
