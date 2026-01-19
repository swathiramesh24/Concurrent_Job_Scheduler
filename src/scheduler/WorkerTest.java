package scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class WorkerTest{
  public static void main(String[]args) throws InterruptedException{
    System.out.println("---Testing the Worker Class---");

    BlockingQueue<Job> queue = new PriorityBlockingQueue<>();

    //Adding jobs in the queue
    System.out.println("\nAdding Jobs to Queue");
    queue.offer(new Job("Job 1", 5, ()->{
      System.out.println("Job 1 is executing");
      try{
        Thread.sleep(300);
      }
      catch(InterruptedException e){

      }
    }));

    queue.offer(new Job("Job 2", 10, ()->{
      System.out.println("Job 2 is executing");
      try{
        Thread.sleep(300);
      }
      catch(InterruptedException e)
      {

      }
    }));
    
    Thread.sleep(2000);

    //Creating workers
    System.out.println("\nCreating worker");
    Worker worker = new Worker(queue, "Test Worker");
    Thread workerThread = new Thread(worker);
    workerThread.start();
    System.out.println("Worker started successfully");
    Thread.sleep(500);
    

    //Shutting down
    System.out.println("\nShutting down worker");
    worker.shutdown();

    //Adding new Job to see if it taken after shutdown
    System.out.println("\nAdding Jobs to Queue after shutdown");
    queue.offer(new Job("Job 3", 15, ()->{
      System.out.println("Job 3 is executing");
      try{
        Thread.sleep(300);
      }
      catch(InterruptedException e){

      }
    }));
    workerThread.join(2000);

    if(!workerThread.isAlive())
    {
      System.out.println("Worker thread shutdown successfully");
    }
    else
    {
      System.out.println("Worker thread did not shutdown successfully");
    }


    //Checking is all jobs are executed
    System.out.println("\nChecking Queue");
    if(queue.isEmpty())
    {
      System.out.println("All jobs are executed successfully");
    }
    else
    {
      System.out.println("Queue still has " + queue.size() + " jobs to execute");
    }

    System.out.println("\nWorker Class tested successfully");
  }
}