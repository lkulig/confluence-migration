package com.lkulig.confluence.migration.job;

import static com.google.common.collect.Lists.newArrayList;
import com.lkulig.confluence.client.page.summary.ConfluencePageSummary;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluenceExportJobFactory {

    public List<ExportJob> createFrom(List<ConfluencePageSummary> confluencePageSummaries) {
        List<ExportJob> exportJobs = newArrayList();
        for(ConfluencePageSummary confluencePageSummary : confluencePageSummaries) {
            exportJobs.add(createExportJob(confluencePageSummary));
        }
        return exportJobs;
    }

    private ExportJob createExportJob(ConfluencePageSummary confluencePageSummary) {

        return new ExportJob(confluencePageSummary);
    }
}
