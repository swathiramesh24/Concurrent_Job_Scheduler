package demo;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import scheduler.Scheduler;

public class SchedulerDemo{
  private static final Random random = new Random();

  public static void main(String[]args){
    System.out.println("---Concurrent Job Scheduler---");

    //Demo Scenarios
    demonstrateBasicUsage();
    System.out.println("\n" + "=".repeat(50)+"\n");

    demonstratePriorityScheduling();
    System.out.println("\n" + "=".repeat(50)+"\n");

    demonstrateConcurrentExecution();
    System.out.println("\n" + "=".repeat(50)+"\n");

    demonstrateGracefulShutdown();
    
    System.out.println("\n---Demo Complete---");
  }

  //Basic Usage Function
  private static void demonstrateBasicUsage(){
    System.out.println("\n\n---Basic Usage---");

    Scheduler scheduler = new Scheduler(2);

    scheduler.submitJob("Calculate Sum", 1, ()->{
      int sum=0;
      for(int i=1;i<=10;i++)
      {
        sum += i;
      }
      System.out.println("Result of Sum: " + sum);
    });

    scheduler.submitJob("Print a message", 20, ()->{
      System.out.println("This is a message from a Job");
    });

    scheduler.submitJob("Product of Two Numbers", 7, ()->{
      int a=5,b=4;
      int product = a*b;
      System.out.println("Result of Product: " + product);
    });

    waitAndShutdown(scheduler,3);
  }

  //Priority Based Scheduling Function
  private static void demonstratePriorityScheduling(){
    System.out.println("\n\n---Priority Scheduling---");
    System.out.println("Submitting jobs with different priorities(high priority runs first)");

    Scheduler scheduler = new Scheduler(1);

    scheduler.submitJob("Low Priority Task", 1, createDelayedTask("LOW", 200));
    scheduler.submitJob("Medium Priority Task", 5, createDelayedTask("MEDIUM", 200));
    scheduler.submitJob("Critical Priority Task", 10, createDelayedTask("CRITICAL", 200));
    scheduler.submitJob("High Priority Task", 9, createDelayedTask("HIGH", 200));
    scheduler.submitJob("Another Low Priority Task", 2, createDelayedTask("LOW-2", 200));

    System.out.println("Despite submission order, jobs execute by priority");
    waitAndShutdown(scheduler,5);
  }

  //Concurrent execution of multiple workers
  private static void demonstrateConcurrentExecution(){
    System.out.println("\n\n---Concurrent Execution---");
    System.out.println("Using 4 workers to process jobs in parallel\n");

    Scheduler scheduler = new Scheduler(4);

    for(int i=1;i<=8;i++)
    {
      final int jobNum = i;
      scheduler.submitJob(
        "Parallel Job " + jobNum, 5, ()->{
          try{
            int processingTime = 800 + random.nextInt(400);
            System.out.println(String.format("[Job %d] Processing for %dms", jobNum,processingTime));
            Thread.sleep(processingTime);
            System.out.println(String.format("[Job %d] Done", jobNum));
          }
          catch(InterruptedException e)
          {
            Thread.currentThread().interrupt();
          }
        }
      );
    }

    waitAndShutdown(scheduler, 10);
  }

  //Graceful Shutdown 
  private static void demonstrateGracefulShutdown()
  {
    System.out.println("\n\n---Graceful Shutdown---");

    Scheduler scheduler = new Scheduler(2);

    for(int i=1;i<=6;i++)
    {
      final int jobNum=i;
      scheduler.submitJob(
        "Shutdown Test Job " + jobNum,
        i,
        createDelayedTask("Job-" + jobNum, 500)
      );
    }
    System.out.println("\nScheduler status: " + scheduler.getStatus());
    sleep(1000);

    System.out.println("Initiating graceful shutdown");
    scheduler.shutdown();

    System.out.println("Scheduler status after shutdown: " + scheduler.getStatus());

    try{
      boolean terminated = scheduler.awaitTermination(10, TimeUnit.SECONDS);
      if(terminated){
        System.out.println("All jobs are completed successfully");
      }
      else{
        System.out.println("Timeout waiting for jobs to complete");
      }
    }
    catch(InterruptedException e){
      Thread.currentThread().interrupt();
    }

    System.out.println("Final scheduler status: "+scheduler.getStatus());
  }

  //Delayed Task function
  private static Runnable createDelayedTask(String label, long delayMs){
    return ()->{
      try{
        Thread.sleep(delayMs);
        System.out.println(" [" + label + "] Task completed");
      }
      catch(InterruptedException e){
        Thread.currentThread().interrupt();
        System.out.println(" [" + label + "] Task interrupted");
      }
    };
  }

  //wait and shutdown function
  private static void waitAndShutdown(Scheduler scheduler, int waitSeconds){
    try{
      Thread.sleep(waitSeconds * 1000L);
      scheduler.shutdown();
      scheduler.awaitTermination(5,TimeUnit.SECONDS);
    }
    catch(InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }

  //Sleep method
  private static void sleep(long millis){
    try{
      Thread.sleep(millis);
    }
    catch(InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }
}