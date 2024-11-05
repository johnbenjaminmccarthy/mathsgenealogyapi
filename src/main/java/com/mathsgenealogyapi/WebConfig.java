package com.mathsgenealogyapi;

import com.mathsgenealogyapi.advisor.AdvisorDtoConverter;
import com.mathsgenealogyapi.dissertation.DissertationDtoConverter;
import com.mathsgenealogyapi.graph.GraphDtoConverter;
import com.mathsgenealogyapi.node.NodeDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
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
        registry.addConverter(graphDtoConverter());
        registry.addConverter(advisorDtoConverter());
    }

    @Bean
    public NodeDtoConverter nodeDtoConverter() {
        return new NodeDtoConverter(conversionService);
    }

    @Bean
    public DissertationDtoConverter dissertationDtoConverter() {
        return new DissertationDtoConverter(conversionService);
    }

    @Bean
    public AdvisorDtoConverter advisorDtoConverter() {
        return new AdvisorDtoConverter();
    }

    @Bean
    public GraphDtoConverter graphDtoConverter() {
        return new GraphDtoConverter(conversionService);
    }
}
