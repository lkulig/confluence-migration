package com.lkulig.confluence.migration.page;

import static com.google.common.base.Optional.absent;
import com.google.common.base.Optional;
import com.lkulig.confluence.client.ConfluenceClient;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConfluenceHomePageManager {

    @Autowired
    @Qualifier(value = "confluenceToClient")
    private ConfluenceClient confluenceClient;
    @Autowired
    private ConfluenceHomePageCreator confluenceHomePageCreator;
    @Value("${confluence.to.space.name}")
    private String spaceName;
    private Optional<Page> homePage = absent();

    public void createHomePage(String homePageName) {
        if (homePage == null) {
            confluenceClient.addOrUpdatePage(confluenceHomePageCreator.createHomePage(homePageName));
        }
    }

    public Optional<Page> load(String homePageName) {
        if (homePage == null) {
            homePage = confluenceClient.getPage(spaceName, homePageName);
        }
        return homePage;
    }

    public void removeAll() {
        if (homePage.isPresent()) {
            removeDescendantsOf(homePage.get());
            removeContentFrom(homePage.get());
        }
    }

    private void removeDescendantsOf(PageSummary page) {
        List<PageSummary> childPages = confluenceClient.getDescendentsOf(page);
        for (PageSummary childPage : childPages) {
            confluenceClient.removePage(childPage);
        }
    }

    private void removeContentFrom(Page homePage) {
        homePage.setContent("");
        confluenceClient.addOrUpdatePage(homePage);
    }
}
