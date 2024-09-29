package net.sonicrushxii.beyondthehorizon.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(6);

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void shutdown() {
        executorService.shutdown();
    }
}
