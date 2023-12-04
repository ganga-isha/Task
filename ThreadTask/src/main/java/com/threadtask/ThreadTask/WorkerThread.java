package com.threadtask.ThreadTask;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Callable;


@Component
@Scope("prototype")
public class WorkerThread implements Callable<String> {


    private String request;

    public WorkerThread(String request) {
        this.request = request;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Thread started [" + request + "]");
        return doWork();
    }

    private String doWork() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("An unexpected interrupt exception occurred!");
        }

        return "Request [" + request + "] " + createUUID();
    }

    private String createUUID() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

}
