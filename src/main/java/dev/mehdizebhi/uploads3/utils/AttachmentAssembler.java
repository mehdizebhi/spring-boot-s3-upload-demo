package dev.mehdizebhi.uploads3.utils;

import dev.mehdizebhi.uploads3.controller.AttachmentApi;
import dev.mehdizebhi.uploads3.controller.AttachmentController;
import dev.mehdizebhi.uploads3.model.Attachment;
import dev.mehdizebhi.uploads3.model.AttachmentEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AttachmentAssembler extends RepresentationModelAssemblerSupport<AttachmentEntity, Attachment> {

    @Value("${arvan.s3.bucket_name}")
    private String defaultBucketName;

    @Value("${arvan.s3.endpoint}")
    private String storageEndpoint;

    public AttachmentAssembler() {
        super(AttachmentController.class, Attachment.class);
    }

    @Override
    public Attachment toModel(AttachmentEntity entity) {
        if (Objects.nonNull(entity)) {
            Attachment model = Attachment.builder().build();
            BeanUtils.copyProperties(entity, model);
            model.setId(entity.getId().toString());
            model.setHref(getDownloadHref(entity).getHref());
            model.setCreatedAt(entity.getCreatedAt());
            model.setUpdatedAt(entity.getUpdatedAt());
            return model;
        }
        return null;
    }

    public Attachment multipartFileToModel (MultipartFile file) {
        String fileId = UUID.randomUUID().toString();
        try {
            return Attachment.builder()
                    .fileId(fileId)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileExtension(file.getContentType().split("/")[1])
                    .fileSize(file.getSize())
                    .data(file.getBytes())
                    .uri(getBasedUri() + "/" + fileId + "." + AttachmentHelper.getFileExtension(file.getOriginalFilename()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("can not get file's bytes");
        }
    }

    private List<Link> getLinks(AttachmentEntity entity) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(AttachmentApi.class).download(entity.getId())).withRel("download"));
        return links;
    }

    private Link getDownloadHref(AttachmentEntity entity) {
        return linkTo(methodOn(AttachmentApi.class).download(entity.getId())).withRel("download");
    }

    private String getBasedUri() {
        return storageEndpoint.substring(0,8) + defaultBucketName + "." + storageEndpoint.substring(8);
    }
}