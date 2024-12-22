package de.nodeline.box.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import de.nodeline.box.application.primaryadapter.api.dto.HttpGetRequestAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.HttpPostRequestAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.JoltTransformationAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.TransformationAttributesDto;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(
            new NamedType(HttpPostRequestAttributesDto.class, "HttpPostRequestAttributesDto"),
            new NamedType(HttpGetRequestAttributesDto.class, "HttpGetRequestAttributesDto"),
            new NamedType(TransformationAttributesDto.class, "TransformationAttributesDto"),
            new NamedType(JoltTransformationAttributesDto.class, "JoltTransformationAttributesDto")
            );
        return objectMapper;
    }
}
