package com.netflix.conductor.client.demo.config;

import java.util.Collections;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.exceptions.ConductorClientException;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;


@Component
public class ConductorMetadataRunner implements ApplicationRunner {


	private final MetadataClient metadataClient;
	private final ObjectMapper objectMapper;
	private final Resource[] taskResources;
	private final Resource[] workflowResources;

	public ConductorMetadataRunner(
			final MetadataClient metadataClient,
			final ObjectMapper objectMapper,
			@Value("classpath:defs/tasks/*.json") final Resource[] taskResources,
			@Value("classpath:defs/workflows/*.json") final Resource[] workflowResources) {

		this.metadataClient = metadataClient;
		this.objectMapper = objectMapper;
		this.taskResources = taskResources;
		this.workflowResources = workflowResources;
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {

		for (final Resource taskResource : this.taskResources) {
			final JsonNode rootNode = this.objectMapper.readTree(taskResource.getInputStream());
			if (rootNode == null || !rootNode.isContainerNode()) {
				throw new IllegalArgumentException("Expected JSON array or object");
			}

			if (rootNode.isArray()) {
				final Iterator<JsonNode> iterator = rootNode.elements();
				while (iterator.hasNext()) {
					final JsonNode elementNode = iterator.next();
					final TaskDef taskDef = this.objectMapper.convertValue(elementNode, TaskDef.class);
					this.upsertTaskDef(taskDef);
				}
			} else if (rootNode.isObject()) {
				final TaskDef taskDef =
						this.objectMapper.readValue(taskResource.getInputStream(), TaskDef.class);
				this.upsertTaskDef(taskDef);
			}
		}

		for (final Resource workflowResource : this.workflowResources) {
			final WorkflowDef workflowDef =
					this.objectMapper.readValue(workflowResource.getInputStream(), WorkflowDef.class);
			this.upsertWorkflowDef(workflowDef);
		}
	}

	private void upsertTaskDef(final TaskDef taskDef) {
		try {
			this.metadataClient.updateTaskDef(taskDef);
		} catch (final ConductorClientException e) {
			this.metadataClient.registerTaskDefs(Collections.singletonList(taskDef));
		}
	}

	private void upsertWorkflowDef(final WorkflowDef workflowDef) {
		try {
			this.metadataClient.updateWorkflowDefs(Collections.singletonList(workflowDef));
		} catch (final ConductorClientException e) {
			this.metadataClient.registerWorkflowDef(workflowDef);
		}
	}
}

