package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelivererDto {
    private Type type;
    private UUID id;

    public enum Type {
        POST_REQUEST
    }
}
