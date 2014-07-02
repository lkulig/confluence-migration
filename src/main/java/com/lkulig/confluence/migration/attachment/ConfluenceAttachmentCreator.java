package com.lkulig.confluence.migration.attachment;

import static com.lkulig.confluence.client.attachment.ConfluenceAttachmentBuilder.confluenceAttachment;
import com.lkulig.confluence.client.attachment.ConfluenceAttachment;
import org.springframework.stereotype.Component;

@Component
public class ConfluenceAttachmentCreator {

    public ConfluenceAttachment createFrom(ConfluenceAttachment attachment, String pageId) {
        return confluenceAttachment()
                .contentType(attachment.getContentType())
                .fileName(attachment.getFileName())
                .pageId(pageId)
                .build();
    }
}
