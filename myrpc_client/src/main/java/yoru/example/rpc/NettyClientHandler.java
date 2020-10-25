package yoru.example.rpc;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;

/**
 * @author YoruG
 * @date 2020/10/23-11:59
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        logger.info("客户端接收到服务端的响应");
        Channel channel = channelHandlerContext.channel();
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        channel.attr(key).set(rpcResponse);
  //    channel.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("客户端通道连接的数据处理过程中出现异常");
        ctx.close();
    }




    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()){
                case READER_IDLE:
                    logger.info("出现读空闲");
         //           channel.close();
                    break;
                case WRITER_IDLE:
                    logger.info("出现写空闲,发送心跳包给服务器{}",channel.remoteAddress());
        //            channel.close();
                    break;
                case ALL_IDLE:
                    logger.info("出现读写空闲,发送心跳包给服务器{}",channel.remoteAddress());
//                    logger.info(channel.toString());
//                    RpcRequest heartBeatRequest = new RpcRequest();
//                    heartBeatRequest.setHeartBeat(true);
//                    channel.writeAndFlush(heartBeatRequest);
                    break;
            }
        }
    }
}
