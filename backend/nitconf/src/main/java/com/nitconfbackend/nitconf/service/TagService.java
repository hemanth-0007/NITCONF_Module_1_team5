package com.nitconfbackend.nitconf.service;

import com.nitconfbackend.nitconf.models.ConferencePaper;
import com.nitconfbackend.nitconf.models.Tag;
import com.nitconfbackend.nitconf.repositories.TagsRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TagService
 * Service class for managing tags.
 * @since 1.0
 */
@Service

public class TagService {

    @Autowired
    private TagsRepository tagsRepository;

    /**
     * getAllTags
     * Retrieves all tags from the repository.
     *
     * @return List of all tags.
     */
    public List<Tag> getAllTags() {
        return tagsRepository.findAll();
    }
     /**
     * createNewTag
     * Creates a new tag with the given title and saves it to the repository.
     *
     * @param title The title of the new tag.
     * @return The newly created tag.
     */

    public Tag CreateNewTag(String title) {
        Tag newtag = new Tag(title);
        tagsRepository.save(newtag);
        return newtag;
    }
    /**
     * findSessions
     * Finds sessions related to the specified tag title.
     *
     * @param title The title of the tag to search for.
     * @return List of sessions related to the specified tag.
     * @throws RuntimeException if the tag with the given title is not found.
     */
    public List<ConferencePaper> findSessions(String id) {
        Tag tag = tagsRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        System.out.println("The sessions are : " + tag.getConferencePapers());
        List<ConferencePaper> sessions = tag.getConferencePapers();
        System.out.println(sessions.size());
        return sessions;
    }

    public List<ConferencePaper> findSessionByTitle(String title) {
        Tag tag = tagsRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Tag not found"));
        List<ConferencePaper> sessions = tag.getConferencePapers();
        System.out.println(sessions.size());
        return sessions;
    }
}
