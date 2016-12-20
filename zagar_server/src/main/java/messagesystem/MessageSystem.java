package messagesystem;

import main.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author e.shubin
 */
public final class MessageSystem {

    private static final Logger LOG = LogManager.getLogger(MessageSystem.class);

    private final Map<Address, Queue<Message>> messages = new HashMap<>();
    private final Map<Class<?>, Service> services = new ConcurrentHashMap<>();

    public void registerService(Class<?> type, Service service) {
        services.put(type, service);
        messages.putIfAbsent(service.getAddress(), new LinkedBlockingQueue<>());
        LOG.info(service + " registered");
    }

    public <T> T getService(Class<T> type) {
        return (T) services.get(type);
    }

    public Collection<Service> getServices() {
        return services.values();
    }

    public void sendMessage(Message message) {
        messages.get(message.getTo()).add(message);
    }

    public void execForService(Service service) {
        Queue<Message> queue = messages.get(service.getAddress());
        while (!queue.isEmpty()) {
            Message message = queue.poll();
            message.exec(service);
        }
    }

    public void execOneForService(Service service, long timeout) throws InterruptedException {
        execOneForService(service, timeout, TimeUnit.MILLISECONDS);
    }

    private void execOneForService(Service service, long timeout, TimeUnit unit) throws InterruptedException {
        BlockingQueue<Message> queue = (BlockingQueue<Message>) messages.get(service.getAddress());
        final Message message = queue.poll(timeout, unit);
        if (message == null) {
            return;
        }
        message.exec(service);
    }

}
