package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcurerDto {
    private Type type;
    private UUID id;

    public enum Type {
        GET_REQUEST
    }
}
