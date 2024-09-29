package net.sonicrushxii.beyondthehorizon.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduledTask {
    private final Runnable task;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);
    private CompletableFuture<?> future = null;
    private int ticksRemaining;

    public ScheduledTask(Runnable task, int delayInTicks) {
        this.task = task;
        this.ticksRemaining = delayInTicks;
    }

    public void tick() {
        if (ticksRemaining > 0 && !isCancelled.get()) {
            ticksRemaining--;
        }
    }

    public boolean isReady() {
        return ticksRemaining <= 0 && !isCancelled.get();
    }

    public void run() {
        if (!isCancelled.get()) {
            future = CompletableFuture.runAsync(task, ThreadManager.getExecutorService());
        }
    }

    public void cancel() {
        isCancelled.set(true);
        if (future != null) {
            future.cancel(true);
        }
    }

    public boolean isCancelled() {
        return isCancelled.get();
    }
}
