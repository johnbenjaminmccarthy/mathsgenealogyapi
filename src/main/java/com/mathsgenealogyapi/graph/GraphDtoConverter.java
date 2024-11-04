package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.dissertation.DissertationDto;
import com.mathsgenealogyapi.edge.EdgeDto;
import com.mathsgenealogyapi.node.NodeDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GraphDtoConverter implements Converter<Graph, GraphDto> {

    ConversionService conversionService;

    public GraphDtoConverter(ConversionService conversionService) { this.conversionService = conversionService; }

    @Override
    public GraphDto convert(Graph from) {
        return new GraphDto(
                from.getBase(),
                from.getGenerationsUp(),
                from.getGenerationsDown(),
                from.getNumberOfNodes(),
                from.getNumberOfEdges(),
                from.getNodes().stream().map(it -> conversionService.convert(it, NodeDto.class)).collect(Collectors.toList()),
                from.getEdges().stream().map(it -> conversionService.convert(it, EdgeDto.class)).collect(Collectors.toList())
        );
    }
}
