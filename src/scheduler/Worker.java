package scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class Worker implements Runnable {
    
    private final BlockingQueue<Job> jobQueue;
    private final String workerName;
    private volatile boolean running = true;
    
   
    public Worker(BlockingQueue<Job> jobQueue, String workerName) {
        this.jobQueue = jobQueue;
        this.workerName = workerName;
    }
    
    
    @Override
    public void run() {
        System.out.println(String.format("[%s] Worker started", workerName));
        
        while (running) {
            try {
                Job job = jobQueue.poll(100, TimeUnit.MILLISECONDS);
                
                if (job != null) {
                    System.out.println(String.format("[%s] Picked up %s", workerName, job));
                    job.run();
                }
                
            } catch (InterruptedException e) {
                System.out.println(String.format("[%s] Interrupted, shutting down...", workerName));
                Thread.currentThread().interrupt(); 
                break;
            } catch (Exception e) {
                System.err.println(String.format("[%s] Unexpected error: %s", workerName, e.getMessage()));
                e.printStackTrace();
            }
        }
        
        System.out.println(String.format("[%s] Worker stopped", workerName));
    }
    
   
    public void shutdown() {
        running = false;
    }
    
    public String getWorkerName() {
        return workerName;
    }
}