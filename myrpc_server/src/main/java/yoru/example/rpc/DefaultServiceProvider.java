package yoru.example.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author YoruG
 * @date 2020/10/23-9:17
 **/
public class DefaultServiceProvider implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceProvider.class);
    //由于设置成静态的可以通用
    private static final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredServiceSet = new HashSet<>();

    /**
     * 获取服务全限定类名以及其实现的所有接口名，以接口-服务的键值对形式添加
     * 到注册表上，另外用set来防止服务重复注册
     * @param service   服务提供者
     * @param serviceName    服务名
     * @param <T>
     */
    @Override
    public synchronized <T> void addService(T service, String serviceName) {
        if (registeredServiceSet.contains(serviceName))
            return;
        registeredServiceSet.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(),service);
        }
        logger.info("向接口{}注册新服务{}",interfaces,serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null){
            logger.error("从注册表中获取服务失败");
        }
        return service;
    }
}
