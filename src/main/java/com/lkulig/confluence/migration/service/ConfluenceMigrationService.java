package com.lkulig.confluence.migration.service;

import com.google.common.base.Optional;
import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.client.attachment.ConfluenceAttachment;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.client.page.summary.ConfluencePageSummary;
import com.lkulig.confluence.migration.AttachmentCreator;
import com.lkulig.confluence.migration.page.ConfluenceHomePageManager;
import com.lkulig.confluence.migration.page.ConfluencePageCreator;
import com.lkulig.confluence.migration.util.progress.ProgressLogger;
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
    @Value("${confluence.from.home.page.name}")
    private String sourceHomePage;
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
    private ConfluencePageCreator pageCreator;
    @Autowired
    private ConfluenceHomePageManager destinationHomePageManager;
    @Autowired
    private AttachmentCreator attachmentCreator;

    public void migrate() {
        destination.login();
        source.login();

        LOG.info("removing all pages from {}", sourceHomePage);
        destinationHomePageManager.load(sourceHomePage);
        destinationHomePageManager.removeAll();

        Optional<ConfluencePage> destinationPage = destination.getPage(confluenceToSpaceName, sourceHomePage);
        Optional<ConfluencePage> home = source.getPage(confluenceFromSpaceName, "Home");

        if (home.isPresent() && destinationPage.isPresent()) {
            List<ConfluencePageSummary> sourcePages = source.getChildrenOf(home.get());
            for (ConfluencePageSummary summary : sourcePages) {
                migrate(summary, source, destination, destinationPage.get().id());
            }
        }

        source.logout();
        destination.logout();
    }

    private void migrate(ConfluencePageSummary pageSummary, ConfluenceClient source, ConfluenceClient destination, String parentId) {
        LOG.info("migrating page id:[{}] , title:[{}]", pageSummary.id(), pageSummary.title());
        Optional<ConfluencePage> sourcePage = source.getPage(pageSummary.id());

        if (sourcePage.isPresent()) {
            Optional<ConfluencePage> createdPage;
            if (!destination.pageExists(confluenceToSpaceName, sourcePage.get().title())) {
                ConfluencePage newPage = pageCreator.createFrom(sourcePage.get(), parentId, confluenceToSpaceName);
                createdPage = destination.addOrUpdatePage(newPage);
            } else {
                createdPage = destination.getPage(confluenceToSpaceName, sourcePage.get().title());
            }

            ProgressLogger.incrementProcessedPages();

            exportAttachments(sourcePage.get(), createdPage.get().id());
            List<ConfluencePageSummary> sourcePageChildren = source.getChildrenOf(sourcePage.get());

            for (ConfluencePageSummary childPage : sourcePageChildren) {
                migrate(childPage, source, destination, createdPage.get().id());
            }
        }
    }

    private void exportAttachments(ConfluencePage sourcePage, String createdPageId) {
        List<ConfluenceAttachment> attachments = source.getAttachments(sourcePage.id());
        for (ConfluenceAttachment attachment : attachments) {
            exportAttachment(attachment, createdPageId);
        }
    }

    private void exportAttachment(ConfluenceAttachment attachment, String pageId) {
        LOG.info("migrating attachment id:[{}],  page id:[{}]", attachment.fileName(), pageId);
        ConfluenceAttachment attachmentToExport = attachmentCreator.createFrom(attachment, pageId);
        Optional<byte[]> attachmentData = source.getAttachmentData(attachment.pageId(), attachment.fileName(), "0");
        if (attachmentData.isPresent()) {
            destination.addAttachment(attachmentToExport, attachmentData.get());
        }
    }
}
