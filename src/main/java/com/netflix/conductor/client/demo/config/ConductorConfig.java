package com.netflix.conductor.client.demo.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.task.WorkflowTaskCoordinator;
import com.netflix.conductor.client.worker.Worker;

@Configuration
public class ConductorConfig {

	@Value("${conductor.server.url}")
	String conductorUrl;

	@Bean
	public TaskClient taskClient() {
		final TaskClient taskClient = new TaskClient();
		System.out.println("Conductor server url " + this.conductorUrl);
		taskClient.setRootURI(this.conductorUrl);
		return taskClient;
	}

	@Bean
	public MetadataClient metadataClient() {
		final MetadataClient metadataClient = new MetadataClient();
		metadataClient.setRootURI(this.conductorUrl);
		return metadataClient;
	}

	@Bean
	public WorkflowTaskCoordinator workflowTaskCoordinator(final Collection<Worker> workers) {
		final WorkflowTaskCoordinator workflowTaskCoordinator = new WorkflowTaskCoordinator.Builder()
				.withWorkers(workers).withThreadCount(3).withTaskClient(this.taskClient()).build();
		return workflowTaskCoordinator;
	}

}
