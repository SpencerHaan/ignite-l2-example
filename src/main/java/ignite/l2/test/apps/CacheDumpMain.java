package ignite.l2.test.apps;

import ignite.l2.test.IgniteHelper;
import org.apache.ignite.internal.IgniteKernal;
import org.apache.ignite.internal.processors.cache.IgniteInternalCache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheDumpMain {

    public static void main(String[] args) {
        IgniteHelper.start();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            IgniteHelper.stop();
        }));
        executor.scheduleAtFixedRate(() -> {
            IgniteInternalCache<Object, Object> cache = ((IgniteKernal) IgniteHelper.getIgnite()).getCache("default");

            System.out.println("Cache dump start");
            cache.entrySet().forEach(System.out::println);
            System.out.println("Cache dump end");
            System.out.println();
        }, 0, 1, TimeUnit.SECONDS);
    }
}
