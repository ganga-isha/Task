package com.threadtask.ThreadTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
@RestController
@RequestMapping("/api")
public class ThreadController {

    private List<String> myArray = new ArrayList<>();
    @Autowired
    private ApplicationContext context;

    @Value("${number.of.requests}")
    private int numberOfRequests;

    @Value("${number.of.threads}")
    private int numberOfThreads;

    @GetMapping("/threadExecutor")
    public List<String> threadInfo ()
    {
        System.out.println("Spring Boot multithreaded example has started....");
        System.out.println("Number of requests: " + numberOfRequests);
        System.out.println("Number of threads: " + numberOfThreads);
        System.out.println("Number of processors: " + Runtime.getRuntime().availableProcessors());

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<WorkerThread> tasks = new ArrayList<>(numberOfRequests);
        for (int i = 0; i < numberOfRequests; i++) {
            WorkerThread wt = context.getBean(WorkerThread.class, String.valueOf(i));
            tasks.add(wt);
        }

        try {
            List<Future<String>> futures = executorService.invokeAll(tasks);
            for (Future<String> future : futures) {
                String result = future.get(10000, TimeUnit.MILLISECONDS);
                System.out.println("Thread reply results [" + result + "]");
                myArray.add("Thread reply results [" + result + "]");
            }

            executorService.shutdown();

            System.out.println("Spring Boot multithreaded example has ended....");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        return myArray;
    }
}
