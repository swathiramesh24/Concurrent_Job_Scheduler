package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scheduler{
  private final BlockingQueue<Job> jobQueue;
  private final List<Thread> workerThreads;
  private final List<Worker> workers;
  private final int poolSize;
  private final AtomicBoolean isShutdown;

  public Scheduler(int poolSize)
  {
    if(poolSize<=0)
    {
      throw new IllegalArgumentException("Pool Size must be positive");
    }
    this.poolSize=poolSize;
    this.jobQueue= new PriorityBlockingQueue<>();
    this.workerThreads= new ArrayList<>();
    this.workers= new ArrayList<>();
    this.isShutdown = new AtomicBoolean(false);

    initializeWorkers();
  }

  private void initializeWorkers()
  {
    for(int i=0;i<poolSize;i++)
    {
      Worker worker = new Worker(jobQueue, "Worker - " + (i+1));
      Thread thread = new Thread(worker);
      thread.setName("Worker - " + (i+1));
      workers.add(worker);
      workerThreads.add(thread);
      thread.start();
    }

    System.out.println(String.format("Job Scheduler initialized with %d workers", poolSize));
  }

  public void submitJob(Job job)
  {
    if(isShutdown.get())
    {
      throw new IllegalStateException("Cannot submit job - Scheduler is shutdown");
    }
    jobQueue.offer(job);
    System.out.println(String.format("Job submitted : %s", job));
  }

  public void submitJob(String name, int priority, Runnable task)
  {
    submitJob(new Job(name, priority, task));
  }

  public void shutdown()
  {
    if(isShutdown.getAndSet(true))
    {
      System.out.println("Scheduler already shutdown");
      return;
    }
    System.out.println("Initiating shutdown");

    for(Worker worker : workers)
    {
      worker.shutdown();
    }
  }

  public void shutDownNow()
  {
    shutdown();
    System.out.println("Initiating forced shutdown");

    for(Thread thread : workerThreads)
    {
      thread.interrupt();
    }
  }

  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
  {
    long startTime = System.currentTimeMillis();
    long timeOutMillis = unit.toMillis(timeout);

    for(Thread thread : workerThreads)
    {
      long elapsed = System.currentTimeMillis() - startTime;
      long remaining = timeOutMillis - elapsed;

      if(remaining<=0)
      {
        return false;
      }

      thread.join(remaining);

      if(thread.isAlive())
      {
        return false;
      }
    }
    System.out.println("All workers terminated successfully");
    return true;
  }

  public boolean isShutdown()
  {
    return isShutdown.get();
  }

  public int pendingJobs()
  {
    return jobQueue.size();
  }

  public int getPoolSize()
  {
    return poolSize;
  }

  public String getStatus()
  {
    int activeWorkers = (int) workerThreads.stream().filter(Thread::isAlive).count(); 

    return String.format("Scheduler{poolSize = %d, activeWorkers = %d, pendingJobs = %d, isShutdown = %s}",
                        poolSize, activeWorkers, pendingJobs(),isShutdown.get());
  }
}