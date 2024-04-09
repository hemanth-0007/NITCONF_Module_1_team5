package com.nitconfbackend.nitconf.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session
 * Represents a session in the system.
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="sessions")
public class ConferencePaper {
     /**
     * Unique identifier for the session.
     */
    @Id public String id;
   /**
     * Title of the session.
     */
    public String title;
    /**
     * Description of the session.
     */
    public String description;
   /**
     * Status of the session.
     */
    public Status status;
   /**
     * Date when the session was created.
     */
    public Date date;

     /**
     * List of document versions associated with the session.
     */
    @DBRef
    public List<DocumentVersion> documentVersions;

     /**
     * Session Constructor
     * Constructs a new Session with the given parameters.
     *
     * @param title       Title of the session.
     * @param description Description of the session.
     * @param language    Language of the session.
     * @param level       Level of the session.
     * @param status      Status of the session.
     * @param tags        List of tags associated with the session.
     */
    public ConferencePaper(String title, String description, Status status, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.date = new Date();
        this.tags = tags;
        this.documentVersions = new ArrayList<>();
    }
      /**
     * List of tags associated with the session.
     */
    @DBRef
    public List<Tag> tags;
}
