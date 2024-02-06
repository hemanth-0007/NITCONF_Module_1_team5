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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="sessions")
public class Session {
    @Id public String id;
    public String title;
    public String description;
    public Status status;
    // @JsonIgnore public User user;
    public Date date;

    @DBRef
    public List<Document> documentVersions;

    public Session(String title, String description, Status status, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.date = new Date();
        this.tags = tags;
        this.documentVersions = new ArrayList<>();
    }

    @DBRef
    public List<Tag> tags;
}
