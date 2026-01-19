# Concurrent Job Scheduler (Java)

A simple **Java-based Concurrent Job Scheduler** that executes multiple jobs in parallel using worker threads, priority-based scheduling, and safe concurrency mechanisms.

This project is built to understand how real schedulers work internally and demonstrates core Java multithreading concepts.

---

## 📌 Project Overview

Many real-world systems (servers, background workers, batch processors) require executing multiple tasks concurrently.

This project implements:
- A custom job scheduler
- A worker thread pool
- Priority-based job execution
- Safe concurrency using Java’s `java.util.concurrent` utilities

---

## 🧠 Why this project?

Instead of only using high-level abstractions like `ExecutorService`, this project focuses on **how schedulers work internally**.

Learning:
- Scheduler design
- Worker thread behavior
- Priority queues
- Graceful shutdown handling
- Producer–Consumer pattern


---

## ⚙️ Features

- Concurrent job execution using multiple worker threads
- Priority-based job scheduling
- Thread-safe job queue (`PriorityBlockingQueue`)
- Graceful scheduler shutdown
- Atomic job ID generation
- Clean separation of Scheduler, Worker, and Job

---

## 🧩 Core Components

### Job
- Represents a unit of work
- Implements `Runnable`
- Implements `Comparable<Job>` for priority-based execution
- Contains job metadata such as ID and priority

### Scheduler
- Accepts jobs from the user
- Manages the job queue
- Starts and stops worker threads
- Controls scheduler lifecycle

### Worker
- Runs in its own thread
- Continuously fetches jobs from the queue
- Executes jobs safely
- Stops gracefully during shutdown

---

###  Getting Started

### Prerequisites
- Java 11 or higher
- Git
- Any Java IDE (VS Code / IntelliJ)

---

### Compile the Project
javac -d bin src/**/*.java
(or build using your IDE)

### Run
java -cp bin demo.SchedulerDemo

### Demo Scenarios

The demo class demonstrates:
Basic job execution
Priority-based scheduling
Concurrent execution
Graceful shutdown


### Thread Safety

Job IDs generated using AtomicLong

Scheduler state managed using AtomicBoolean

Thread-safe queue (PriorityBlockingQueue)

Safe worker shutdown without abrupt interruption

### Future Improvements
Retry mechanism with backoff
Delayed and periodic jobs
Job status tracking
Metrics and monitoring
Job persistence

👤 Author
Swathii Ramesh
GitHub: https://github.com/swathiramesh24

### Clone the Repository

```bash
git clone https://github.com/swathiramesh24/Concurrent_Job_Scheduler.git
cd Concurrent_Job_Scheduler

---
---

