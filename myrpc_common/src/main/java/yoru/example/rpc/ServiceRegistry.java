package yoru.example.rpc;

import java.net.InetSocketAddress;

/**
 * @author YoruG
 * @date 2020/10/24-0:21
 **/
public interface ServiceRegistry {
    void register(String serviceName, InetSocketAddress address);
    InetSocketAddress lookupService(String serviceName);
    void clearLocalServiceFromRegistry(String localhost, int port);
}
