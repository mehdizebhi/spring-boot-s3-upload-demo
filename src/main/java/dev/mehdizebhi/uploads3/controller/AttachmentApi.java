package dev.mehdizebhi.uploads3.controller;

import dev.mehdizebhi.uploads3.model.Attachment;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("api/v1/attachments")
public interface AttachmentApi {

    @PostMapping("/public-upload")
    ResponseEntity<Attachment> publicUpload(
            @RequestPart MultipartFile file
    ) throws Exception;

    @PostMapping("/public-upload-all")
    ResponseEntity<List<Attachment>> publicUploadAll(
            @RequestPart List<MultipartFile> files
    );

    @PostMapping("/upload")
    ResponseEntity<Attachment> privateUpload(
            @RequestPart MultipartFile file
    ) throws Exception;

    @PostMapping("/upload-all")
    ResponseEntity<List<Attachment>> privateUploadAll(
            @RequestPart List<MultipartFile> files
    );

    @GetMapping("/download/{id}")
    ResponseEntity<Resource> download(
            @PathVariable("id") ObjectId id
    );

    @GetMapping("/{id}")
    ResponseEntity<Attachment> getAttachmentInfo(
            @PathVariable("id") ObjectId id
    );

    @GetMapping("")
    ResponseEntity<List<Attachment>> getAttachments(
            @PageableDefault(sort = {"updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAttachment(
            @PathVariable("id") ObjectId id
    );
}
