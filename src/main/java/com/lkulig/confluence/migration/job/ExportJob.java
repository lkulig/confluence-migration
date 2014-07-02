package com.lkulig.confluence.migration.job;

import com.lkulig.confluence.client.page.summary.ConfluencePageSummary;

public class ExportJob {

    private ConfluencePageSummary confluencePageSummary;

    public ExportJob(ConfluencePageSummary confluencePageSummary) {
        this.confluencePageSummary = confluencePageSummary;
    }
}
