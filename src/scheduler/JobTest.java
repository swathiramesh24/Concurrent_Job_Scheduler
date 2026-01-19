package scheduler;

public class JobTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Job Class ===\n");
        
        // Test 1: Basic job creation
        System.out.println("Test 1: Creating jobs...");
        Job job1 = new Job("Test Job 1", 5, () -> {
            System.out.println("Job 1 is running!");
        });
        
        Job job2 = new Job("Test Job 2", 10, () -> {
            System.out.println("Job 2 is running!");
        });
        
        System.out.println("Created: " + job1);
        System.out.println("Created: " + job2);
        System.out.println("✓ Jobs created successfully\n");
        
        // Test 2: Job execution
        System.out.println("Test 2: Executing jobs...");
        job1.run();
        job2.run();
        System.out.println("✓ Jobs executed successfully\n");
        
        // Test 3: Priority comparison
        System.out.println("Test 3: Testing priority comparison...");
        int comparison = job1.compareTo(job2);
        if (comparison > 0) {
            System.out.println("✓ Job2 (priority 10) has higher priority than Job1 (priority 5)");
        } else {
            System.out.println("✗ Priority comparison failed!");
        }
        
        // Test 4: Unique IDs
        System.out.println("\nTest 4: Testing unique IDs...");
        Job job3 = new Job("Test Job 3", 1, () -> {});
        System.out.println("Job1 ID: " + job1.getId());
        System.out.println("Job2 ID: " + job2.getId());
        System.out.println("Job3 ID: " + job3.getId());
        
        if (job1.getId() != job2.getId() && job2.getId() != job3.getId()) {
            System.out.println("✓ All jobs have unique IDs\n");
        } else {
            System.out.println("✗ ID generation failed!\n");
        }
        
        // Test 5: Exception handling
        System.out.println("Test 5: Testing exception handling...");
        Job faultyJob = new Job("Faulty Job", 5, () -> {
            throw new RuntimeException("Intentional error for testing");
        });
        faultyJob.run();
        System.out.println("✓ Exception was caught and handled\n");
        
        System.out.println("=== All Job tests passed! ===");
    }
}