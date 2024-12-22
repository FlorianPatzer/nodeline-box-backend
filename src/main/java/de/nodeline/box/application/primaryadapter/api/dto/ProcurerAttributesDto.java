package de.nodeline.box.application.primaryadapter.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpGetRequestAttributesDto.class, name = "HttpGetRequestAttributesDto")
})
public abstract class ProcurerAttributesDto {

}
