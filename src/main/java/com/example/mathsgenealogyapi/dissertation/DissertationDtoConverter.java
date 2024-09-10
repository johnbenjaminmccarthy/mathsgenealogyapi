package com.example.mathsgenealogyapi.dissertation;

import com.example.mathsgenealogyapi.node.Node;
import com.example.mathsgenealogyapi.node.NodeDto;
import org.springframework.core.convert.converter.Converter;

public class DissertationDtoConverter implements Converter<Dissertation, DissertationDto> {
    @Override
    public DissertationDto convert(Dissertation from) {
        return DissertationDto.builder()
                .phdprefix(from.getPhdprefix())
                .university(from.getUniversity())
                .yearofcompletion(from.getYearofcompletion())
                .dissertationtitle(from.getDissertationtitle())
                .mscnumber(from.getMscnumber())
                .advisor1name(from.getAdvisor1name())
                .advisor1id(from.getAdvisor1id())
                .advisor2name(from.getAdvisor2name())
                .advisor2id(from.getAdvisor2id())
                .build();
    }
}
