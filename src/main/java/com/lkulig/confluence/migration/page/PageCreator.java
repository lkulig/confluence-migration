package com.lkulig.confluence.migration.page;

import static com.google.common.collect.Maps.newHashMap;
import org.codehaus.swizzle.confluence.Page;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PageCreator {

    public Page createFrom(Page page, String parentId, String space) {
        Map<String, Object> pageDetails = newHashMap();
        pageDetails.put("space", space);
        pageDetails.put("homePage", false);
        pageDetails.put("title", page.getTitle());
        pageDetails.put("content", page.getContent());
        pageDetails.put("parentId", parentId);

        return new Page(pageDetails);
    }
}
