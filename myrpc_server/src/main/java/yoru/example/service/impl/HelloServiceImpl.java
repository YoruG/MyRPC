package yoru.example.service.impl;

import yoru.example.entity.HelloObject;
import yoru.example.service.HelloService;

/**
 * @author YoruG
 * @date 2020/10/22-21:35
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject object) {
        return "Hello,打工人" + object.getId() +"! 你的消息是" + object.getMessage();
    }
}
