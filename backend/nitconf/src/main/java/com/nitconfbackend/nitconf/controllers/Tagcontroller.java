package com.nitconfbackend.nitconf.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nitconfbackend.nitconf.models.Session;
import com.nitconfbackend.nitconf.models.Tag;
import com.nitconfbackend.nitconf.repositories.TagsRepository;
import com.nitconfbackend.nitconf.service.TagService;
import com.nitconfbackend.nitconf.types.TagRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tags;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller class for managing tags related to sessions.
 */
@RestController
@RequestMapping("/api/tags")
@SecurityRequirement(name = "bearerAuth")
public class Tagcontroller {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagsRepository repository;

     /**
     * Retrieves sessions associated with a specific tag title.
     *
     * @param title The title of the tag to search for.
     * @return ResponseEntity containing a list of sessions related to the specified tag.
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Session>> FindSessions(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.badRequest().build();
        Tag tag = repository.findById(id).orElseThrow();
        List<Session> relatedSessions = tag.getSessions();

        return ResponseEntity.ok(relatedSessions);
    }


     /**
     * Retrieves all tags.
     *
     * @return ResponseEntity containing a list of all tags.
     */
    @GetMapping("/findall")
    public ResponseEntity<List<Tag>> FindAll() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    /**
     * Creates a new tag.
     *
     * @param entity The request body containing the title of the new tag.
     * @return ResponseEntity containing the newly created tag.
     */
    @PostMapping("/newtag")
    public ResponseEntity<Tag> newtag(@RequestBody TagRequest entity) {
        return ResponseEntity.ok(tagService.CreateNewTag(entity.title));
    }

}
