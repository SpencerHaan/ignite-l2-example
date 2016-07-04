package ignite.l2.test;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.sharedfs.TcpDiscoverySharedFsIpFinder;

public class IgniteHelper {

    private static Ignite ignite;

    public static Ignite getIgnite() {
        return ignite;
    }

    public static void start() {
        TcpDiscoverySharedFsIpFinder ipFinder = new TcpDiscoverySharedFsIpFinder();
        ipFinder.setPath("ignite-shared");

        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        spi.setIpFinder(ipFinder);

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration()
                .setGridName("hibernate-grid")
                .setDiscoverySpi(spi);

        ignite = Ignition.getOrStart(igniteConfiguration);

        CacheConfiguration cacheConfiguration = new CacheConfiguration()
                .setName("default")
                .setCacheMode(CacheMode.PARTITIONED)
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        ignite.getOrCreateCache(cacheConfiguration);
    }

    public static void stop() {
        Ignition.stop("hibernate-grid", true);
    }
}
