package de.nodeline.box.application.acl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.nodeline.box.application.primaryadapter.api.dto.BearerTokenAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.CredentialsDto;
import de.nodeline.box.domain.model.BearerTokenCredentials;
import de.nodeline.box.domain.model.Credentials;

@Service
public class CredentialsService {

    public CredentialsDto toDto(Credentials credentials) {
        CredentialsDto dto = new CredentialsDto();
        dto.setId(credentials.getId());
        dto.setName(credentials.getName());
        dto.setDescription(credentials.getDescription());
        switch (credentials) {
            case BearerTokenCredentials cred:
                BearerTokenAttributesDto bearerTokenAttributesDto = new BearerTokenAttributesDto();
                bearerTokenAttributesDto.setToken(cred.getToken());
                dto.setAttributes(bearerTokenAttributesDto);
                break;
        
            default:
                break;
        }
        return dto;
    }

    public Credentials toEntity(CredentialsDto dto) {
        Credentials entity = null;
        switch (dto.getAttributes()) {
            case BearerTokenAttributesDto bearerTokenAttributesDto:
                entity = new BearerTokenCredentials();
                ((BearerTokenCredentials) entity).setToken(bearerTokenAttributesDto.getToken());
                break;
        
            default:
                throw new IllegalArgumentException("Unsupported credentials type: " + dto.getAttributes().getClass());
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
