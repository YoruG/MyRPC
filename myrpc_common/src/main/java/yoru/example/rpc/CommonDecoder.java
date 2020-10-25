package yoru.example.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import yoru.example.enums.PackageType;
import yoru.example.entity.RpcException;
import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;
import yoru.example.serializer.CommonSerializer;

import java.util.List;

/**
 * @author YoruG
 * @date 2020/10/23-11:17
 **/
public class CommonDecoder extends ReplayingDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //读取魔数
        int magicNumber = byteBuf.readInt();
        if (magicNumber != MAGIC_NUMBER)
            throw new RpcException(RpcException.UNKNOWN_PACKAGE_ERROR);
        //读取数据包类型 请求/响应
        int packageTypeCode = byteBuf.readInt();
        Class<?> packageClass;
        if (packageTypeCode == PackageType.RPC_REQUEST.getCode())
            packageClass = RpcRequest.class;
        else if (packageTypeCode == PackageType.RPC_RESPONSE.getCode())
            packageClass = RpcResponse.class;
        else
            throw new RpcException(RpcException.UNKNOWN_PACKAGE_ERROR);
        //读取序列化器类型
        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null)
            throw new RpcException(RpcException.NO_EXIST_SERIALIZER);
        //读取序列化数据
        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        //实现反序列化
        Object obj = serializer.deserialize(bytes, packageClass);
        list.add(obj);
    }
}
