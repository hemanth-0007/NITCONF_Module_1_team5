package com.nitconfbackend.nitconf.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
// import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DocumentVersion
 * Represents a version of a document in the system.
 * @since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="document")
public class DocumentVersion {
    
    /**
     * Unique identifier for the document version.
     */
    @Id public String id;
    
    /**
     * Description of changes made in this version.
     */
    public String changesDesc;
     /**
     * Binary data of the document file.
     */
    @NonNull 
    @JsonIgnore
    public byte[] file;
    /**
     * Version number of the document.
     */
    @NonNull public Integer version;
    /**
     * Date when the document version was created.
     */
    @JsonIgnore @NonNull public Date date;
    /**
     * List of reviews associated with this document version.
     */
    @DBRef
    public List<Review> reviews;
    
    // @DBRef
    // public Session session;
    
     /**
     * DocumentVersion Constructor
     * Constructs a new DocumentVersion with the given parameters.
     *
     * @param changesDesc Description of changes made in this version.
     * @param file        Binary data of the document file.
     * @param version     Version number of the document.
     */
    public DocumentVersion(String changesDesc, @NonNull byte[] file, @NonNull Integer version) {
        this.changesDesc = changesDesc;
        this.file = file;
        this.version = version;
        this.date = new Date();
        this.reviews = new ArrayList<Review>();
        // this.session = session;
    }
}
