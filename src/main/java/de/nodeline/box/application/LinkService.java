package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.LinkRepositoryInterface;
import de.nodeline.box.domain.model.Link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LinkService {

    @Autowired
    private LinkRepositoryInterface linkRepository;

    public List<Link> getAllLinks() {
        return new ArrayList<>(linkRepository.findAll());
    }

    public Optional<Link> getLinkById(UUID id) {
        return linkRepository.findById(id);
    }

    public Link createLink(Link link) {
        return linkRepository.save(link);
    }

    public Optional<Link> updateLink(UUID id, Link link) {
        if(linkRepository.existsById(id)) {
            return Optional.of(linkRepository.save(link));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteLink(UUID id) {
        if(linkRepository.existsById(id)) {
            linkRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

