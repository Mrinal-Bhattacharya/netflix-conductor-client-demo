package com.netflix.conductor.client.demo.workers;

import org.springframework.stereotype.Component;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

@Component
public class DotNetProcess1 implements Worker {
    @Override
    public String getTaskDefName() {
        return "dotnetprocess1";
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult taskResult = new TaskResult(task);
        System.out.println("dotnetprocess1");
        System.out.println(task.getInputData());
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        return taskResult;
    }
}
