package dev.mehdizebhi.uploads3.service;

import dev.mehdizebhi.uploads3.model.Attachment;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface AttachmentService {

    Optional<Attachment> save(Attachment attachment, boolean isPublic);

    Optional<List<Attachment>> saveAll(List<Attachment> attachments, boolean isPublic);

    Optional<Attachment> loadById(ObjectId id);

    void delete(Attachment attachment);

    void deleteById(ObjectId attachmentId);

    void deleteAll(List<Attachment> attachments);
}
