// package com.nitconfbackend.nitconf.controllers;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.ByteArrayResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.nitconfbackend.nitconf.models.DocumentVersion;
// import com.nitconfbackend.nitconf.models.Session;
// import com.nitconfbackend.nitconf.models.Tag;
// import com.nitconfbackend.nitconf.models.User;
// import com.nitconfbackend.nitconf.models.Status;

// import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
// import com.nitconfbackend.nitconf.repositories.SessionRepository;
// import com.nitconfbackend.nitconf.repositories.TagsRepository;
// import com.nitconfbackend.nitconf.repositories.UserRepository;
// import com.nitconfbackend.nitconf.service.DocumentUtility;
// import com.nitconfbackend.nitconf.types.SessionRequest;

// import io.swagger.v3.oas.annotations.security.SecurityRequirement;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;

// @RestController
// @RequestMapping("/api/abstract")
// @SecurityRequirement(name = "bearerAuth")
// public class AbstractController {
//     @Autowired
//     private UserRepository userRepo;

//     @Autowired
//     private SessionRepository sessionRepo;

//     @Autowired
//     private DocumentVersionRepository docRepo;

//     @Autowired
//     private DocumentUtility documentUtility;

//     @Autowired
//     private TagsRepository tagsRepo;

//     @GetMapping("/all")
//     public ResponseEntity<List<Session>> getAllSessions() {

//         // String email = SecurityContextHolder.getContext().getAuthentication().getName();
//         // User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         // System.out.println("email: " + user.email);
//         // System.out.println("Email: " + email);
//         // User currentUser = userRepo.findByEmail(email).orElseThrow();
//         // User currentUser = userRepo.findByEmail(email).orElseThrow();
//         // System.out.println("User: " + currentUser);
        
        
//         // List<Session> sessions = user.getSessions();
//             // System.out.println("Sessions: " + sessions);
    
//             // if (sessions == null)
//                 // return ResponseEntity.notFound().build();
//             List<Session> sessions = sessionRepo.findAll();
//             return ResponseEntity.ok(sessions);
//     }

//     @GetMapping("")
//     public ResponseEntity<List<Session>> getAllUserSessions() {

//         User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         // String email = SecurityContextHolder.getContext().getAuthentication().getName();
//         // String email = "sai@gmail.com";
//         // User currentUser = userRepo.findById(user.getId()).orElseThrow();

//         System.out.println("User: " + currentUser);
    
//         List<Session> sessions = currentUser.getSessions();
//         System.out.println("Sessions: " + sessions);

//         if (sessions == null)
//             return ResponseEntity.notFound().build();
        
//         return ResponseEntity.ok(sessions);
//     }

//     @PostMapping("")
//     public ResponseEntity<Session> newSession(@RequestBody SessionRequest entity) {
//         if (entity.title == null || entity.description == null 
//                 || entity.status == null || entity.tags == null){
//             System.out.println("Entity is null");       
//             return ResponseEntity.badRequest().build();
//             }
//         System.out.println("Entity is not null");

//         List<Tag> tags = new ArrayList<Tag>();
//         entity.tags.forEach(tag -> {
//             System.out.println("Tag: " + tag);
//             if (tag != null) {
//                 Tag newTag = tagsRepo.findByTitle(tag.toString()).orElseThrow();
//                 tags.add(newTag);
//             }
//         });

//         Session session = new Session(
//                 entity.title,
//                 entity.description,
//                 entity.status,
//                 tags
//                 );

//         String email = SecurityContextHolder.getContext().getAuthentication().getName();
//         User currentUser = userRepo.findByEmail(email).orElseThrow();
//         // session.setUser(currentUser);

//         sessionRepo.save(session);

//         currentUser.getSessions().add(session);
//         userRepo.save(currentUser);

//         tags.forEach(tag -> {
//             tag.getSessions().add(session);
//             tagsRepo.save(tag);
//         });

