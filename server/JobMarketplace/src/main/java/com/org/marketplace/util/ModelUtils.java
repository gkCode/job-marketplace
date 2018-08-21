package com.org.marketplace.util;

import com.org.marketplace.model.Project;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.payload.UserSummary;

/**
 * @author gauravkahadane
 *
 */
public class ModelUtils {

	public static ProjectResponse mapProjectToProjectResponse(Project project, User creator) {
		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setId(project.getId());
		projectResponse.setName(project.getName());
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
	}

	public static ProjectResponse mapProjectToProjectResponse(Project project) {
		return mapProjectToProjectResponse(project, null);
	}

}