package com.org.marketplace.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.org.marketplace.model.audit.UserDateAudit;

/**
 * @author gauravkahadane
 *
 */
@Entity
@Table(name = "projects")
public class Project extends UserDateAudit {

	private static final long serialVersionUID = -6674017353910255564L;

	public Project() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 20)
	@NotNull
	private String name;

	@Size(max = 400)
	private String description;

	@Min(1)
	private Double budget;

	@NotNull
	@Column(name = "expiration_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bidExpiry;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("value ASC")
	private List<Bid> bids;

	public String getName() {
		return name;
	}

	public Project(Long id) {
		super();
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Date getBidExpiry() {
		return bidExpiry;
	}

	public void setBidExpiry(Date bidExpiry) {
		this.bidExpiry = bidExpiry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description=" + description + ", budget=" + budget
				+ ", bidExpiry=" + bidExpiry + ", bids=" + bids + "]";
	}

}
