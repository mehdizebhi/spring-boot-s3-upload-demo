package dev.mehdizebhi.uploads3.controller;

import dev.mehdizebhi.uploads3.model.Attachment;
import dev.mehdizebhi.uploads3.service.AttachmentDaoService;
import dev.mehdizebhi.uploads3.service.AttachmentService;
import dev.mehdizebhi.uploads3.utils.AttachmentAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AttachmentController implements AttachmentApi {

    private final AttachmentService attachmentService;
    private final AttachmentDaoService daoService;
    private final AttachmentAssembler assembler;

    @Override
    public ResponseEntity<Attachment> publicUpload(MultipartFile file) throws Exception {
        var attachment = attachmentService.save(assembler.multipartFileToModel(file), true);
        if (attachment.isPresent()) {
            return ResponseEntity.ok(attachment.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    public ResponseEntity<List<Attachment>> publicUploadAll(List<MultipartFile> files) {
        var attachments = files.stream()
                .map(file -> {
                    try {
                        return attachmentService.save(assembler.multipartFileToModel(file), true).get();
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(attachments);
    }

    @Override
    public ResponseEntity<Attachment> privateUpload(MultipartFile file) throws Exception {
        var attachment = attachmentService.save(assembler.multipartFileToModel(file), false);
        if (attachment.isPresent()) {
            return ResponseEntity.ok(attachment.get());
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<List<Attachment>> privateUploadAll(List<MultipartFile> files) {
        var attachments = files.stream()
                .map(file -> {
                    try {
                        return attachmentService.save(assembler.multipartFileToModel(file), false).get();
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(attachments);
    }

    @Override
    public ResponseEntity<Resource> download(ObjectId id) {
        Attachment attachment = attachmentService.loadById(id).get();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, attachment.getFileType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(attachment.getFileSize()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @Override
    public ResponseEntity<Attachment> getAttachmentInfo(ObjectId id) {
        var attachment = daoService.findById(id);
        if (attachment.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(attachment.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Attachment>> getAttachments(Pageable pageable) {
        return ResponseEntity.ok(daoService.findByPageable( pageable));
    }

    @Override
    public ResponseEntity<Void> deleteAttachment(ObjectId id) {
        attachmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
