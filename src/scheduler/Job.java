package scheduler;

import java.util.concurrent.atomic.AtomicLong;

public class Job implements Comparable<Job> , Runnable
{
    private static final AtomicLong idGenerator = new AtomicLong(0);

    private final long id;
    private final String name;
    private final int priority;
    private final Runnable task;

    public Job(String name, int priority, Runnable task)
    {
        this.id = idGenerator.incrementAndGet();
        this.name=name;
        this.priority=priority;
        this.task=task;
    }

    @Override
    public void run()
    {
        System.out.println(String.format(
            "[Job %d] '%s' started on thread: %s",
            id, name, Thread.currentThread().getName()
        ));
        
        try
        {
            task.run();
        }
        catch(Exception e)
        {
            System.err.println(String.format(
                "[Job %d] '%s' has an error: %s",
                id, name, e.getMessage()
            ));

            e.printStackTrace();
        }

        System.out.println(String.format( 
            "[Job %d] '%s' completed",
            id, name
        ));
    }

    public int compareTo(Job other)
    {
        return Integer.compare(other.priority, this.priority);
    }

    public long getId()
    {
        return id;
    }

    public int getPriority()
    {
        return priority;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return String.format(
        "Job{id=%d, name='%s', priority=%d}",
        id,name,priority
     );
    }
}