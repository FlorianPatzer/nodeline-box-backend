package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Validated
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(of={"id", "pipeline"})
public abstract class Linkable {
    @Id
    protected UUID id;

    @OneToMany(mappedBy = "in", cascade = {CascadeType.ALL}, orphanRemoval = true)
    protected Set<Link> out;
    
    @OneToMany(mappedBy = "out", cascade = {CascadeType.ALL}, orphanRemoval = true)
    protected Set<Link> in;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;

    protected Linkable() {
        this.id = UUID.randomUUID();
        this.out = new HashSet<>();
        this.in = new HashSet<>();
    }

    public void addIn(Link in) {
        this.in.add(in);
        in.setOut(this);
    }
    public void addOut(Link out) {
        this.out.add(out);
        out.setIn(this);
    }
}
