package com.org.marketplace.service;

import java.time.LocalDate;
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

import com.org.marketplace.entity.Project;
import com.org.marketplace.entity.User;
import com.org.marketplace.exception.BadRequestException;
import com.org.marketplace.exception.ResourceNotFoundException;
import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectRequest;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.repository.BidRepository;
import com.org.marketplace.repository.ProjectRepository;
import com.org.marketplace.repository.UserRepository;
import com.org.marketplace.security.UserPrincipal;
import com.org.marketplace.util.AppUtils;
import com.org.marketplace.util.ModelUtils;
import com.org.marketplace.util.ValidatorUtils;

/**
 * Service for managing projects
 * 
 * @author gauravkahadane
 *
 */
@Service
public class ProjectService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Retrieves all the projects
	 * 
	 * @param page index of a page
	 * @param size size of a page
	 * @return paged response of projects
	 */
	public PagedResponse<ProjectResponse> getAllProjects(int page, int size) {
		List<ProjectResponse> projectResponses;
		Page<Project> projects;

		try {
			ValidatorUtils.validatePageNumberAndSize(page, size);

			Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

			projects = projectRepository.findAll(pageable);

			if (projects.getNumberOfElements() == 0) {
				return new PagedResponse<>(Collections.emptyList(), projects.getNumber(), projects.getSize(),
						projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
			}

			pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

			projectResponses = projects.map(project -> {
				return ModelUtils.mapProjectToProjectResponse(project);
			}).getContent();

		} catch (BadRequestException e) {
			LOGGER.error("Bad Request: " + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Failed to fetch projects: " + e);
			throw e;
		}
		return new PagedResponse<>(projectResponses, projects.getNumber(), projects.getSize(),
				// Get All Users and create UserSummary //TODO : may make use of HATEOS
				projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
	}

	/**
	 * Creates a project
	 * 
	 * @param projectRequest contains project details
	 * @param userPrincipal  authenticated user
	 * @return the created project
	 * @throws Exception
	 */
	public Project createProject(ProjectRequest projectRequest, UserPrincipal userPrincipal) throws Exception {
		try {
			if (!ValidatorUtils.validateName(projectRequest.getName())) {
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
		} catch (Exception e) {
			LOGGER.error("Failed to create project: " + e);
			throw e;
		}
	}

	/**
	 * Retrieves a project by the given id
	 * 
	 * @param projectId id of project
	 * @return project response
	 */
	public ProjectResponse getProjectById(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

		try {
			return ModelUtils.mapProjectToProjectResponse(project);
		} catch (Exception e) {
			LOGGER.error("Failed to fetch project: " + e);
			throw e;
		}

	}

	/**
	 * Retrieves the projects/bids placed by a user
	 * 
	 * @param username
	 * @param currentUser authenticated user
	 * @param page        index of a page
	 * @param size        size of a page
	 * @return projects on which user has placed the bid
	 */
	public PagedResponse<ProjectResponse> getBidsPlacedBy(String username, UserPrincipal currentUser, int page,
			int size) {
		try {
			ValidatorUtils.validatePageNumberAndSize(page, size);

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

			return new PagedResponse<>(projectResponses, userbiddedprojectIds.getNumber(),
					userbiddedprojectIds.getSize(), userbiddedprojectIds.getTotalElements(),
					userbiddedprojectIds.getTotalPages(), userbiddedprojectIds.isLast());

		} catch (BadRequestException e) {
			LOGGER.error("Bad Request: " + e);
			throw e;
		} catch (ResourceNotFoundException e) {
			LOGGER.error("Resource Not Found: " + e);
			throw e;
		}
	}

	/**
	 * Retrieves projects/bids won by a user
	 * 
	 * @param username
	 * @param currentUser authenticated user
	 * @param page        index of a page
	 * @param size        size of a page
	 * @return projects won by a user
	 */
	public PagedResponse<ProjectResponse> getBidsWonBy(String username, UserPrincipal currentUser, int page, int size) {
		try {
			ValidatorUtils.validatePageNumberAndSize(page, size);

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

			LOGGER.info("User bidded projects: " + projectIds.size());

			Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
			List<Project> projects = projectRepository.findByIdIn(projectIds, sort);

			projects = projects.stream().filter(project -> project.getBidExpiry().isBefore(LocalDate.now()))
					.collect(Collectors.toList());

			LOGGER.info("Filtered sold projects, size:" + projects.size());

			if (projects.size() == 0) {
				return new PagedResponse<>(Collections.emptyList(), userbiddedprojectIds.getNumber(),
						userbiddedprojectIds.getSize(), userbiddedprojectIds.getTotalElements(),
						userbiddedprojectIds.getTotalPages(), userbiddedprojectIds.isLast());
			}

			Map<Long, User> creatorMap = getProjectCreatorMap(projects);

			List<ProjectResponse> projectResponses = projects.stream().map(project -> {
				return ModelUtils.mapProjectToProjectResponse(project, creatorMap.get(project.getCreatedBy()));
			}).collect(Collectors.toList());

			return new PagedResponse<>(projectResponses, userbiddedprojectIds.getNumber(),
					userbiddedprojectIds.getSize(), userbiddedprojectIds.getTotalElements(),
					userbiddedprojectIds.getTotalPages(), userbiddedprojectIds.isLast());

		} catch (BadRequestException e) {
			LOGGER.error("Bad Request: " + e);
			throw e;
		} catch (ResourceNotFoundException e) {
			LOGGER.error("Resource Not Found: " + e);
			throw e;
		}
	}

	/**
	 * Maps a project to its creator user
	 * 
	 * @param projects list of projects
	 * @return project to creator user map
	 */
	private Map<Long, User> getProjectCreatorMap(List<Project> projects) {
		List<Long> creatorIds = projects.stream().map(Project::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return creatorMap;
	}
}
