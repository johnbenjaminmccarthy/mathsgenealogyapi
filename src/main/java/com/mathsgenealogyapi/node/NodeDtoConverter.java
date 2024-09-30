package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.Student;
import com.mathsgenealogyapi.dissertation.DissertationDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NodeDtoConverter implements Converter<Node, NodeDto> {

    ConversionService conversionService;

    public NodeDtoConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public NodeDto convert(Node from) {
        return new NodeDto(
                from.getId(),
                from.getName(),
                from.getDissertations().stream().map(it -> conversionService.convert(it, DissertationDto.class)).collect(Collectors.toList()),
                from.getStudentEdges().stream().map(it -> new Student(it.getToNode().getId(), it.getToNode().getName())).collect(Collectors.toList()),
                from.getNumberofdescendents()
                );
    }
}
