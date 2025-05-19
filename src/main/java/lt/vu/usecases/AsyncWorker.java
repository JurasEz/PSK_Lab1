package lt.vu.usecases;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

@Stateless
public class AsyncWorker {

    @PersistenceContext(unitName = "StudentPersistenceUnit")
    private EntityManager em;

    @Asynchronous
    public Future<String> runLongCalculation() {
        try {
            Thread.sleep(10000); // simulate heavy work/long calculations

            long count = em.createQuery("SELECT COUNT(s) FROM Student s", Long.class)
                    .getSingleResult();

            return CompletableFuture.completedFuture("Calculation done. Found: " + count + " students in the database.");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Calculation interrupted");
        }
    }
}

// 1. No. By default, @Asynchronous EJB methods run in a new transaction (REQUIRES_NEW)
// 2. No. A @RequestScoped EM only lives for the duration of the original HTTP request.
//    The asynchronous work runs after that request completes, so it must use a container-managed EM in a stateless/EJB bean.