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

import com.nitconfbackend.nitconf.models.Session;
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
        List<Session> sessions = new ArrayList<>();
        Session session1 = new Session();
        Session session2 = new Session();
        sessions.add(session1);
        sessions.add(session2);
        tag.setSessions(sessions);

        when(tagsRepository.findById(id)).thenReturn(Optional.of(tag));

        ResponseEntity<List<Session>> responseEntity = tagController.FindSessions(id);

        assertEquals(sessions, responseEntity.getBody());
    }

    @Test
    public void testFindSessions_notExist() {
        String title = "TestTag";
        String title1 = "NonExistent";
        Tag tag = new Tag();
        tag.setTitle(title);
        List<Session> sessions = new ArrayList<>();
        Session session1 = new Session();
        Session session2 = new Session();
        sessions.add(session1);
        sessions.add(session2);
        tag.setSessions(sessions);

        when(tagsRepository.findByTitle(title1)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> {
            tagController.FindSessions(title1);
        });

    }

  
    @Test
    public void testNewTag() {
        TagRequest tagRequest = new TagRequest();
        tagRequest.setTitle("TestTag");
        Tag newTag = new Tag(tagRequest.getTitle());
 
        when(tagsRepository.save(any(Tag.class))).thenReturn(newTag);

        Tag responseEntityTag = tagsRepository.save(newTag);

        assertEquals(newTag, responseEntityTag);
    }

    @Test
    public void testFindAll() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tags.add(tag1);
        tags.add(tag2);

        when(tagsRepository.findAll()).thenReturn(tags);

        List<Tag> responseEntity = tagsRepository.findAll();

        assertEquals(tags, responseEntity);
    }


}