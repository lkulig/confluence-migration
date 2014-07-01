package com.lkulig.confluence.migration.page;

import static com.google.common.collect.Maps.newHashMap;
import org.codehaus.swizzle.confluence.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ConfluenceHomePageCreator {

    @Value("${confluence.to.space.name}")
    private String space;

    public Page createHomePage(String homePageName) {
        return new Page(homePageDetails(homePageName));
    }

    private Map<String, Object> homePageDetails(String homePageName) {
        Map<String, Object> pageDetails = newHashMap();

        pageDetails.put("space", space);
        pageDetails.put("homePage", true);
        pageDetails.put("title", homePageName);

        return pageDetails;
    }
}
