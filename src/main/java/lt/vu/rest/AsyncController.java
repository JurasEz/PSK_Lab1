package lt.vu.rest;

import lt.vu.usecases.AsyncWorker;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.concurrent.Future;

@Path("/async")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@ApplicationScoped
public class AsyncController {
    @EJB
    private AsyncWorker worker;
    private Future<String> lastResult;

    @GET @Path("start")
    public Response start() {
        lastResult = worker.runLongCalculation();
        return Response.accepted("Task started").build();
    }

    @GET @Path("status")
    public Response status() {
        if (lastResult == null)
            return Response.ok("No task started").build();
        if (!lastResult.isDone())
            return Response.ok("Task still running").build();
        try {
            return Response.ok("Result: " + lastResult.get()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching result").build();
        }
    }
}
