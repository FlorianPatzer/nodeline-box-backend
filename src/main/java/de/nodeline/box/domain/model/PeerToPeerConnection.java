package de.nodeline.box.domain.model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PeerToPeerConnection extends Link {

}
