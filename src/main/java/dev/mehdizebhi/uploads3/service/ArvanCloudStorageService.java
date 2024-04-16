package dev.mehdizebhi.uploads3.service;

import dev.mehdizebhi.uploads3.model.Attachment;
import dev.mehdizebhi.uploads3.utils.AttachmentAssembler;
import dev.mehdizebhi.uploads3.utils.AttachmentHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArvanCloudStorageService implements AttachmentService {

    private final S3Client client;
    private final AttachmentDaoService daoService;
    private final AttachmentAssembler assembler;

    @Value("${arvan.s3.bucket_name}")
    private String defaultBucketName;

    @Override
    public Optional<Attachment> save(Attachment attachment, boolean isPublic) {
        try {
            var putObjectBuilder = PutObjectRequest.builder()
                    .bucket(defaultBucketName)
                    .key(AttachmentHelper.getKey(attachment));

            if (isPublic) putObjectBuilder.acl(ObjectCannedACL.PUBLIC_READ);
            else putObjectBuilder.acl(ObjectCannedACL.PRIVATE);

            PutObjectRequest putOb = putObjectBuilder.build();

            client.putObject(putOb, RequestBody.fromBytes(attachment.getData()));
            log.info("File Uploaded!");

            return daoService.register(attachment).map(assembler::toModel);

        } catch (S3Exception e) {
            log.error("S3 has problem with uploading = {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Attachment>> saveAll(List<Attachment> attachments, boolean isPublic) {
        return Optional.of(attachments.stream()
                .map(attachment -> save(attachment, isPublic))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Attachment> loadById(ObjectId id) {
        try {
            var entity = daoService.findById(id).get();
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .key(AttachmentHelper.getKey(entity))
                    .bucket(defaultBucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(objectRequest);
            Attachment attachment = assembler.toModel(entity);
            attachment.setData(objectBytes.asByteArray());

            return Optional.of(attachment);
        } catch (S3Exception e) {
            log.error("S3 Client has problem with downloading fileId = {}", id);
            return Optional.empty();
        }
    }

    @Override
    public void delete(Attachment attachment) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(defaultBucketName)
                    .key(AttachmentHelper.getKey(attachment))
                    .build();

            client.deleteObject(deleteObjectRequest);
            daoService.delete(new ObjectId(attachment.getId()));
        } catch (S3Exception e) {
            log.error("S3 Client Can not delete object = {}", AttachmentHelper.getKey(attachment));
        }
    }

    @Override
    public void deleteById(ObjectId attachmentId) {
        var attachmentOp = daoService.findById(attachmentId);
        if (attachmentOp.isPresent()) {
            this.delete(attachmentOp.map(assembler::toModel).get());
        }
    }

    @Override
    public void deleteAll(List<Attachment> attachments) {
        List<ObjectIdentifier> keys = attachments.stream()
                .map(attachment -> ObjectIdentifier.builder().key(AttachmentHelper.getKey(attachment)).build())
                .collect(Collectors.toList());

        List<ObjectId> ids = attachments.stream()
                .map(attachment -> new ObjectId(attachment.getId()))
                .collect(Collectors.toList());

        Delete delete = Delete.builder()
                .objects(keys)
                .build();
        try {
            DeleteObjectsRequest  multiObjectDeleteRequest = DeleteObjectsRequest.builder()
                    .bucket(defaultBucketName)
                    .delete(delete)
                    .build();

            client.deleteObjects(multiObjectDeleteRequest);
            daoService.deleteAll(ids);
        } catch (S3Exception e) {
            log.error("S3 Client Can not delete objects = {}", keys);
        }
    }
}
