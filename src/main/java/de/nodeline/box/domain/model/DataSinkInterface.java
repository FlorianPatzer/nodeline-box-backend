package de.nodeline.box.domain.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Use the class name as the type identifier
    include = JsonTypeInfo.As.PROPERTY, // Include the type info as a JSON property
    property = "type" // The property name in JSON to indicate the class type
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpPostRequest.class, name = "HttpPostRequest") // Map HttpPostRequest as a subtype
})
public interface DataSinkInterface {
    public void placeData();
}

