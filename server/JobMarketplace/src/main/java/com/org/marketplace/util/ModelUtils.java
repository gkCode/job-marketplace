package com.org.marketplace.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.marketplace.model.Project;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.payload.UserSummary;

/**
 * Model conversion utility
 * 
 * @author gauravkahadane
 *
 */
public final class ModelUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelUtils.class);

	/**
	 * Maps project model to a project response to be sent to the client
	 * 
	 * @param project model
	 * @param creator authenticated user
	 * @return project response
	 */
	public static ProjectResponse mapProjectToProjectResponse(Project project, User creator) {
		ProjectResponse projectResponse = new ProjectResponse();

		try {
			if (project == null) {
				throw new Exception("Project is null");
			}

			projectResponse.setId(project.getId());
			projectResponse.setName(project.getName());
			projectResponse.setDescription(project.getDescription());
			projectResponse.setBudget(project.getBudget());

			if (project.getBids() != null && project.getBids().size() > 0) {
				projectResponse.setBid(project.getBids().get(0).getBid());
			}

			if (creator != null) {
				UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
				projectResponse.setCreatedBy(creatorSummary);
			}

			projectResponse.setBidExpiry(project.getBidExpiry());
			return projectResponse;
		} catch (Exception e) {
			LOGGER.error("Failed to generate project response: " + e);
		}
		return projectResponse;
	}

	public static ProjectResponse mapProjectToProjectResponse(Project project) {
		return mapProjectToProjectResponse(project, null);
	}

}