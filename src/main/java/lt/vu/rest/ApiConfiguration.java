package lt.vu.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApiConfiguration extends Application {
    // No additional code needed: activates JAX-RS on /api/*
}