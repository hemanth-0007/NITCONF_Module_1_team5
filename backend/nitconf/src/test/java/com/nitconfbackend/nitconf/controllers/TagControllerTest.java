package com.nitconfbackend.nitconf.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.nitconfbackend.nitconf.models.ConferencePaper;
import com.nitconfbackend.nitconf.models.Tag;
import com.nitconfbackend.nitconf.repositories.TagsRepository;
import com.nitconfbackend.nitconf.types.TagRequest;
import com.nitconfbackend.nitconf.service.TagService;

public class TagControllerTest {

    @Mock
    private TagsRepository tagsRepository;

    @InjectMocks
    private Tagcontroller tagController;

    @Mock
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindSessions() {
        // Prepare test data
        String id = "1234";
        Tag tag = new Tag();
        tag.setId(id);
        List<ConferencePaper> sessions = new ArrayList<>();
        ConferencePaper session1 = new ConferencePaper();
        ConferencePaper session2 = new ConferencePaper();
        sessions.add(session1);
        sessions.add(session2);
        tag.setConferencePapers(sessions);

        when(tagsRepository.findById(id)).thenReturn(Optional.of(tag));

        ResponseEntity<List<ConferencePaper>> responseEntity = tagController.FindSessions(id);

        assertEquals(sessions, responseEntity.getBody());
    }

    @Test
    public void testFindSessions_notExist() {
        String title = "Machine Learning";
        String invalid_title = "Invalid Title";
        Tag tag = new Tag();
        tag.setTitle(title);
        List<ConferencePaper> sessions = new ArrayList<>();
        ConferencePaper session1 = new ConferencePaper();
        sessions.add(session1);
        tag.setConferencePapers(sessions);

        when(tagsRepository.findByTitle(invalid_title)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> {
            tagController.FindSessions(invalid_title);
        });

    }

  
    @Test
    public void testNewTag() {
        TagRequest tagRequest = new TagRequest();
        tagRequest.setTitle("TestTag");
        Tag newTag = new Tag(tagRequest.getTitle());
 
        when(tagsRepository.save(any(Tag.class))).thenReturn(newTag);

        ResponseEntity<Tag> responseEntity = tagController.newtag(tagRequest);

        assertEquals(newTag, responseEntity.getBody());
    }

    @Test
    public void testFindAll() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tags.add(tag1);
        tags.add(tag2);

        when(tagsRepository.findAll()).thenReturn(tags);

        ResponseEntity<List<Tag>> responseEntity = tagController.FindAll();

        assertEquals(tags, responseEntity.getBody());
    }


}