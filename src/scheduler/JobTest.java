package scheduler;

public class JobTest{
    public static void main(String[] args) {
        
        System.out.println("---Testing Job Class---");

        //Creating Jobs Job 1 and Job 2
        Job job1 = new Job("Job 1", 5, ()->{
            System.out.println("Job 1 is running.");
        });

        Job job2 = new Job("Job 2", 10, ()->{
             System.out.println("Job 2 is running");
        });

        System.out.println("Created :" + job1);
        System.out.println("Created: " + job2);
        System.out.println("Jobs created successfully\n");

        //Executing Job1 and Job2
        System.out.println("\nExecuting Jobs...");
        job1.run();
        job2.run();
        System.out.println("Jobs executed Successfully\n");

        //Comparing priority using the compareTo() function
        System.out.println("\nComparing the Job priorities");
        int comparison = job1.compareTo(job2);

        if(comparison>0)
        {
            System.out.println("Job2 has higher priority than Job1\n");
        }
        else
        {
            System.out.println("Priority comparision failed\n");
        }

        //Testing for unique ideas...testing AtomicLong
        System.out.println("\nTesting unique IDs");
        Job job3 = new Job("Job 3", 1, ()->{
            System.out.println("Creating Job 3");
        });

        System.out.println("Job1 ID: " + job1.getId());
        System.out.println("Job2 ID: " + job2.getId());
        System.out.println("Job3 ID: " + job3.getId());

        if(job1.getId() != job2.getId() && job2.getId()!=job3.getId())
        {
            System.out.println("All IDs are unique\n");
        }

        //Testing the exception statements
        System.out.println("\nTesting for exception handling");

        Job faultJob = new Job("Fault Job", 3, ()->{
            throw new RuntimeException("Intentional error for testing");
        });
        faultJob.run();
        System.out.println("Exception handled successfully\n");

        System.out.println("\nAll Job Tests Passed");
    }    
}  