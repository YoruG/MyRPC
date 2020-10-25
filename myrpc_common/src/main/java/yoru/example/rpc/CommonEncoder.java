package yoru.example.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import yoru.example.enums.PackageType;
import yoru.example.entity.RpcRequest;
import yoru.example.serializer.CommonSerializer;

/**
 * @author YoruG
 * @date 2020/10/23-10:33
 **/
public class CommonEncoder extends MessageToByteEncoder {
    private final CommonSerializer serializer;
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //写入魔数(识别码)
        byteBuf.writeInt(MAGIC_NUMBER);
        //区分发送的数据包时请求类型还是响应类型
        if (o instanceof RpcRequest)
            byteBuf.writeInt(PackageType.RPC_REQUEST.getCode());
        else
            byteBuf.writeInt(PackageType.RPC_RESPONSE.getCode());
        byteBuf.writeInt(serializer.getCode());
        //获得序列化器序列后的数组
        byte[] bytes = serializer.serialize(o);
        byteBuf.writeInt(bytes.length);
        System.out.println("len:" + bytes.length);
//        System.out.println(new String(bytes));
        //写入序列化数据
        byteBuf.writeBytes(bytes);
    }
}
