package yoru.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author YoruG
 * @date 2020/10/22-21:42
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private String interfaceName;   //接口名
    private String methodName;      //方法名
    private Object[] parameters;    //方法参数
    private Class<?>[] paramTypes;  //参数类型
    private Boolean heartBeat = false;     //是否是心跳包
}
