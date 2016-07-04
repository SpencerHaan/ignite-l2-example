package ignite.l2.test.apps;

import ignite.l2.test.IgniteHelper;
import ignite.l2.test.TestEntity;
import org.apache.derby.drda.NetworkServerControl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.function.Supplier;

import static ignite.l2.test.apps.TestConstants.TEST_ENTITY_ID;

public class ReaderMain {

    public static void main(String[] args) throws Exception {
        IgniteHelper.start();
        NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
        server.start(new PrintWriter(System.out));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ignite.l2.test.jpa");

        // Start application
        ReaderApp app = new ReaderApp(entityManagerFactory::createEntityManager);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            entityManagerFactory.close();
            try {
                server.shutdown();
            } catch (Exception e) {
                System.err.println("Failed to shut down Derby server");
                e.printStackTrace(System.err);
            }
            IgniteHelper.stop();
        }));
        app.start();
    }

    private static class ReaderApp extends AbstractLoopingJpaApplication {

        ReaderApp(Supplier<EntityManager> entityManagerSupplier) {
            super(entityManagerSupplier);
        }

        @Override
        protected void act(EntityManager entityManager) {
            TestEntity entity = entityManager.find(TestEntity.class, TEST_ENTITY_ID);
            System.out.println(entity);
        }
    }
 }
