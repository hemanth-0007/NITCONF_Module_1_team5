package com.nitconfbackend.nitconf.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nitconfbackend.nitconf.models.DocumentVersion;
/**
 * Service class for document-related utility methods.
 */
@Service
public class DocumentUtility {
    /**
     * Converts a PDF file to a byte array.
     *
     * @param file The PDF file to be converted.
     * @return Byte array representing the contents of the PDF file.
     * @throws IllegalStateException If the file cannot be processed.
     * @throws IOException           If an I/O exception occurs during file processing.
     */
    public byte[] pdfToByte(MultipartFile file) throws IllegalStateException, IOException {
        return file.getBytes();
    }
     /**
     * Downloads the latest version of a document as a ByteArrayResource.
     *
     * @param allDocs List of document versions.
     * @return ByteArrayResource representing the latest version of the document.
     */
    public ByteArrayResource downloadFile(List<DocumentVersion> allDocs) {
        allDocs.sort((a, b) -> b.getVersion().compareTo(a.getVersion()));
        int len = allDocs.size();
        if (len > 0) {
            DocumentVersion latestDoc = allDocs.get(len - 1);
            ByteArrayResource resource = new ByteArrayResource(latestDoc.getFile());
            return resource;
        }
        else 
            return null;
    }

}
