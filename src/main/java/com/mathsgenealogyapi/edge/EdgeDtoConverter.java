package com.mathsgenealogyapi.edge;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EdgeDtoConverter implements Converter<Edge, EdgeDto> {

    @Override
    public EdgeDto convert(Edge from) {
        return new EdgeDto(
                from.getFromNode().getId(),
                from.getToNode().getId()
        );
    }
}
