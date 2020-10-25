package yoru.example.rpc;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;

import java.lang.reflect.Method;

/**
 * @author YoruG
 * @date 2020/10/23-11:37
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private ServiceProvider serviceProvider = new DefaultServiceProvider();


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        if (request.getHeartBeat() == true){
            logger.info("接收到客户端发送的心跳包，不做处理");
            return;
        }
        logger.info("服务端接受到请求，正在处理...");
        Object service = serviceProvider.getService(request.getInterfaceName());
        Object result = handle(request,service);
        Channel channel = channelHandlerContext.channel();
        ChannelFuture channelFuture = channel.writeAndFlush(result);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("服务端通道连接的数据处理过程中出现异常");
        ctx.close();
    }




    /**
     * 通过反射来调用服务类执行对应的接口方法并返回结果
     * @param request   客户端请求
     * @param service   服务类
     * @return
     */
    public Object handle(RpcRequest request,Object service){
        Object data;
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            data = method.invoke(service, request.getParameters());
            logger.info("服务{}成功调用方法{}",request.getInterfaceName(),request.getMethodName());
        } catch (Exception e) {
            logger.error("通过反射调用本地服务处理请求出现错误");
            return RpcResponse.fail("远程服务调用失败");
        }
        return RpcResponse.success(data);
    }
}
