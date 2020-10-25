package yoru.example.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;
import yoru.example.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author YoruG
 * @date 2020/10/23-10:01
 **/
@Data
public class NettyClient implements RpcClient{
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private ServiceRegistry serviceRegistry;
    private static Bootstrap bootstrap;
    private static EventLoopGroup workerGroup;
    private CommonSerializer serializer = CommonSerializer.getByCode(0);


    public void start(){
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
//                        pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                        pipeline.addLast(new IdleStateHandler(0,5,0,TimeUnit.SECONDS));
                        pipeline.addLast(new CommonEncoder(serializer));
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }


    /**
     * 通过Netty的通道发送请求，这里目标服务器是通过注册中心的lookupService()得到的
     * 而不再像之前一样只能指定目标端口
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try{
            start();
            InetSocketAddress socketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            String remoteHost = socketAddress.getHostName();
            int remotePort = socketAddress.getPort();
            ChannelFuture channelFuture = bootstrap.connect(remoteHost, remotePort).sync();
            logger.info("客户端连接到服务器{}:{}",remoteHost,remotePort);
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        logger.info("客户端成功发送请求" + rpcRequest);
                    }
                    else
                        logger.error("客户端请求发送失败");
                }
            });
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            workerGroup.shutdownGracefully();
            return rpcResponse;
        }catch (Exception e){
            logger.error("发送请求时出现错误");
        }
        return null;
    }
}
