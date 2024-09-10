package com.example.mathsgenealogyapi;

import com.example.mathsgenealogyapi.dissertation.DissertationDto;
import com.example.mathsgenealogyapi.dissertation.DissertationDtoConverter;
import com.example.mathsgenealogyapi.node.Node;
import com.example.mathsgenealogyapi.node.NodeDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private ConversionService conversionService;

    @Autowired
    public WebConfig(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void  addFormatters(FormatterRegistry registry) {
        registry.addConverter(nodeDtoConverter());
        registry.addConverter(dissertationDtoConverter());
    }

    @Bean
    public NodeDtoConverter nodeDtoConverter() {
        return new NodeDtoConverter(conversionService);
    }

    @Bean
    public DissertationDtoConverter dissertationDtoConverter() {
        return new DissertationDtoConverter();
    }
}
