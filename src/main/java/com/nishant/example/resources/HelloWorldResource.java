package com.nishant.example.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.nishant.example.HelloWorldConfiguration;
import com.nishant.example.core.Saying;

@Service
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

	@Autowired
	private HelloWorldConfiguration configuration;

    private final AtomicLong counter = new AtomicLong();

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {

        final String value = String.format(configuration.getTemplate(), name.or(configuration.getDefaultName()));
        return new Saying(counter.incrementAndGet(), value);
    }
}
