package com.org.marketplace.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.org.marketplace.exception.BadRequestException;
import com.org.marketplace.exception.ResourceNotFoundException;
import com.org.marketplace.model.Project;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectRequest;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.repository.BidRepository;
import com.org.marketplace.repository.ProjectRepository;
import com.org.marketplace.repository.UserRepository;
import com.org.marketplace.security.UserPrincipal;
import com.org.marketplace.util.AppConstants;
import com.org.marketplace.util.AppUtils;
import com.org.marketplace.util.ModelUtils;
import com.org.marketplace.util.ValidatorUtils;

/**
 * @author gauravkahadane
 *
 */
@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

	public PagedResponse<ProjectResponse> getAllProjects(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<Project> projects = projectRepository.findAll(pageable);

		if (projects.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(),
					projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
		}

		pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		List<ProjectResponse> projectResponses = projects.map(project -> {
			return ModelUtils.mapProjectToProjectResponse(project);
		}).getContent();

		return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(),
				projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
	}

	public Project createProject(ProjectRequest projectRequest, UserPrincipal userPrincipal) throws Exception {
		if(!ValidatorUtils.validateName(projectRequest.getName())) {
			throw new Exception("Invalid Project Name");
		}
		Project project = new Project();
		project.setName(projectRequest.getName());
		project.setDescription(projectRequest.getDescription());
		project.setBidExpiry(AppUtils.getDate(projectRequest.getBidExpiry()));
		project.setBudget(projectRequest.getBudget());
		Project savedProject = projectRepository.save(project);

		LOGGER.info("Created a project: " + project.toString());
		return savedProject;
	}

	public ProjectResponse getProjectById(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

		return ModelUtils.mapProjectToProjectResponse(project);

	}

	public PagedResponse<ProjectResponse> getBidsPlacedBy(String username, UserPrincipal currentUser, int page,
			int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Long> userbiddedprojectIds = bidRepository.findBiddedProjectIdsByUserId(user.getId(), pageable);

		if (userbiddedprojectIds.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), userbiddedprojectIds.getNumber(),
					userbiddedprojectIds.getSize(), userbiddedprojectIds.getTotalElements(),
					userbiddedprojectIds.getTotalPages(), userbiddedprojectIds.isLast());
		}

		List<Long> projectIds = userbiddedprojectIds.getContent();

		Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
		List<Project> projects = projectRepository.findByIdIn(projectIds, sort);

		Map<Long, User> creatorMap = getProjectCreatorMap(projects);

		List<ProjectResponse> projectResponses = projects.stream().map(project -> {
			return ModelUtils.mapProjectToProjectResponse(project, creatorMap.get(project.getCreatedBy()));
		}).collect(Collectors.toList());

		return new PagedResponse<>(projectResponses, userbiddedprojectIds.getNumber(), userbiddedprojectIds.getSize(),
				userbiddedprojectIds.getTotalElements(), userbiddedprojectIds.getTotalPages(),
				userbiddedprojectIds.isLast());
	}

	private void validatePageNumberAndSize(int page, int size) throws BadRequestException {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	Map<Long, User> getProjectCreatorMap(List<Project> projects) {
		List<Long> creatorIds = projects.stream().map(Project::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return creatorMap;
	}
}
