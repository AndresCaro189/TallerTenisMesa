package co.sofka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;



/**
 * A class concerned with reloading a server-side cache of data, to avoid unnecessary 
 * and expensive calls outside the system (i.e. to persistence, to login server, etc).  
 * This is NOT good code, but it's realistic code... in that I found it in one of my 
 * systems.  We'll refactor it and make it better.
 * 
 * @author Abby B. Bullock
 * 
 */
public abstract class ProjectDataReloader {


    private static final Logger logger = LoggerFactory.getLogger(ProjectDataReloader.class);
    /**
     * Reload period is 30 seconds
     */
    private static final long RELOAD_PERIOD = 30000;
    
    /**
     * Sleep by small portions of 1 second
     */
    private static final long SLEEPING_PERIOD = 1000;

    
    private boolean stopped = false;


    
    protected final Project project;
    
    /**
     * Counter for how many times the reloadProjectData() method has been called,
     * used for reloading data types more or less often
     */
    protected int reloadsCounter = 0;
    
    public static ProjectDataReloader getReloaderForType(Project project) {
            ProjectType type = project.getType();
            if (type.equals(ProjectType.STATIC))
                return new StaticProjectDataReloader(project);
            if (type.equals(ProjectType.LIVE))
                return new LiveProjectDataReloader(project);

        return null;
    }

    protected ProjectDataReloader(Project project) {
        this.project = project;
    }
    
    /**
     * The reload method, called once per reload period;
     */
    protected abstract void reloadProjectData();
    
    public void start() {
        /**
         * The per-project reloading thread
         */
         Thread thread;
        // inline implementation of runnable for reloader thread
        thread = new Thread(() -> {
            logger.info("Starting project data reloading thread for project \"{}\", type: {}" ,project.getName() ,project.getType());

            stoppedstart();
        });

        thread.start();
    }

    private void stoppedstart() {
        while (!stopped) {
                long s = System.currentTimeMillis();
                if (relogMethottime()) break;

                // calculate the time taken for the reload
                long timeUsedForLastReload = System.currentTimeMillis() - s;

                // sleep until next fetch
                if (isaBoolean(timeUsedForLastReload)) {
                    sleepUntil(timeUsedForLastReload);
                }
                reloadsCounter++;
        }
        logger.info("Stopped project persistence reloading thread for project \"{}\"",project.getName());
    }

    private boolean isaBoolean(long timeUsedForLastReload) {
        return timeUsedForLastReload < RELOAD_PERIOD;
    }

    private void sleepUntil(long timeUsedForLastReload) {
        long timeLeftToSleep = RELOAD_PERIOD - timeUsedForLastReload;

        while (timeLeftToSleep > 0) {
            // check the termination flag
            synchronized (ProjectDataReloader.this) {
                if (stopped) {
                    break;
                }
            }
            tryCatchSleepPeriod(SLEEPING_PERIOD);

            // dec the timeLeft
            timeLeftToSleep -= SLEEPING_PERIOD;
        }
    }

    private static void tryCatchSleepPeriod(long sleepingPeriod) {
        try {
            // sleep for SLEEPING_PERIOD
            Thread.sleep(sleepingPeriod);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean relogMethottime() {

        try {
            // call a project-type-specific reloading procedure that reloads some of the project data from
            // persistence
            logger.info("Starting reloading for project {}",project.getName());
            reloadProjectData();
            logger.info("Done reloading for project {}",project.getName());
            project.prettyPrint();
            logger.info("-----------------------------------------------------------------");
            // check the termination flag
            synchronized (ProjectDataReloader.this) {
                if (stopped) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Could not load project data for ptoject {} : {}",project.getName(), e.getMessage());
        }
        return false;
    }

    protected void loadProjectDetails() {
        logger.info("Loading project details for project {}", project.getName());
        logger.info("(Talking to database and updating our project-related objects.)");
        //this could be a lot of lines of code and involve collaborators, helpers, etc
        //...
        //...
        //...
        //...
        //...
        //...
        //...
        //... Talk to database,
        //... Build domain objects,
        //... Update stuff
        //...
        //...
        //...
        //...
        //...
        //... Clear previously cached data
        //...
        //... Cache fresh data
        project.setProjectDetails("Package.Project details created: " + new Date(System.currentTimeMillis()));
    }
    
    protected void loadLastUpdateTime() {
        logger.info("Loading last update time for project {}",project.getName());
        logger.info("(Checking the database to see when the data was last refreshed)");
        // this might also be a lot of lines of code
        //...
        //...
        //...
        //...
        //...
        //...
        //... Look at database
        //...
        //...
        //...
        //...
        //... Clear previously cached data
        //...
        //... Cache fresh data
        project.setLastUpdateTime("Package.Project update time calculated: " + new Date(System.currentTimeMillis()));
    }
    
    protected void loadLoginStatistics() {
        logger.info("Loading login statistics for project {}",project.getName());
        logger.info("(Talking to our login server via http request)");
        // This might involve other collaborators/helpers to make the http request and
        // handle the response.
        //...
        //...
        //...
        //...
        //...
        //... Talk to login server
        //...
        //...
        //...
        //... Clear previously cached data
        //...
        //... Cache fresh data
        project.setLoginStatistics("Login statistics looked up: " + new Date(System.currentTimeMillis()));
    }
    
    public void stop() {
        
        logger.info("Stopping project persistence reloading thread for project {}", project.getName());
        
        stopped = true;
    }
    
    public static void main(String[] args) {
        ProjectDataReloader reloader1 = getReloaderForType(new Project("project1", ProjectType.STATIC));
        ProjectDataReloader reloader2 = getReloaderForType(new Project("project2", ProjectType.LIVE));

        assert reloader1 != null;
        reloader1.start();

        tryCatchSleepPeriod(SLEEPING_PERIOD);

        assert reloader2 != null;
        reloader2.start();

        tryCatchSleepPeriod(180000);

        reloader1.stop();
        reloader2.stop();
    }
}
