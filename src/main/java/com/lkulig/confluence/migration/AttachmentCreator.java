package com.lkulig.confluence.migration;

import org.codehaus.swizzle.confluence.Attachment;
import org.springframework.stereotype.Component;

@Component
public class AttachmentCreator {

    public Attachment createFrom(Attachment attachment, String pageId) {
        Attachment newAttachment = new Attachment();

        newAttachment.setPageId(pageId);
        newAttachment.setContentType(attachment.getContentType());
        newAttachment.setFileName(attachment.getFileName());

        return newAttachment;
    }
}
