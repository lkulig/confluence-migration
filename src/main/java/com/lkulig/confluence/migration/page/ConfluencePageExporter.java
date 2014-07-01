package com.lkulig.confluence.migration.page;

import com.google.common.base.Optional;
import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.client.page.summary.ConfluencePageSummary;
import com.lkulig.confluence.migration.attachment.ConfluenceAttachmentExporter;
import com.lkulig.confluence.migration.util.progress.ProgressLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluencePageExporter {

    private static final Logger LOG = LoggerFactory.getLogger(ConfluencePageExporter.class);
    @Value("${confluence.from.home.page.name}")
    private String sourceHomePage;
    @Value("${confluence.to.home.page.name}")
    private String destinationHomePage;
    @Value("${confluence.from.space.name}")
    private String confluenceFromSpaceName;
    @Value("${confluence.to.space.name}")
    private String confluenceToSpaceName;
    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient destination;
    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;
    @Autowired
    private ConfluenceHomePageManager destinationHomePageManager;
    @Autowired
    private ConfluencePageCreator pageCreator;
    @Autowired
    private ConfluenceAttachmentExporter confluenceAttachmentExporter;

    public void export() {
        destinationHomePageManager.load(destinationHomePage);
        Optional<ConfluencePage> destinationPage = destination.getPage(confluenceToSpaceName, destinationHomePage);
        Optional<ConfluencePage> home = source.getPage(confluenceFromSpaceName, sourceHomePage);
        if (home.isPresent() && destinationPage.isPresent()) {
            export(destinationPage, home);
        }
    }

    private void export(Optional<ConfluencePage> destinationPage, Optional<ConfluencePage> sourcePage) {
        List<ConfluencePageSummary> sourcePages = source.getChildrenOf(sourcePage.get());
        for (ConfluencePageSummary page : sourcePages) {
            exportSingle(page, destinationPage.get().id());
        }
    }

   private void exportSingle(ConfluencePageSummary pageSummary, String parentId) {
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

           confluenceAttachmentExporter.export(sourcePage.get(), createdPage.get().id());
           List<ConfluencePageSummary> sourcePageChildren = source.getChildrenOf(sourcePage.get());

           for (ConfluencePageSummary childPage : sourcePageChildren) {
               exportSingle(childPage, createdPage.get().id());
           }
       }
   }
}
