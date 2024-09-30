package com.mathsgenealogyapi.dissertation;

import org.springframework.core.convert.converter.Converter;

public class DissertationDtoConverter implements Converter<Dissertation, DissertationDto> {
    @Override
    public DissertationDto convert(Dissertation from) {
        return new DissertationDto(
                from.getNode().getId(),
                from.getNode().getName(),
                from.getPhdprefix(),
                from.getUniversity(),
                from.getYearofcompletion(),
                from.getDissertationtitle(),
                from.getMscnumber(),
                from.getAdvisor1edge().getFromNode().getId(),
                from.getAdvisor1edge().getFromNode().getName(),
                from.getAdvisor2edge().getFromNode().getId(),
                from.getAdvisor2edge().getFromNode().getName()
        );
    }
}
