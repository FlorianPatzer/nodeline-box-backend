package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.domain.model.Link;
import de.nodeline.box.application.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<List<Link>> getAllLinks() {
        return ResponseEntity.ok(linkService.getAllLinks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> getLinkById(@PathVariable UUID id) {
        Optional<Link> link = linkService.getLinkById(id);
        return link.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Link> createLink(@RequestBody Link link) {
        Link createdLink = linkService.createLink(link);
        return ResponseEntity.status(201).body(createdLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Link> updateLink(@PathVariable UUID id, @RequestBody Link link) {
        Optional<Link> updatedLink = linkService.updateLink(id, link);
        return updatedLink.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable UUID id) {
        boolean deleted = linkService.deleteLink(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

