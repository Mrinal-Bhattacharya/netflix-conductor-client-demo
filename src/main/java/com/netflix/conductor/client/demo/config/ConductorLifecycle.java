package com.netflix.conductor.client.demo.config;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.client.task.WorkflowTaskCoordinator;

@Configuration
public class ConductorLifecycle implements SmartLifecycle {

	AtomicBoolean isRunning = new AtomicBoolean(false);
    WorkflowTaskCoordinator taskCoordinator;
    public ConductorLifecycle(WorkflowTaskCoordinator workflowTaskCoordinator) {
        this.taskCoordinator = workflowTaskCoordinator;
    }

    @Override
    public void start() {
    	System.out.println("Starting taskCoord");
        taskCoordinator.init();;
    }

    @Override
    public void stop() {
    taskCoordinator.shutdown();
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }
}
