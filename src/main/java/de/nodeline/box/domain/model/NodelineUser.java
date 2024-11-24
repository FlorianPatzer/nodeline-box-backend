package de.nodeline.box.domain.model;

import java.util.UUID;

import de.nodeline.box.domain.model.record.MailAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class NodelineUser {
    @Id
    private UUID id;
    private String name;    
    private MailAddress mailAddress;

    public NodelineUser() {
        this.id = UUID.randomUUID();
    }
}
