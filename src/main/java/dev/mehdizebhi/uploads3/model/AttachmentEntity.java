package dev.mehdizebhi.uploads3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "attachments")
public class AttachmentEntity {
    private @MongoId ObjectId id;
    private @Indexed(unique = true) String fileId;
    private String fileName;
    private String fileType;
    private String fileExtension;
    private long fileSize;
    private @Indexed(unique = true) String uri;
    private @CreatedDate Date createdAt;
    private @LastModifiedDate Date updatedAt;
}
