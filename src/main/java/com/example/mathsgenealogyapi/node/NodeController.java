package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.Constants;
import com.example.mathsgenealogyapi.dissertation.Dissertation;
import com.example.mathsgenealogyapi.dissertation.DissertationRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    @Autowired NodeService service;

    @GetMapping("")
    public String getTestMessage() {
        return "Hello World!";
    }

    @GetMapping("/test/{id}")
    public String testGetMapping(@PathVariable Integer id) {
        return id.toString();
    }

    @GetMapping("/node/{id}")
    public NodeDto getEntryById(@PathVariable Integer id) {
        try {
            return service.getSingleNode(id);
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        catch (NodeDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A node on the genealogy family tree with id " + id + " does not exist.");
        }
    }



}
