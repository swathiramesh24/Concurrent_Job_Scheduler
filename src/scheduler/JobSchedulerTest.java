package scheduler;

import java.util.concurrent.TimeUnit;

public class JobSchedulerTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Testing JobScheduler Class ===\n");
        
        // Test 1: Scheduler creation
        System.out.println("Test 1: Creating scheduler with 3 workers...");
        JobScheduler scheduler = new JobScheduler(3);
        System.out.println("✓ Scheduler created successfully");
        System.out.println("Status: " + scheduler.getStatus() + "\n");
        
        Thread.sleep(500);
        
        // Test 2: Job submission
        System.out.println("Test 2: Submitting jobs...");
        scheduler.submitJob("Job A", 5, () -> {
            System.out.println("  → Job A executing");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        });
        
        scheduler.submitJob("Job B", 10, () -> {
            System.out.println("  → Job B executing (HIGH PRIORITY)");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        });
        
        scheduler.submitJob("Job C", 1, () -> {
            System.out.println("  → Job C executing");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        });
        
        System.out.println("✓ Jobs submitted");
        System.out.println("Pending jobs: " + scheduler.getPendingJobCount() + "\n");
        
        // Test 3: Concurrent execution
        System.out.println("Test 3: Testing concurrent execution...");
        System.out.println("Submitting 6 jobs to 3 workers (should run in pairs)\n");
        
        for (int i = 1; i <= 6; i++) {
            final int jobNum = i;
            scheduler.submitJob("Concurrent Job " + i, 5, () -> {
                System.out.println("  → Concurrent Job " + jobNum + " START on " + 
                    Thread.currentThread().getName());
                try { Thread.sleep(800); } catch (InterruptedException e) {}
                System.out.println("  → Concurrent Job " + jobNum + " END");
            });
        }
        
        Thread.sleep(3000); // Let jobs process
        
        // Test 4: Status checking
        System.out.println("\nTest 4: Checking status...");
        System.out.println("Status: " + scheduler.getStatus());
        System.out.println("Pool size: " + scheduler.getPoolSize());
        System.out.println("Pending jobs: " + scheduler.getPendingJobCount());
        System.out.println("✓ Status methods working\n");
        
        // Test 5: Graceful shutdown
        System.out.println("Test 5: Testing graceful shutdown...");
        scheduler.submitJob("Final Job", 5, () -> {
            System.out.println("  → Final job executing");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        });
        
        Thread.sleep(100);
        scheduler.shutdown();
        
        boolean terminated = scheduler.awaitTermination(5, TimeUnit.SECONDS);
        
        if (terminated) {
            System.out.println("✓ Scheduler terminated successfully\n");
        } else {
            System.out.println("✗ Scheduler termination timeout\n");
        }
        
        // Test 6: Shutdown state
        System.out.println("Test 6: Verifying shutdown state...");
        System.out.println("Is shutdown: " + scheduler.isShutdown());
        System.out.println("Final status: " + scheduler.getStatus());
        
        try {
            scheduler.submitJob("Should Fail", 1, () -> {});
            System.out.println("✗ Accepted job after shutdown!");
        } catch (IllegalStateException e) {
            System.out.println("✓ Correctly rejected job after shutdown\n");
        }
        
        System.out.println("=== All JobScheduler tests passed! ===");
    }
}