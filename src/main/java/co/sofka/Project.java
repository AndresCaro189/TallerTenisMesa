package co.sofka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project {
    
    private final String name;
    private final ProjectType type;
    private String projectDetails = "Never Updated Project Details";
    private String lastUpdateTime = "Never Updated Last Update Time";
    private String loginStatistics = "Never Updated Login Statistics";
    private static final Logger logger = LoggerFactory.getLogger(Project.class);
    
    public Project(String name, ProjectType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return  name;
    }
    
    public ProjectType getType() {
        return type;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String string) {
        this.lastUpdateTime = string;
    }

    public String getLoginStatistics() {
        return loginStatistics;
    }

    public void setLoginStatistics(String loginStatistics) {
        this.loginStatistics = loginStatistics;
    }

    public void prettyPrint() {

        logger.info("Package.Project: {}", getName());
        logger.info("type: {}", getType());
        logger.info(getProjectDetails());
        logger.info(getLastUpdateTime());
        logger.info(getLoginStatistics());
    }
}
