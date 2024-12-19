package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.PeerToPeerConnectionService;
import de.nodeline.box.application.primaryadapter.api.dto.PeerToPeerDto;

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
    private PeerToPeerConnectionService linkService;

    @GetMapping
    public ResponseEntity<List<PeerToPeerDto>> getAllLinks() {
        return ResponseEntity.ok(linkService.getAllLinks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeerToPeerDto> getLinkById(@PathVariable UUID id) {
        Optional<PeerToPeerDto> link = linkService.getLinkById(id);
        return link.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PeerToPeerDto> createLink(@RequestBody PeerToPeerDto link) {
        PeerToPeerDto createdLink = linkService.createLink(link);
        return ResponseEntity.status(201).body(createdLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeerToPeerDto> updateLink(@PathVariable UUID id, @RequestBody PeerToPeerDto link) {
        Optional<PeerToPeerDto> updatedLink = linkService.updateLink(id, link);
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

