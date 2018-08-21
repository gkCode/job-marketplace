package com.org.marketplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.marketplace.model.Bid;
import com.org.marketplace.model.Project;
import com.org.marketplace.model.User;
import com.org.marketplace.payload.BidRequest;
import com.org.marketplace.repository.BidRepository;
import com.org.marketplace.repository.UserRepository;
import com.org.marketplace.security.UserPrincipal;

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

}
