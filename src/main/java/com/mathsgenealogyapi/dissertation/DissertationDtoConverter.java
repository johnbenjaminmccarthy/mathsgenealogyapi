package com.mathsgenealogyapi.dissertation;

import com.mathsgenealogyapi.advisor.AdvisorDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class DissertationDtoConverter implements Converter<Dissertation, DissertationDto> {

    ConversionService conversionService;

    public DissertationDtoConverter(ConversionService conversionService) { this.conversionService = conversionService; }

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
                from.getAdvisors().stream().map(it -> conversionService.convert(it, AdvisorDto.class)).collect(Collectors.toList())
        );
    }
}
