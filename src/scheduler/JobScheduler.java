package scheduler;

import java.util.concurrent.TimeUnit;

public class JobScheduler{
  /**
   * @param args
   * @throws InterruptedException
   */
  public static void main(String[]args) throws InterruptedException{
    System.out.println("---Testing Job Scheduler---");

    //Creating scheduler
    System.out.println("Creating scheduler with 3 workers");
    Scheduler scheduler = new Scheduler(3);
    System.out.println("Scheduler created successfully");
    System.out.println("Status: " + scheduler.getStatus() + "\n");
    Thread.sleep(500);

    //Submitting the job
    System.out.println("\n Submitting Jobs");

    scheduler.submitJob("Job 1", 5, ()->{
      System.out.println("Job 1 is executing");
      try{
        Thread.sleep(500);
      }
      catch(InterruptedException e)
      {
        
      }
    });

    scheduler.submitJob("Job 2", 10, ()->{
      System.out.println("Job 2 is executing");
      try
      {
        Thread.sleep(500);
      }
      catch(InterruptedException e)
      {

      }
    });

    scheduler.submitJob("Job 3", 12, ()->{
      System.out.println("Job 3 is executing");
      try
      {
        Thread.sleep(500);
      }
      catch(InterruptedException e)
      {

      }
    });

    System.out.println("Jobs Submitted Successfully");

    // Checking Concurrency
    System.out.println("\nTesting concurrent execution");
    System.out.println("Submitting 6 jobs to 3 workers");

    for(int i=1;i<=6;i++)
    {
      final int jobNum = i;
      scheduler.submitJob("Job" + i, 5, ()->{
        System.out.println("Job " + jobNum + " Start on "+ Thread.currentThread().getName());
        try
        {
          Thread.sleep(500);
        }
        catch(InterruptedException e)
        {

        }
      });
    }
    Thread.sleep(3000);

    //Status checking
    System.out.println("\n Checking status");
    System.out.println("Status: " + scheduler.getStatus());
    System.out.println("Pool size: " + scheduler.getPoolSize());
    System.out.println("Pending Jobs : " + scheduler.pendingJobs());
    System.out.println("Status checked");

    //Testing normal shutdown
    System.out.println("\nTesting graceful shutdown");
    scheduler.submitJob("Final Job", 13, ()->{
      System.out.println("Final job is executing");
      try
      {
        Thread.sleep(500);
      }
      catch(InterruptedException e)
      {

      }
    });
    Thread.sleep(100);
    scheduler.shutdown();

    boolean terminated = scheduler.awaitTermination(5, TimeUnit.SECONDS);

    if(terminated)
    {
      System.out.println("Terminated Successfully");
    }
    else
    {
      System.out.println("Termination timeout");
    }

    //Condition after shutdown
    System.out.println("\nVerifying shutdown");
    System.out.println("Final status: " + scheduler.getStatus());

    try{
      scheduler.submitJob("Fail Job",1, ()->{});
        System.out.println("Accepted a job after shutdown");
    }
    catch(IllegalStateException e)
    {
      System.out.println("Rejected job after shutdown");
    }

    System.out.println("\n All tests are successfull");
  }
}