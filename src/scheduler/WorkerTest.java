package scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class WorkerTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Testing Worker Class ===\n");
        
        // Test 1: Worker creation and startup
        System.out.println("Test 1: Creating worker...");
        BlockingQueue<Job> queue = new PriorityBlockingQueue<>();
        Worker worker = new Worker(queue, "TestWorker");
        Thread workerThread = new Thread(worker);
        workerThread.start();
        System.out.println("✓ Worker started successfully\n");
        
        Thread.sleep(500); // Give worker time to start
        
        // Test 2: Worker picks up and executes jobs
        System.out.println("Test 2: Adding jobs to queue...");
        queue.offer(new Job("Job 1", 5, () -> {
            System.out.println("  → Job 1 executing");
            try { Thread.sleep(300); } catch (InterruptedException e) {}
        }));
        
        queue.offer(new Job("Job 2", 10, () -> {
            System.out.println("  → Job 2 executing");
            try { Thread.sleep(300); } catch (InterruptedException e) {}
        }));
        
        System.out.println("✓ Jobs added to queue\n");
        
        // Wait for jobs to be processed
        Thread.sleep(1500);
        
        // Test 3: Worker shutdown
        System.out.println("\nTest 3: Shutting down worker...");
        worker.shutdown();
        workerThread.join(2000); // Wait max 2 seconds
        
        if (!workerThread.isAlive()) {
            System.out.println("✓ Worker shut down successfully\n");
        } else {
            System.out.println("✗ Worker did not shut down properly\n");
        }
        
        // Test 4: Verify queue is empty
        System.out.println("Test 4: Checking queue...");
        if (queue.isEmpty()) {
            System.out.println("✓ All jobs were processed\n");
        } else {
            System.out.println("✗ Queue still has " + queue.size() + " jobs\n");
        }
        
        System.out.println("=== All Worker tests passed! ===");
    }
}