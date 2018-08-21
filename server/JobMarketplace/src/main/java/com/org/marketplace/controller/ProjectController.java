package com.org.marketplace.controller;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.org.marketplace.model.Bid;
import com.org.marketplace.model.Project;
import com.org.marketplace.payload.ApiResponse;
import com.org.marketplace.payload.BidRequest;
import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectRequest;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.security.CurrentUser;
import com.org.marketplace.security.UserPrincipal;
import com.org.marketplace.service.BidService;
import com.org.marketplace.service.ProjectService;
import com.org.marketplace.util.AppConstants;
/**
 * @author gauravkahadane
 *
 */
@RestController
@RequestMapping("/mkt/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BidService bidService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

	@GetMapping
	public PagedResponse<ProjectResponse> getProjects(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return projectService.getAllProjects(currentUser, page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest,
			@CurrentUser UserPrincipal currentUser) {
		Project project = projectService.createProject(projectRequest, currentUser);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{projectId}")
				.buildAndExpand(project.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Project Created Successfully"));
	}

	@PostMapping("/placeBid")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> placeBid(@Valid @RequestBody BidRequest bidRequest,
			@CurrentUser UserPrincipal currentUser) {
		Bid project = bidService.placeBid(bidRequest, currentUser);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{bidValue}")
				.buildAndExpand(project.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Bid Placed Successfully"));
	}

	@GetMapping("/{projectId}")
	public ProjectResponse getprojectById(@PathVariable Long projectId) {
		return projectService.getProjectById(projectId);
	}

}
