package de.nodeline.box.application.secondaryadapter.nifi.model;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="processor")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of={"id", "version", "modelId"})
public class Processor {
    @Id
    private String id; 
    String version;
    @ManyToOne
    @JoinColumn(name = "process_group_id", referencedColumnName = "id")
    ProcessGroup processGroup;
    //Back reference to the backend entity
    UUID modelId;
    Type type;

    
    public enum HttpRequestRelationship implements RelationshipInterface {
        FAILURE("Failure"),
        RETRY("Retry"),
        NO_RETRY("No Retry"),
        ORIGINAL("Original"),
        RESPONSE("Response");
        
        private final String value;

        HttpRequestRelationship(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    public enum JoltTransformationRelationship implements RelationshipInterface {
        FAILURE("failure"),
        SUCCESS("success");

        private final String value;

        JoltTransformationRelationship(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    public enum Type {
        HTTP_REQUEST("org.apache.nifi.processors.standard.InvokeHTTP"),
        JOLT_TRANSFORMATION("org.apache.nifi.processors.jolt.JoltTransformJSON");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

    }
}
