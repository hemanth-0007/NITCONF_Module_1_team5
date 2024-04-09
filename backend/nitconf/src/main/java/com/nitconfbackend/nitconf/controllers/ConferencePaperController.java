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
import com.nitconfbackend.nitconf.models.ConferencePaper;
import com.nitconfbackend.nitconf.models.Tag;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.models.Status;

import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
import com.nitconfbackend.nitconf.repositories.ConferencePaperRepository;
import com.nitconfbackend.nitconf.repositories.TagsRepository;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.service.DocumentUtility;
import com.nitconfbackend.nitconf.types.ConferencePaperRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/abstract")
@SecurityRequirement(name = "bearerAuth")
public class ConferencePaperController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ConferencePaperRepository conferencePaperRepository;

    @Autowired
    private DocumentVersionRepository docRepo;

    @Autowired
    private DocumentUtility documentUtility;

    @Autowired
    private TagsRepository tagsRepo;

    @GetMapping("")
    public ResponseEntity<List<ConferencePaper>> getAllConferencePapers() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        List<ConferencePaper> conferencePapers = currentUser.getConferencePapers();
        if (conferencePapers == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(conferencePapers);
    }

    @PostMapping("")
    public ResponseEntity<ConferencePaper> newConferencePaper(@RequestBody ConferencePaperRequest entity) {
        if (entity == null)
            return ResponseEntity.badRequest().build();
        if (entity.title == null || entity.description == null
                || entity.status == null || entity.tags == null)
            return ResponseEntity.badRequest().build();

        List<Tag> tags = new ArrayList<Tag>();
        entity.tags.forEach(tag -> {
            if (tag != null) {
                Tag newTag = tagsRepo.findByTitle(tag.toString()).orElseThrow();
                tags.add(newTag);
            }
        });

        ConferencePaper paper = new ConferencePaper(
                entity.title,
                entity.description,
                entity.status,
                tags);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        conferencePaperRepository.save(paper);

        currentUser.getConferencePapers().add(paper);
        userRepo.save(currentUser);
        tags.forEach(tag -> {
            tag.getConferencePapers().add(paper);
            tagsRepo.save(tag);
        });

        return ResponseEntity.ok(paper);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConferencePaper> updateConferencePaper(@PathVariable String id,
            @RequestBody ConferencePaperRequest entity) {
        if (id == null || entity == null)
            return ResponseEntity.notFound().build();
        if (entity.title == null || entity.description == null
                || entity.status == null || entity.tags == null)
            return ResponseEntity.badRequest().build();
        ConferencePaper conferencePaper = conferencePaperRepository.findById(id).orElseThrow();

        List<Tag> tags = new ArrayList<Tag>();
        entity.tags.forEach(tag -> {
            if (tag != null) {
                Tag newTag = tagsRepo.findById(tag).orElseThrow();
                tags.add(newTag);
            }
        });

        conferencePaper.setTitle(entity.title);
        conferencePaper.setDescription(entity.description);
        conferencePaper.setStatus(entity.status);
        conferencePaper.setTags(tags);

        conferencePaperRepository.save(conferencePaper);

        return ResponseEntity.ok(conferencePaper);
    }

    @PutMapping("/doc/{id}")
    public ResponseEntity<?> uploadPdf(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        if (id == null || file == null)
            return ResponseEntity.badRequest().build();
        ConferencePaper session = conferencePaperRepository.findById(id).orElseThrow();
        try {
            byte[] data = documentUtility.pdfToByte(file);
            List<DocumentVersion> allDocs = session.getDocumentVersions();
            if (data == null)
                return ResponseEntity.notFound().build();
            DocumentVersion newDoc = new DocumentVersion(
                    "New Submission",
                    data,
                    allDocs.size() + 1
            // session
            );
            docRepo.save(newDoc);
            session.getDocumentVersions().add(newDoc);
            conferencePaperRepository.save(session);
        } 
        catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/doc/{id}")
    public ResponseEntity<Resource> getDocumentByConferencePaperId(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.notFound().build();
        ConferencePaper session = conferencePaperRepository.findById(id).orElseThrow();
        List<DocumentVersion> allDocs = session.getDocumentVersions();
        ByteArrayResource resource = documentUtility.downloadFile(allDocs);
        if (resource == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConferencePaper> getConferencePaper(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.notFound().build();
        ConferencePaper session = conferencePaperRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(session);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConferencePaper(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.badRequest().build();

        ConferencePaper conferencePaper = conferencePaperRepository.findById(id).orElseThrow();

        List<DocumentVersion> allDocs = conferencePaper.getDocumentVersions();
        if (allDocs != null && !allDocs.isEmpty())
            allDocs.forEach(doc -> {
                if (doc != null)
                    docRepo.delete(doc);
            });
        List<Tag> relatedTags = conferencePaper.getTags();
        if (relatedTags != null && !relatedTags.isEmpty())
            relatedTags.forEach(tag -> {
                if (tag != null) {
                    tag.getConferencePapers().remove(conferencePaper);
                    tagsRepo.save(tag);
                }
            });
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        // System.out.println(currentUser.getConferencePapers());
        currentUser.getConferencePapers().remove(conferencePaper);
        // System.out.println(currentUser.getConferencePapers());
        userRepo.save(currentUser);
        conferencePaperRepository.delete(conferencePaper);

        return ResponseEntity.ok("DELETED PAPER");
    }

}