package com.org.marketplace.entity;

import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

/**
 * Represents role table in the database
 * 
 * @author gauravkahadane
 *
 */
@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleType name;

	public Role() {

	}

	public Role(RoleType name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleType getName() {
		return name;
	}

	public void setName(RoleType name) {
		this.name = name;
	}

}