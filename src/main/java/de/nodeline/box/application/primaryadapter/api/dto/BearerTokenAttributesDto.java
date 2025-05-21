package de.nodeline.box.application.primaryadapter.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.nodeline.box.domain.model.record.BearerToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BearerTokenAttributesDto extends CredentialsAttributesDto {
    @JsonProperty("token")
    private BearerToken token;
}
