package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.node.NodeRepository;
import com.mathsgenealogyapi.node.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GraphService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    ConversionService conversionService;

    public GraphDto getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown) {
        Graph scrapedGraph = nodeRepository.getGraph(id, maxGenerationsUp, maxGenerationsDown);
        return conversionService.convert(scrapedGraph, GraphDto.class);
    }
}
