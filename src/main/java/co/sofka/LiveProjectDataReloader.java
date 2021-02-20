package co.sofka;

/**
 * Reloader for a "live" project, where project data is expected to change
 * frequently.  Periodic reloads are necessary for time of last update, 
 * project details, and login statistics.
 * 
 */
public class LiveProjectDataReloader extends ProjectDataReloader {

    protected LiveProjectDataReloader(Project project) {
        super(project);
    }

    protected void reloadProjectData() {
        // load details every other reload attempt

        extracted1();

        //do this often
            new Thread(() -> loadLastUpdateTime()).start();

        // don't need this very often..
        // load login statistics every five hundred reload attempts
        if (reloadsCounter % 500 == 0) {
            new Thread(() -> loadLoginStatistics()).start();
        }
    }

    private void extracted1() {
        if (reloadsCounter % 2 == 0) {
            new Thread(() -> loadProjectDetails()).start();
        }
    }

}
