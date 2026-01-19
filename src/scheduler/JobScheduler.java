package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class JobScheduler {
    
    private final BlockingQueue<Job> jobQueue;
    private final List<Thread> workerThreads;
    private final List<Worker> workers;
    private final int poolSize;
    private final AtomicBoolean isShutdown;
    
  
    public JobScheduler(int poolSize) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be positive");
        }
        
        this.poolSize = poolSize;
        this.jobQueue = new PriorityBlockingQueue<>();
        this.workerThreads = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.isShutdown = new AtomicBoolean(false);
        
        initializeWorkers();
    }
    
    
    private void initializeWorkers() {
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(jobQueue, "Worker-" + (i + 1));
            Thread thread = new Thread(worker);
            thread.setName("Worker-" + (i + 1));
            
            workers.add(worker);
            workerThreads.add(thread);
            thread.start();
        }
        
        System.out.println(String.format("JobScheduler initialized with %d workers", poolSize));
    }
    
   
    public void submitJob(Job job) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Cannot submit job - scheduler is shutdown");
        }
        
        jobQueue.offer(job);
        System.out.println(String.format("Job submitted: %s (Queue size: %d)", job, jobQueue.size()));
    }
    
    
    public void submitJob(String name, int priority, Runnable task) {
        submitJob(new Job(name, priority, task));
    }
    
    public void shutdown() {
        if (isShutdown.getAndSet(true)) {
            System.out.println("Scheduler already shutdown");
            return;
        }
        
        System.out.println("Initiating scheduler shutdown...");
        

        for (Worker worker : workers) {
            worker.shutdown();
        }
    }
    
    public void shutdownNow() {
        shutdown();
        
        System.out.println("Forcing immediate shutdown - interrupting workers...");
        
        for (Thread thread : workerThreads) {
            thread.interrupt();
        }
    }
    
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = unit.toMillis(timeout);
        
        for (Thread thread : workerThreads) {
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = timeoutMillis - elapsed;
            
            if (remaining <= 0) {
                return false;
            }
            
            thread.join(remaining);
            
            if (thread.isAlive()) {
                return false;
            }
        }
        
        System.out.println("All workers terminated successfully");
        return true;
    }
   
    public int getPendingJobCount() {
        return jobQueue.size();
    }
    
    
    public int getPoolSize() {
        return poolSize;
    }
    
    public boolean isShutdown() {
        return isShutdown.get();
    }
    
   
    public String getStatus() {
        int activeWorkers = (int) workerThreads.stream()
                .filter(Thread::isAlive)
                .count();
        
        return String.format(
            "Scheduler{poolSize=%d, activeWorkers=%d, pendingJobs=%d, isShutdown=%s}",
            poolSize, activeWorkers, getPendingJobCount(), isShutdown.get()
        );
    }
}