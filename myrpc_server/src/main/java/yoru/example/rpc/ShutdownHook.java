package yoru.example.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author YoruG
 * @date 2020/10/24-0:11
 **/
public class ShutdownHook {
    private InetSocketAddress address;
    private ServiceRegistry serviceRegistry;

    public ShutdownHook(ServiceRegistry serviceRegistry,InetSocketAddress address) {
        this.address = address;
        this.serviceRegistry = serviceRegistry;
    }

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    private static volatile ShutdownHook instance;
    public static ShutdownHook getShutdownHookInstance(ServiceRegistry serviceRegistry,InetSocketAddress address){
        if (instance == null){
            synchronized (ShutdownHook.class){
                if (instance == null)
                    instance = new ShutdownHook(serviceRegistry,address);
            }
        }
        return instance;
    }

    public void addClearHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                serviceRegistry.clearLocalServiceFromRegistry(address.getHostName(),address.getPort());
            }
        });
    }
}
