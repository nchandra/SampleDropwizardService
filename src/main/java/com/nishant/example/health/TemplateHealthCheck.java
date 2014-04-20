package com.nishant.example.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.health.HealthCheck;
import com.nishant.example.HelloWorldConfiguration;

@Service
public class TemplateHealthCheck extends HealthCheck {

	@Autowired
	private HelloWorldConfiguration configuration;

	@Override
	protected Result check() throws Exception {
		final String saying = String.format(configuration.getTemplate(), "TEST");
		if (!saying.contains("TEST")) {
			return Result.unhealthy("template doesn't include a name");
		}
		return Result.healthy();
	}
}
