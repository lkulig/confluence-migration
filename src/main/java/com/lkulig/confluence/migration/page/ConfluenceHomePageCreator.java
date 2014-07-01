package com.lkulig.confluence.migration.page;

import static com.lkulig.confluence.client.page.details.ConfluencePageDetailsBuilder.pageDetails;
import com.lkulig.confluence.client.page.ConfluencePage;
import com.lkulig.confluence.client.page.details.ConfluencePageDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfluenceHomePageCreator {

    @Value("${confluence.to.space.name}")
    private String space;

    public ConfluencePage createHomePage(String homePageName) {
        return new ConfluencePage(homePageDetails(homePageName));
    }

    private ConfluencePageDetails homePageDetails(String homePageName) {
        return pageDetails()
                .homePage()
                .space(space)
                .title(homePageName)
                .build();
    }
}
