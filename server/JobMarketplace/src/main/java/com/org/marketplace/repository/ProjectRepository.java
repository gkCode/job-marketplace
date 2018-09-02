package com.org.marketplace.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.org.marketplace.entity.Project;

/**
 * JPA repository for projects table
 * 
 * @author gauravkahadane
 *
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

	List<Project> findByIdIn(List<Long> projectIds);

	List<Project> findByIdIn(List<Long> projectIds, Sort sort);

}
