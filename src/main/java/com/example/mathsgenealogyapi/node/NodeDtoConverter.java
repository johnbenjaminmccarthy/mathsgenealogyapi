package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.dissertation.DissertationDto;
import com.example.mathsgenealogyapi.dissertation.DissertationDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
        return NodeDto.builder()
                .genealogyId(from.getGenealogyId())
                .name(from.getName())
                .dissertations(from.getDissertations().stream().map(it -> conversionService.convert(it, DissertationDto.class)).collect(Collectors.toList()))
                .numberofdescendents(from.getNumberofdescendents())
                .students(from.getStudents().toString())
                .build();
    }
}
