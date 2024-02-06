package com.nitconfbackend.nitconf.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nitconfbackend.nitconf.models.DocumentVersion;
import com.nitconfbackend.nitconf.models.Session;
import com.nitconfbackend.nitconf.models.Tag;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.models.Status;

import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
import com.nitconfbackend.nitconf.repositories.SessionRepository;
import com.nitconfbackend.nitconf.repositories.TagsRepository;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.service.DocumentUtility;
import com.nitconfbackend.nitconf.types.SessionRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/abstract")

public class AbstractController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SessionRepository sessionRepo;

    // @Autowired
    // private DocumentVersionRepository docRepo;

    // @Autowired
    // private DocumentUtility documentUtility;

    @Autowired
    private TagsRepository tagsRepo;


    @GetMapping("")
    public ResponseEntity<List<Session>> getAllSessions() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();

        List<Session> sessions = currentUser.getSessions();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("")
    public ResponseEntity<Session> newSession(@RequestBody SessionRequest entity) {
        if (entity.title == null || entity.description == null 
                || entity.status == null || entity.tags == null){
            System.out.println("Entity is null");       
            return ResponseEntity.badRequest().build();
            }
        System.out.println("Entity is not null");

        List<Tag> tags = new ArrayList<Tag>();
        entity.tags.forEach(tag -> {
            System.out.println("Tag: " + tag);
            if (tag != null) {
                Tag newTag = tagsRepo.findByTitle(tag.toString()).orElseThrow();
                tags.add(newTag);
            }
        });

        Session session = new Session(
                entity.title,
                entity.description,
                entity.status,
                tags
                );

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        // session.setUser(currentUser);

        sessionRepo.save(session);

        currentUser.getSessions().add(session);
        userRepo.save(currentUser);

        tags.forEach(tag -> {
            tag.getSessions().add(session);
            tagsRepo.save(tag);
        });

        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable String id, @RequestBody SessionRequest entity) {
        if (id == null)
            return ResponseEntity.notFound().build();
        if (entity.title == null || entity.description == null  || entity.status == null || entity.tags == null)          
            return ResponseEntity.badRequest().build();
        Session session = sessionRepo.findById(id).orElseThrow();

        List<Tag> tags = new ArrayList<Tag>();
        entity.tags.forEach(tag -> {
            if (tag != null) {
                Tag newTag = tagsRepo.findById(tag).orElseThrow();
                tags.add(newTag);
            }
        });

        session.setTitle(entity.title);
        session.setDescription(entity.description);
        session.setStatus(entity.status);
        session.setTags(tags);

        sessionRepo.save(session);

        return ResponseEntity.ok(session);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.notFound().build();
        Session session = sessionRepo.findById(id).orElseThrow();
        if (session != null)
            sessionRepo.delete(session);
        return ResponseEntity.ok("DELETED SESSION");
    }

}
// New session created to be added to user sessions