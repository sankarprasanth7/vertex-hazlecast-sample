package demo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class LauncherClient {
	public static EventBus clusterEB = null;
	public static void main(String args[]) {
		ClientConfig config = new ClientConfig();
		String[] addresses = { "195.201.90.61" + ":" + "5701" };
		config.getNetworkConfig().setSmartRouting(true).addAddress(addresses);

		// start Hazelcast client
		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);
		ClusterManager mgr = new HazelcastClusterManager(hazelcastInstance);
		VertxOptions options = new VertxOptions().setClusterManager(mgr);
		Vertx.clusteredVertx(options, res -> {
			if (res.succeeded()) {
				Vertx vertx = res.result();
				clusterEB = vertx.eventBus();
				System.out.println("connected to  hazlecast cluster");
				clusterEB.send("worker.count", 10);
			} else {
				System.out.println("failed");
			}
		});
	}
}
