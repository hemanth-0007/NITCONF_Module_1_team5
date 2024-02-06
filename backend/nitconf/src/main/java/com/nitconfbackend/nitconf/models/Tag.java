package com.nitconfbackend.nitconf.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tag
 * Represents a tag associated with sessions in the system.
 * @since 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="tags")
public class Tag {
     /**
     * The unique identifier for the tag.
     */
    @Id public String id;
    /**
     * The title of the tag.
     */
    public String title;

     /**
     * List of sessions associated with this tag.
     */
    @DBRef
    @JsonIgnore
    public List<Session> sessions;

    /**
     * Constructs a new tag with the given title.
     * @param title The title of the tag.
     */
    public Tag(String title) {
        this.title = title;
        this.sessions= new ArrayList<Session>();
    }
}


