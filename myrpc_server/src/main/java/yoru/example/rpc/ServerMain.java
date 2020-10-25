package yoru.example.rpc;

import yoru.example.enums.SerializerType;
import yoru.example.serializer.CommonSerializer;
import yoru.example.service.HelloService;
import yoru.example.service.impl.HelloServiceImpl;

/**
 * @author YoruG
 * @date 2020/10/22-23:24
 **/
public class ServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        //开启服务器，配置自身IP、端口、序列化器，连接到注册中心，将Hello实现注册
        NettyServer nettyServer = new NettyServer("127.0.0.1",7888);
//        nettyServer.setSerializer(CommonSerializer.getByCode(SerializerType.HESSIAN_SERIALIZER.getCode()));
//        nettyServer.setSerializer(CommonSerializer.getByCode(SerializerType.JSON_SERIALIZER.getCode()));
        nettyServer.setSerializer(CommonSerializer.getByCode(SerializerType.KRYO_SERIALIZER.getCode()));
        nettyServer.setServiceRegistry(new NacosServiceRegistry("127.0.0.1",8848));
        nettyServer.publishService(helloService,HelloService.class);
    }
}
