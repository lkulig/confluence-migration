package com.lkulig.confluence.migration.service;

import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.migration.page.ConfluencePageExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConfluenceMigrationService {

    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;
    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient destination;
    @Autowired
    private ConfluencePageExporter confluencePageExporter;

    public void migrate() {
        destination.login();
        source.login();
        confluencePageExporter.export();
        source.logout();
        destination.logout();
    }
}
