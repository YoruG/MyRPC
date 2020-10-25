package yoru.example.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcException;
import yoru.example.serializer.CommonSerializer;

import java.net.InetSocketAddress;

/**
 * @author YoruG
 * @date 2020/10/23-10:06
 **/
@Data
public class NettyServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private String localhost;
    private int port;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer  = CommonSerializer.getByCode(0);
    public NettyServer(String localhost, int port) {
        this.localhost = localhost;
        this.port = port;
    }



    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new NettyServerHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            ShutdownHook.getShutdownHookInstance(serviceRegistry,new InetSocketAddress(localhost,port)).addClearHook();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("启动服务器出现错误！！");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 发布服务到注册中心上，同时在本地的注册表上也注册对应服务
     * @param service  服务
     * @param serviceClass  服务类
     * @param <T>
     */
    @Override
    public <T> void publishService(Object service, Class<T> serviceClass)  {
        if(serializer == null){
            logger.error(RpcException.FAIL_SERIALIZER);
            return;
        }
        InetSocketAddress address = new InetSocketAddress(localhost,port);
        String serviceName = serviceClass.getCanonicalName();
        new DefaultServiceProvider().addService(service,serviceName);
        serviceRegistry.register(serviceName,address);
        start();
    }
}
