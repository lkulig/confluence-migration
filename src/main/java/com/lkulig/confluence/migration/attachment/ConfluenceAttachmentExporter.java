package com.lkulig.confluence.migration.attachment;

import com.google.common.base.Optional;
import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.client.attachment.ConfluenceAttachment;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.migration.AttachmentCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluenceAttachmentExporter {

    private static final Logger LOG = LoggerFactory.getLogger(ConfluenceAttachmentExporter.class);
    @Autowired
    private AttachmentCreator attachmentCreator;
    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient destination;
    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;

    public void export(ConfluencePage sourcePage, String destinationPageId) {
        List<ConfluenceAttachment> attachments = source.getAttachments(sourcePage.id());
        for (ConfluenceAttachment attachment : attachments) {
            export(attachment, destinationPageId);
        }
    }

    private void export(ConfluenceAttachment attachment, String pageId) {
        LOG.info("migrating attachment id:[{}],  page id:[{}]", attachment.fileName(), pageId);
        ConfluenceAttachment attachmentToExport = attachmentCreator.createFrom(attachment, pageId);
        Optional<byte[]> attachmentData = source.getAttachmentData(attachment.pageId(), attachment.fileName(), "0");
        if (attachmentData.isPresent()) {
            destination.addAttachment(attachmentToExport, attachmentData.get());
        }

    }
}
