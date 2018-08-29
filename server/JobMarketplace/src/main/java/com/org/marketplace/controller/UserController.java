package com.org.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.marketplace.exception.ResourceNotFoundException;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.payload.UserIdentityAvailability;
import com.org.marketplace.payload.UserProfile;
import com.org.marketplace.payload.UserSummary;
import com.org.marketplace.repository.UserRepository;
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
@RequestMapping("/mkt")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private BidService bidService;
	

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/users/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
				user.getCreatedAt());

		return userProfile;
	}

	@GetMapping("/users/{username}/bids")
	public PagedResponse<ProjectResponse> getBidsPlacedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return projectService.getBidsPlacedBy(username, currentUser, page, size);
	}

	@GetMapping("/users/{username}/bidsWon")
	public PagedResponse<ProjectResponse> getBidsWonBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return bidService.getBidsWonBy(username, currentUser, page, size);
	}
}
