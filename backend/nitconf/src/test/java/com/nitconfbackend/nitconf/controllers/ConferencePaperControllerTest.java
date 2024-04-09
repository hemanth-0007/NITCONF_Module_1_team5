package com.nitconfbackend.nitconf.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.nitconfbackend.nitconf.repositories.ConferencePaperRepository;
import com.nitconfbackend.nitconf.repositories.TagsRepository;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.service.DocumentUtility;
import com.nitconfbackend.nitconf.types.ConferencePaperRequest;
import com.nitconfbackend.nitconf.models.*;
import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;

public class ConferencePaperControllerTest {


    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConferencePaperRepository conferencePaperRepository;

    @Mock
    private TagsRepository tagsRepository;

    @Mock
    private DocumentUtility documentUtility;

    @InjectMocks
    private ConferencePaperController conferencePaperController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    // Testing newConferencePaper method

     @Test
    public void testNewConferencePaper() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("email@example.com");
        // Prepare the request
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setStatus(Status.PENDING);
        List<String> tags = new ArrayList<>();
        tags.add("Java Script");
        request.setTags(tags);

        User mockUser = new User();
        mockUser.setEmail("email@example.com");
        mockUser.conferencePapers = new ArrayList<ConferencePaper>();

        Tag mockTag = new Tag("Java Script");
        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(mockTag);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(tagsRepository.findByTitle(anyString())).thenReturn(Optional.of(mockTag));
        when(conferencePaperRepository.save(any(ConferencePaper.class))).thenReturn(new ConferencePaper());

     
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.newConferencePaper(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void testNewConferencePaperWithNullRequest() {
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.newConferencePaper(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testNewConferencePaperWithNullFeilds() {
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle(null);
        request.setDescription(null);
        request.setStatus(null);
        request.setTags(null);

        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.newConferencePaper(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testNewConferencePaperWithPartialNullFeilds() {
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setStatus(Status.PENDING);
        request.setTags(null);

        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.newConferencePaper(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }



    // Testing getAllConferencePapers method
    @Test
    public void testGetAllConferencePapers() {
        // Prepare the mock data
        List<ConferencePaper> mockConferencePapers = new ArrayList<>();
        mockConferencePapers.add(new ConferencePaper());
        mockConferencePapers.add(new ConferencePaper());
        mockConferencePapers.add(new ConferencePaper());

        User sampleUser = new User(
            "49832",
            "Arun",
            "Kumar",
            "arun@gmail.com",
            "arun@123",
            true,
            mockConferencePapers);
        
        when(authentication.getName()).thenReturn(sampleUser.getEmail());
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        ResponseEntity<List<ConferencePaper>> responseEntity = conferencePaperController.getAllConferencePapers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, responseEntity.getBody().size());
    }

    @Test
    public void testGetAllConferencePapersThrowsException() {
        when(authentication.getName()).thenThrow(new NoSuchElementException());
        assertThrows(NoSuchElementException.class, () -> {
            conferencePaperController.getAllConferencePapers();
        });
    }

    @Test
    public void testGetConferencePaperNullConferencePapers() {
        User sampleUser = new User(
            "49832",
            "Arun",
            "Kumar",
            "arun@gmail.com",
            "arun@123",
            true,
            null);
        when(authentication.getName()).thenReturn(sampleUser.getEmail());
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));
        ResponseEntity<List<ConferencePaper>> responseEntity = conferencePaperController.getAllConferencePapers();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    // Testing updateConferencePaper method

    @Test
    public void testUpdateConferencePaper() {
        when(authentication.getName()).thenReturn("hello@example.com");

        String conferencePaperId = "1";
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(Status.PENDING);
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        request.setTags(tags);

        ConferencePaper mockPaper = new ConferencePaper();
        mockPaper.setId(conferencePaperId);

        Tag mockTag = new Tag("Java");
        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(mockTag);

        when(conferencePaperRepository.findById(anyString())).thenReturn(Optional.of(mockPaper));
        when(tagsRepository.findById(anyString())).thenReturn(Optional.of(mockTag));
        when(conferencePaperRepository.save(any(ConferencePaper.class))).thenReturn(mockPaper);

        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.updateConferencePaper(conferencePaperId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
        

    @Test
    public void testUpdateConferencePaperWithNullId() {
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.updateConferencePaper(null, new ConferencePaperRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testUpdateConferencePaperWithNullRequest() {
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.updateConferencePaper("1", null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateConferencePaperWithNullFeilds() {
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle(null);
        request.setDescription(null);
        request.setStatus(null);
        request.setTags(null);

        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.updateConferencePaper("1", request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void testUpdateConferencePaperWithNoTagException() {
        when(authentication.getName()).thenReturn("hello@example.com");

        String conferencePaperId = "1";
        ConferencePaperRequest request = new ConferencePaperRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(Status.PENDING);
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        request.setTags(tags);

        ConferencePaper mockPaper = new ConferencePaper();
        mockPaper.setId(conferencePaperId);

        Tag mockTag = new Tag("Java");
        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(mockTag);

        when(conferencePaperRepository.findById(anyString())).thenReturn(Optional.of(mockPaper));
        when(tagsRepository.findById(anyString())).thenThrow(new NoSuchElementException());
        when(conferencePaperRepository.save(any(ConferencePaper.class))).thenReturn(mockPaper);

        assertThrows(NoSuchElementException.class, () -> {
            conferencePaperController.updateConferencePaper(conferencePaperId, request);
        });
    }

    // Testing getConferencePaper method

    @Test
    public void testGetConferencePaper() {
        String conferencePaperId = "1";
        ConferencePaper mockPaper = new ConferencePaper();
        mockPaper.setId(conferencePaperId);

        when(conferencePaperRepository.findById(conferencePaperId)).thenReturn(Optional.of(mockPaper));
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.getConferencePaper(conferencePaperId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetConferencePaperWithNullId() {
        ResponseEntity<ConferencePaper> responseEntity = conferencePaperController.getConferencePaper(null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetConferencePaperWithInvalidId() {
        String conferencePaperId = "1";
        when(conferencePaperRepository.findById(conferencePaperId)).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> {
            conferencePaperController.getConferencePaper(conferencePaperId);
        });
    }


    // Testing deleteConferencePaper method
    @Test
    public void testDeleteConferencePaper() {

        String validId = "validId";
        ConferencePaper paper = new ConferencePaper();
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        User mockUser = new User();
        mockUser.setEmail("email@example.com");
        mockUser.conferencePapers = new ArrayList<ConferencePaper>();
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("email@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(conferencePaperRepository.findById(validId)).thenReturn(Optional.of(paper));

        ResponseEntity<String> responseEntity = conferencePaperController.deleteConferencePaper(validId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETED PAPER", responseEntity.getBody());
    }

    @Test
    public void testDeleteConferencePaperWithNullId() {
        ResponseEntity<String> responseEntity = conferencePaperController.deleteConferencePaper(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteConferencePaperWithInvalidId() {
        String invalidId = "invalidId";
        when(conferencePaperRepository.findById(invalidId)).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> {
            conferencePaperController.deleteConferencePaper(invalidId);
        });
    }



    @Test
    public void testUploadPdf() throws Exception    {
        String id = "validId";
        byte[] pdfData = "PDF content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", pdfData);
        ConferencePaper conferencePaper = new ConferencePaper("Test Title", "Test Description", Status.PENDING, new ArrayList<>());
        List<DocumentVersion> allDocs = conferencePaper.getDocumentVersions();
        DocumentVersion newDoc = new DocumentVersion(
            "New Submission",
            pdfData,
            allDocs.size() + 1
        );

        
        when(conferencePaperRepository.findById(id)).thenReturn(Optional.of(conferencePaper));
        when(documentUtility.pdfToByte(mockFile)).thenReturn(pdfData);
        when(conferencePaperRepository.save(any(ConferencePaper.class))).thenReturn(conferencePaper);
        when(documentVersionRepository.save(any(DocumentVersion.class))).thenReturn(newDoc);

        ResponseEntity<?> responseEntity = conferencePaperController.uploadPdf(id, mockFile);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUploadPdfWithNullId(){
        ResponseEntity<?> responseEntity = conferencePaperController.uploadPdf(null, null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUploadPdfWithInvalidId(){

        String invalidId = "invalidId";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf",
        "Sample PDF content".getBytes());
        when(conferencePaperRepository.findById(invalidId)).thenThrow(new NoSuchElementException());
        assertThrows(NoSuchElementException.class, () -> {
            conferencePaperController.uploadPdf(invalidId, mockFile);
        });
    }


    @Test
    public void testGetDocumentByConferencePaperId(){
        String id = "validId";
        ConferencePaper conferencePaper = new ConferencePaper("Test Title", "Test Description", Status.PENDING, new ArrayList<>());
        DocumentVersion sampleDoc = new DocumentVersion(
            "changesDesc",
            "file".getBytes(),
            1
        );
        conferencePaper.getDocumentVersions().add(sampleDoc);
        when(conferencePaperRepository.findById(id)).thenReturn(Optional.of(conferencePaper));
        when(documentUtility.downloadFile(conferencePaper.getDocumentVersions()))
                .thenReturn(new ByteArrayResource(sampleDoc.getFile()));

        ResponseEntity<Resource> responseEntity = conferencePaperController.getDocumentByConferencePaperId(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetDocumentByConferencePaperIdWithNullId(){
        ResponseEntity<Resource> responseEntity = conferencePaperController.getDocumentByConferencePaperId(null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetDocumentByConferencePaperIdWithNullResource(){
       String id = "1";
         ConferencePaper conferencePaper = new ConferencePaper("Test Title", "Test Description", Status.PENDING, new ArrayList<>());
         when(conferencePaperRepository.findById(id)).thenReturn(Optional.of(conferencePaper));
         when(documentUtility.downloadFile(conferencePaper.getDocumentVersions())).thenReturn(null);
        ResponseEntity<Resource> responseEntity = conferencePaperController.getDocumentByConferencePaperId(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}