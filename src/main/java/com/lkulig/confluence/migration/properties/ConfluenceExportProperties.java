package com.lkulig.confluence.migration.properties;

public class ConfluenceExportProperties {

    private String sourceHomePage;
    private String destinationHomePage;
    private String sourceSpaceName;
    private String destinationSpaceName;

    public ConfluenceExportProperties(String sourceHomePage, String destinationHomePage, String sourceSpaceName,
                                      String destinationSpaceName) {
        this.sourceHomePage = sourceHomePage;
        this.destinationHomePage = destinationHomePage;
        this.sourceSpaceName = sourceSpaceName;
        this.destinationSpaceName = destinationSpaceName;
    }

    public String sourceHomePage() {
        return sourceHomePage;
    }

    public String destinationHomePage() {
        return destinationHomePage;
    }

    public String sourceSpaceName() {
        return sourceSpaceName;
    }

    public String destinationSpaceName() {
        return destinationSpaceName;
    }
}
