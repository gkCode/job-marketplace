package com.org.marketplace.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.org.marketplace.security.UserPrincipal;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	@Bean
	public AuditorAware<Long> auditorProvider() {
		return new SpringSecurityAuditAwareImpl();
	}
}

/**
 * @author gauravkahadane
 *
 */
class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityAuditAwareImpl.class);

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		LOGGER.info("Current Auditor: " + userPrincipal.getUsername());

		return Optional.ofNullable(userPrincipal.getId());
	}
}
