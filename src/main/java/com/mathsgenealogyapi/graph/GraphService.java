package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import com.mathsgenealogyapi.node.NodeRepository;
import com.mathsgenealogyapi.node.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GraphService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    ConversionService conversionService;

    private Graph nodeListToGraph(List<Node> nodes, Integer baseId) {
        Set<Edge> edges = new HashSet<>();
        for (Node node: nodes) {
            edges.addAll(node.getAdvisorEdges());
            edges.addAll(node.getStudentEdges());
        }
        return new Graph(baseId, nodes, edges.stream().toList());
    }

    public GraphDto getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown) {
        List<Object[]> scrapedNodes = nodeRepository.getNodes(id, maxGenerationsUp, maxGenerationsDown);
        List<Node> scrapedNodes2 = scrapedNodes.stream().map(it -> (Node)it[0]).collect(Collectors.toList());

        return conversionService.convert(nodeListToGraph(scrapedNodes2, id), GraphDto.class);
    }
}
