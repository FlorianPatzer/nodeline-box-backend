package de.nodeline.box.domain.model.record;

import jakarta.persistence.Embeddable;

@Embeddable
public record MailAddress(String mailAddress) {
    
}