//         return ResponseEntity.ok(session);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Session> updateSession(@PathVariable String id, @RequestBody SessionRequest entity) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         if (entity.title == null || entity.description == null  || entity.status == null || entity.tags == null)          
//             return ResponseEntity.badRequest().build();
//         Session session = sessionRepo.findById(id).orElseThrow();

//         List<Tag> tags = new ArrayList<Tag>();
//         entity.tags.forEach(tag -> {
//             if (tag != null) {
//                 Tag newTag = tagsRepo.findById(tag).orElseThrow();
//                 tags.add(newTag);
//             }
//         });

//         session.setTitle(entity.title);
//         session.setDescription(entity.description);
//         session.setStatus(entity.status);
//         session.setTags(tags);

//         sessionRepo.save(session);

//         return ResponseEntity.ok(session);
//     }

//     @PutMapping("/doc/{id}")
//     public ResponseEntity<?> uploadPdf(@PathVariable String id, @RequestParam("file") MultipartFile file) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         Session session = sessionRepo.findById(id).orElseThrow();
//         try {
//             byte[] data = documentUtility.pdfToByte(file);
//             List<DocumentVersion> allDocs = session.getDocumentVersions();
//             if (data == null)
//                 return ResponseEntity.notFound().build();
//             DocumentVersion newDoc = new DocumentVersion(
//                     "New Submission",
//                     data,
//                     allDocs.size() + 1
//             // session
//             );
//             docRepo.save(newDoc);
//             session.getDocumentVersions().add(newDoc);
//             sessionRepo.save(session);
//         } catch (Exception e) {
//             System.err.println(e.getMessage());
//             return ResponseEntity.badRequest().build();
//         }

//         return ResponseEntity.ok().build();
//     }

//     @GetMapping("/doc/{id}")
//     public ResponseEntity<Resource> getDocument(@PathVariable String id) {
//         if (id == null){
//             System.out.println("Id is null");
//             return ResponseEntity.notFound().build();
//         }
//         try {
//             System.out.println("Id is not null");
//             Session session = sessionRepo.findById(id).orElseThrow();
//             // System.out.println("Session: " + session);
//             List<DocumentVersion> allDocs = session.getDocumentVersions();
//             System.out.println("AllDocs size: " + allDocs.size());
//             ByteArrayResource resource = documentUtility.downloadFile(allDocs);
//             // System.out.println("Resource: " + resource);
//             System.out.println("Resource: " + resource.contentLength());
//             if (resource == null){
//                 return ResponseEntity.badRequest().build();
//             }
//             return ResponseEntity.ok()
//                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + id + ".pdf")
//                     .contentType(MediaType.APPLICATION_PDF)
//                     .contentLength(resource.contentLength())
//                     .body(resource);
//             // return ResponseEntity.ok(session2);
//         } catch (Exception e) {
//             System.err.println(e.getMessage());
//             return ResponseEntity.badRequest().build();
//         }
//     }
 

//     @GetMapping("/{id}")
//     public ResponseEntity<Session> getSession(@PathVariable String id) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         Session session = sessionRepo.findById(id).orElseThrow();
//         return ResponseEntity.ok(session);
//     }

//     @PutMapping("/status/accepted/{id}")
//     public ResponseEntity<String> updateStatusToAccepted(@PathVariable String id) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         Session session = sessionRepo.findById(id).orElseThrow();
//         session.setStatus(Status.ACCEPTED);
//         return ResponseEntity.ok("UPDATED STATUS TO ACCEPTED");
//     }

//     @PutMapping("/status/rejected/{id}")
//     public ResponseEntity<String> updateStatusToRejected(@PathVariable String id) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         Session session = sessionRepo.findById(id).orElseThrow();
//         session.setStatus(Status.REJECTED);
//         return ResponseEntity.ok("UPDATED STATUS TO REJECTED");
//     }

     
//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> deleteSession(@PathVariable String id) {
//         if (id == null)
//             return ResponseEntity.notFound().build();
//         Session session = sessionRepo.findById(id).orElseThrow();
        
//         if (session != null)
//             sessionRepo.delete(session);
//         return ResponseEntity.ok("DELETED SESSION");
//     }

// }
// // New session created to be added to user sessions