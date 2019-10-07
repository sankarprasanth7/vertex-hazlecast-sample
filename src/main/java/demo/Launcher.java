package demo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Launcher {
	public static EventBus clusterEB = null;

	public static void main(String[] args) {
		Config hazelcastConfig = new Config();
		hazelcastConfig.getGroupConfig().setName("staging").setPassword("prasanth");
		hazelcastConfig.getNetworkConfig().setPort(5701).setPortAutoIncrement(true);
		TcpIpConfig tcpIpConfig = new TcpIpConfig();
		tcpIpConfig.addMember("195.201.90.61");
		tcpIpConfig.setEnabled(true);
		hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		hazelcastConfig.getNetworkConfig().getJoin().setTcpIpConfig(tcpIpConfig);
		EventBusOptions eventBusOptions = new EventBusOptions();
		eventBusOptions.setClusterPublicHost("195.201.90.61");
		ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
		VertxOptions options = new VertxOptions().setClusterManager(mgr).setEventBusOptions(eventBusOptions);
		Vertx.clusteredVertx(options, res -> {
			if (res.succeeded()) {
				Vertx vertx = res.result();
				clusterEB = vertx.eventBus();
				vertx.deployVerticle(new DemoVerticle());
			}
		});
	}
}
