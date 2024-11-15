package com.mathsgenealogyapi.advisor;


import org.springframework.core.convert.converter.Converter;

public class AdvisorDtoConverter implements Converter<Advisor, AdvisorDto> {

    @Override
    public AdvisorDto convert(Advisor from) {
        return new AdvisorDto(
                from.getAdvisorId(),
                from.getName(),
                from.getAdvisorNumber()
        );
    }
}
