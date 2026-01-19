package demo;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import scheduler.JobScheduler;


public class SchedulerDemo {
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        System.out.println("=== Concurrent Job Scheduler Demo ===\n");
        
        demonstrateBasicUsage();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstratePriorityScheduling();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateConcurrentExecution();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateGracefulShutdown();
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    /**
     * Demo 1: Basic job submission and execution
     */
    private static void demonstrateBasicUsage() {
        System.out.println("--- Demo 1: Basic Usage ---");
        
        JobScheduler scheduler = new JobScheduler(2);
     
        scheduler.submitJob("Calculate Sum", 1, () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            System.out.println("Sum result: " + sum);
        });
        
        scheduler.submitJob("Print Message", 1, () -> {
            System.out.println("Hello from scheduled job!");
        });
        
        scheduler.submitJob("Sleep Task", 1, () -> {
            try {
                Thread.sleep(500);
                System.out.println("Woke up from sleep!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        waitAndShutdown(scheduler, 3);
    }
    
    /**
     * Demo 2: Priority-based job scheduling
     */
    private static void demonstratePriorityScheduling() {
        System.out.println("--- Demo 2: Priority Scheduling ---");
        System.out.println("Submitting jobs with different priorities (higher priority runs first)\n");
        
        JobScheduler scheduler = new JobScheduler(1); // Single worker to show priority clearly
 
        scheduler.submitJob("Low Priority Task", 1, createDelayedTask("LOW", 200));
        scheduler.submitJob("Medium Priority Task", 5, createDelayedTask("MEDIUM", 200));
        scheduler.submitJob("Critical Task", 10, createDelayedTask("CRITICAL", 200));
        scheduler.submitJob("High Priority Task", 8, createDelayedTask("HIGH", 200));
        scheduler.submitJob("Another Low Task", 2, createDelayedTask("LOW-2", 200));
        
        System.out.println("\nNote: Despite submission order, jobs execute by priority\n");
        
        waitAndShutdown(scheduler, 5);
    }
    
    /**
     * Demo 3: Concurrent execution with multiple workers
     */
    private static void demonstrateConcurrentExecution() {
        System.out.println("--- Demo 3: Concurrent Execution ---");
        System.out.println("Using 4 workers to process jobs in parallel\n");
        
        JobScheduler scheduler = new JobScheduler(4);

        for (int i = 1; i <= 8; i++) {
            final int jobNum = i;
            scheduler.submitJob(
                "Parallel Job " + jobNum,
                5,
                () -> {
                    try {
                        int processingTime = 800 + random.nextInt(400);
                        System.out.println(String.format("  [Job %d] Processing for %dms...", 
                            jobNum, processingTime));
                        Thread.sleep(processingTime);
                        System.out.println(String.format("  [Job %d] Done!", jobNum));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            );
        }
        
        System.out.println("\nWatch multiple jobs execute simultaneously:\n");
        
        waitAndShutdown(scheduler, 10);
    }
    
    /**
     * Demo 4: Graceful shutdown with pending jobs
     */
    private static void demonstrateGracefulShutdown() {
        System.out.println("--- Demo 4: Graceful Shutdown ---");
        
        JobScheduler scheduler = new JobScheduler(2);
        
        for (int i = 1; i <= 6; i++) {
            final int jobNum = i;
            scheduler.submitJob(
                "Shutdown Test Job " + jobNum,
                i,
                createDelayedTask("Job-" + jobNum, 500)
            );
        }
        
        System.out.println("\nScheduler status: " + scheduler.getStatus());
        
        sleep(1000);
        
        System.out.println("\nInitiating graceful shutdown...");
        scheduler.shutdown();
        
        System.out.println("Scheduler status after shutdown: " + scheduler.getStatus());
        System.out.println("Waiting for remaining jobs to complete...\n");
        
        try {
            boolean terminated = scheduler.awaitTermination(10, TimeUnit.SECONDS);
            if (terminated) {
                System.out.println("\n✓ All jobs completed successfully");
            } else {
                System.out.println("\n✗ Timeout waiting for jobs to complete");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final scheduler status: " + scheduler.getStatus());
    }
    
    
    private static Runnable createDelayedTask(String label, long delayMs) {
        return () -> {
            try {
                Thread.sleep(delayMs);
                System.out.println("  [" + label + "] Task completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("  [" + label + "] Task interrupted");
            }
        };
    }
    
    
    private static void waitAndShutdown(JobScheduler scheduler, int waitSeconds) {
        try {
            Thread.sleep(waitSeconds * 1000L);
            scheduler.shutdown();
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}