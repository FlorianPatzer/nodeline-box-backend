package de.nodeline.box.domain.model;

import java.util.UUID;

import de.nodeline.box.domain.model.record.MailAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NodelineUser {
    @Id
    private UUID id;
    private String name;    
    private MailAddress mailAddress;

    public NodelineUser() {
        this.id = UUID.randomUUID();
    }
}
