package yoru.example.rpc;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcException;
import yoru.example.loadbanlance.LoadBalancer;
import yoru.example.loadbanlance.RandomLoadBalance;
import yoru.example.loadbanlance.RoundRobinLoadBalancer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author YoruG
 * @date 2020/10/23-16:00
 **/
public class NacosServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);
    private static NamingService namingService;
    private static Set<String> serviceNames;
    private static LoadBalancer loadBalancer = new RandomLoadBalance();
    /**
     * 注册中心构造器
     * @param registryHost  注册中心IP地址
     * @param registryPort  注册中心端口号
     */
    public NacosServiceRegistry(String registryHost, int registryPort) {
        String address = registryHost + ":" + registryPort;
        try {
            serviceNames = new HashSet<>();
            namingService = NamingFactory.createNamingService(address);
        } catch (NacosException e) {
            logger.error("注册中心创建出现错误",e);
        }
    }


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName,inetSocketAddress.getHostName(),inetSocketAddress.getPort());
            serviceNames.add(serviceName);
        } catch (NacosException e) {
            logger.error("服务注册出现错误",e);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时出现错误",e);
        }
        return null;
    }


    /**
     * 注销服务，在serviceName集合中存放有所有已经存在服务名，而从注册中心取消注册服务需要获得
     * 服务名还有IP地址、端口，因为已经给了IP和端口只需要把与之相关的服务全都注销掉即可
     * @param localhost 注销主机IP
     * @param port  注销主机端口
     */
    @Override
    public void clearLocalServiceFromRegistry(String localhost, int port) {
        Iterator<String> iterator = serviceNames.iterator();
        while(iterator.hasNext()){
            String serviceName = iterator.next();
            try {
                namingService.deregisterInstance(serviceName,localhost,port);
            } catch (NacosException e) {
                logger.error("注销服务时出现错误",e);
            }
        }
    }
}
