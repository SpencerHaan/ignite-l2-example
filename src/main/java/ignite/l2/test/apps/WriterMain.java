package ignite.l2.test.apps;

import ignite.l2.test.IgniteHelper;
import ignite.l2.test.TestEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Supplier;

import static ignite.l2.test.apps.TestConstants.TEST_ENTITY_ID;

public class WriterMain {

    public static void main(String[] args) throws Exception {
        IgniteHelper.start();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ignite.l2.test.jpa");

        // Start application
        WriterApp app = new WriterApp(entityManagerFactory::createEntityManager);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            entityManagerFactory.close();

            IgniteHelper.stop();
        }));
        app.start();
    }

    private static class WriterApp extends AbstractLoopingJpaApplication {

        WriterApp(Supplier<EntityManager> entityManagerSupplier) {
            super(entityManagerSupplier);
        }

        @Override
        protected void act(EntityManager entityManager) {
            TestEntity entity = entityManager.find(TestEntity.class, TEST_ENTITY_ID);
            if (entity == null) {
                entity = new TestEntity(TEST_ENTITY_ID);
                entityManager.persist(entity);
            }
            System.out.println("New count: " + entity.increment());
        }
    }
}
