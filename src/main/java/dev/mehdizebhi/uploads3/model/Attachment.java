package dev.mehdizebhi.uploads3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment extends RepresentationModel<Attachment> {
    private String id;
    private @JsonIgnore String fileId;
    private String fileName;
    private String fileType;
    private String fileExtension;
    private long fileSize;
    private String href;
    private Date createdAt;
    private Date updatedAt;
    private String uri;
    private @JsonIgnore byte[] data;
}