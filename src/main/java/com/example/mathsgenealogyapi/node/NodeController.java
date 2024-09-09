package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.Constants;
import com.example.mathsgenealogyapi.dissertation.Dissertation;
import com.example.mathsgenealogyapi.dissertation.DissertationRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Node getEntryById(@PathVariable Integer id) {
        return service.getSingleNode(id);
    }



}
