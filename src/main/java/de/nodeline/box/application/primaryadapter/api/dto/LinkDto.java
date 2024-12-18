package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkDto {
    private Type type;
    private UUID id;

    public enum Type {
        PEER_TO_PEER
    }
}
