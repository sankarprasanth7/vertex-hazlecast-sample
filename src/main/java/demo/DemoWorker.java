package demo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.util.Random;

public class DemoWorker extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();
        eb.consumer("worker.count", mes -> {
            System.out.println("[Worker|" + mes.body()+ "]: Received new request" );
        });
    }
}
