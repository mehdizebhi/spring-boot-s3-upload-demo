package dev.mehdizebhi.uploads3.utils;

import dev.mehdizebhi.uploads3.model.Attachment;
import dev.mehdizebhi.uploads3.model.AttachmentEntity;

public class AttachmentHelper {

    public static String getKey(Attachment attachment) {
        return attachment.getFileId() + "." + AttachmentHelper.getFileExtension(attachment.getFileName());
    }

    public static String getKey(AttachmentEntity attachment) {
        return attachment.getFileId() + "." + AttachmentHelper.getFileExtension(attachment.getFileName());
    }

    public static String getFileExtension(String fileName){
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }
}
