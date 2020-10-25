package yoru.example.rpc;

import yoru.example.entity.HelloObject;
import yoru.example.enums.SerializerType;
import yoru.example.serializer.CommonSerializer;
import yoru.example.service.HelloService;

/**
 * @author YoruG
 * @date 2020/10/22-23:21
 **/
public class ClientMain {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.setServiceRegistry(new NacosServiceRegistry("127.0.0.1",8848));
//        client.setSerializer(CommonSerializer.getByCode(SerializerType.HESSIAN_SERIALIZER.getCode()));
//        client.setSerializer(CommonSerializer.getByCode(SerializerType.JSON_SERIALIZER.getCode()));
        client.setSerializer(CommonSerializer.getByCode(SerializerType.KRYO_SERIALIZER.getCode()));


        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(1, "小李");
        String result = helloService.hello(object);
        System.out.println("通过服务端实现方法得到结果:" + result);
    }
}
