package com.lkulig.confluence.migration.page;

import static com.lkulig.confluence.client.page.details.ConfluencePageDetailsBuilder.pageDetails;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.client.page.details.ConfluencePageDetails;
import org.codehaus.swizzle.confluence.Page;
import org.springframework.stereotype.Component;

@Component
public class ConfluencePageCreator {

    public ConfluencePage createFrom(ConfluencePage page, String parentId, String space) {
        ConfluencePageDetails confluencePageDetails = pageDetails()
                .space(space)
                .normalPage()
                .title(page.title())
                .content(page.content())
                .parent(parentId)
                .build();

        return new ConfluencePage(confluencePageDetails);
    }
}
