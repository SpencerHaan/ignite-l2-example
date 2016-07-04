package ignite.l2.test.apps;

import javax.persistence.EntityManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractLoopingJpaApplication {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Supplier<EntityManager> entityManagerSupplier;

    AbstractLoopingJpaApplication(Supplier<EntityManager> entityManagerSupplier) {
        this.entityManagerSupplier = entityManagerSupplier;
    }

    void start() {
        executor.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.SECONDS);
    }

    void stop() {
        executor.shutdown();
    }

    protected abstract void act(EntityManager entityManager);

    private void run() {
        EntityManager entityManager = entityManagerSupplier.get();
        try {
            entityManager.getTransaction().begin();
            act(entityManager);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            entityManager.getTransaction().rollback();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().commit();
            }
            entityManager.close();
        }
    }
}
