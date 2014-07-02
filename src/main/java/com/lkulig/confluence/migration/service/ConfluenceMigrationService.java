package com.lkulig.confluence.migration.service;

import static com.google.common.collect.Lists.newArrayList;
import com.google.common.base.Optional;
import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.migration.job.ConfluenceExportJobFactory;
import com.lkulig.confluence.migration.job.ExportJob;
import com.lkulig.confluence.migration.page.ConfluencePageExporter;
import com.lkulig.confluence.migration.properties.ConfluenceExportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluenceMigrationService {

    @Autowired
    private ConfluenceExportProperties properties;
    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;
    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient destination;
    @Autowired
    private ConfluencePageExporter confluencePageExporter;
    @Autowired
    private ConfluenceExportJobFactory exportJobFactory;

    public void migrate() {
        destination.login();
        source.login();

        List<ExportJob> exportJobs = createExportJobs();

        confluencePageExporter.export();
        source.logout();
        destination.logout();
    }

    private List<ExportJob> createExportJobs() {
        Optional<ConfluencePage> sourceHomePage = source.getPage(properties.sourceSpaceName(), properties.sourceHomePage());
        if (sourceHomePage.isPresent()) {
            return exportJobFactory.createFrom(source.getChildrenOf(sourceHomePage.get()));
        }
        return newArrayList();
    }
}
