package com.lkulig.confluence.migration.service;

import com.google.common.base.Optional;
import com.lkulig.confluence.migration.AttachmentCreator;
import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.migration.page.ConfluenceHomePageManager;
import com.lkulig.confluence.migration.page.PageCreator;
import com.lkulig.confluence.migration.util.progress.ProgressLogger;
import org.codehaus.swizzle.confluence.Attachment;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluenceMigrationService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfluenceMigrationService.class);
    private static final String AIDA_DAM = "AIDA DAM XW";
    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;
    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient destination;
    @Value("${confluence.from.space.name}")
    private String confluenceFromSpaceName;
    @Value("${confluence.to.space.name}")
    private String confluenceToSpaceName;
    @Autowired
    private PageCreator pageCreator;
    @Autowired
    private ConfluenceHomePageManager destinationHomePageManager;
    @Autowired
    private AttachmentCreator attachmentCreator;

    public void migrate() {
        destination.login();
        source.login();

        LOG.info("removing all pages from {}", AIDA_DAM);
        destinationHomePageManager.load(AIDA_DAM);
        destinationHomePageManager.removeAll();

        Optional<Page> destinationPage = destination.getPage(confluenceToSpaceName, AIDA_DAM);
        Optional<Page> home = source.getPage(confluenceFromSpaceName, "Home");

        if(home.isPresent() && destinationPage.isPresent()) {
            List<PageSummary> sourcePages = source.getChildrenOf(home.get());
            for (PageSummary summary : sourcePages) {
                migrate(summary, source, destination, destinationPage.get().getId());
            }
        }

        source.logout();
        destination.logout();
    }

    private void migrate(PageSummary pageSummary, ConfluenceClient source, ConfluenceClient destination, String parentId) {
        LOG.info("migrating page id:[{}] , title:[{}]", pageSummary.getId(), pageSummary.getTitle());
        Optional<Page> sourcePage = source.getPage(pageSummary.getId());

        if(sourcePage.isPresent()) {
            Optional<Page> createdPage;
            if (!destination.pageExists(confluenceToSpaceName, sourcePage.get().getTitle())) {
                Page newPage = pageCreator.createFrom(sourcePage.get(), parentId, confluenceToSpaceName);
                createdPage = destination.addOrUpdatePage(newPage);
            } else {
                createdPage = destination.getPage(confluenceToSpaceName, sourcePage.get().getTitle());
            }

            ProgressLogger.incrementProcessedPages();

            exportAttachments(sourcePage.get(), createdPage.get().getId());
            List<PageSummary> sourcePageChildren = source.getChildrenOf(sourcePage.get());

            for (PageSummary childPage : sourcePageChildren) {
                migrate(childPage, source, destination, createdPage.get().getId());
            }
        }
    }

    private void exportAttachments(Page pageSummary, String pageId) {
        List<Attachment> attachments = source.getAttachments(pageSummary.getId());
        for (Attachment attachment : attachments) {
            exportAttachment(attachment, pageId);
        }
    }

    private void exportAttachment(Attachment attachment, String pageId) {
        try {
            LOG.info("migrating attachment id:[{}],  page id:[{}]", attachment.getFileName(), pageId);
            Attachment attachmentToExport = attachmentCreator.createFrom(attachment, pageId);
            Optional<byte[]> attachmentData = source.getAttachmentData(attachment.getPageId(), attachment.getFileName(), "0");
            if(attachmentData.isPresent()) {
                destination.addAttachment(attachmentToExport, attachmentData.get());
            }
        } catch (Exception e) {
            LOG.info("Failed to export attachment because: {}", e.getCause());
        }
    }
}
