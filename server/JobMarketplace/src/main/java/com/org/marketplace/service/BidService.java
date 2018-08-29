package com.org.marketplace.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.marketplace.exception.ResourceNotFoundException;
import com.org.marketplace.model.Bid;
import com.org.marketplace.model.Project;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.BidRequest;
import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.repository.BidRepository;
import com.org.marketplace.repository.ProjectRepository;
import com.org.marketplace.repository.UserRepository;
import com.org.marketplace.security.UserPrincipal;
import com.org.marketplace.util.ModelUtils;

/**
 * @author gauravkahadane
 *
 */
@Service
public class BidService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BidService.class);

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public Bid placeBid(BidRequest bidRequest, UserPrincipal userPrincipal) {
		Bid bid = new Bid();
		try {
			bid.setBid(bidRequest.getBid());
			bid.setProject(new Project(bidRequest.getProjectId()));

			User user = userRepository.getOne(userPrincipal.getId());

			bid.setUser(user);
			bidRepository.save(bid);
		} catch (Exception e) {
			LOGGER.error("Error persisting bid: " + e);
		}
		return bid;
	}

	public PagedResponse<ProjectResponse> getBidsWonBy(String username, UserPrincipal currentUser, int page, int size) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		PagedResponse<ProjectResponse> response = new PagedResponse<ProjectResponse>();

		try {
			StoredProcedureQuery storedProcQuery = session.createNamedStoredProcedureQuery("getLowestBidsByUser");
			storedProcQuery.setParameter("userId", currentUser.getId());
			storedProcQuery.execute();
			@SuppressWarnings("unchecked")
			List<Bid> bidList = storedProcQuery.getResultList();

			User user = userRepository.findByUsername(currentUser.getUsername())
					.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

			List<ProjectResponse> projectsWon = new ArrayList<ProjectResponse>();
			for (Bid bid : bidList) {
				Optional<Project> project = projectRepository.findById(bid.getProject().getId());
				if (project.isPresent()) {
					Project biddedProject = project.get();
					if (biddedProject.getBidExpiry().compareTo(LocalDate.now()) > 0) {
						projectsWon.add(ModelUtils.mapProjectToProjectResponse(bid.getProject(), user));
					}
				}
			}
			
			response.setContent(projectsWon);
		} catch (HibernateException e) {
			LOGGER.error("Failed to fetch bids won by " + currentUser.getUsername() + ": " + e);
		} finally {
			session.close();
		}
		return response;
	}
}
