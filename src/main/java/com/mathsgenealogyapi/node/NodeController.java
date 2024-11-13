package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    final NodeService service;

    private static final Logger logger = LogManager.getLogger(NodeController.class);

    public NodeController(NodeService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<NodeDto> getEntryById(
            @RequestParam Integer id,
            @RequestParam(defaultValue = "false") Boolean forceupdate
            ) {
        logger.info("Received request for single node " + id + (forceupdate ? " with forced rescraping." : "."));
        try {
            return ResponseEntity.ok()
                    .body(service.getSingleNode(id, forceupdate));
        }
        catch (IOException e) {
            logger.error("IOException from JSoup scraping at id " + id + " with message: \n" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during the scraping of data from the maths genealogy project and has been reported. Please try again later.");
        }
        catch (NodeDoesNotExistException e) {
            logger.info("Requested node " + id + " does not exist in the maths genealogy project so request failed with 404");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A node on the genealogy family tree with id " + id + " does not exist.");
        }
        catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



}
