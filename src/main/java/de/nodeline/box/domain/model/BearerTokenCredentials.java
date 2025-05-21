package de.nodeline.box.domain.model;

import de.nodeline.box.domain.model.record.BearerToken;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Entity
@EqualsAndHashCode(of={"id","token"})
public class BearerTokenCredentials extends Credentials {
    private BearerToken token;

    public BearerTokenCredentials() {
        super();
    }

    public BearerToken getToken() {
        return token;
    }

    public void setToken(BearerToken token) {
        this.token = token;
    }

}
