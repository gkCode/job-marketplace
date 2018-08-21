package com.org.marketplace.payload;

import java.time.Instant;
import java.util.Date;

/**
 * @author gauravkahadane
 *
 */
public class ProjectResponse {
	private Long id;
	private String name;
	private UserSummary createdBy;
	private Instant creationDateTime;
	private Instant expirationDateTime;
	private Boolean isExpired;
	private Double budget;
	private Date bidExpiry;
	private Double bid;

	public ProjectResponse() {
		super();
		this.bid = (double) 0;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Instant creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Instant getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Instant expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public Boolean getExpired() {
		return isExpired;
	}

	public void setExpired(Boolean expired) {
		isExpired = expired;
	}

	public Date getBidExpiry() {
		return bidExpiry;
	}

	public void setBidExpiry(Date bidExpiry) {
		this.bidExpiry = bidExpiry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

}
