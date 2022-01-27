package com.system.design.seckill.business.controller;

import com.alibaba.fastjson.JSON;
import com.system.design.seckill.common.api.IAccountService;
import com.system.design.seckill.common.po.Account;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author chengzhengzheng
 * @date 2022/1/27
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
    @DubboReference
    private IAccountService accountService;

    @RequestMapping("/say")
    public Account sayHello() {
        return accountService.findById(1L);
    }
//
//    public static void main(String[] args) throws SocketException {
//        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//        while (networkInterfaces.hasMoreElements()) {
//            NetworkInterface networkInterface = networkInterfaces.nextElement();
//            byte[]           hardwareAddress  = networkInterface.getHardwareAddress();
//
//            if (hardwareAddress != null) {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0, hardwareAddressLength = hardwareAddress.length; i < hardwareAddressLength; i++) {
//                    byte address = hardwareAddress[i];
//                    sb.append(String.format("%02X%s", address, (i < hardwareAddress.length - 1) ? "-" : ""));
//                }
//                System.out.println("网卡:" + sb);
//            }
//        }
//    }
}
