package org.example;

import org.example.client.annotation.RpcClientApplication;
import org.example.common.constant.Protocol;

@RpcClientApplication(rpcApiPackages = {"org/example/communication/server/api"}, protocols = {Protocol.TCP, Protocol.UDP})
public class MeetingClient {
    public static void main(String[] args) {
        RpcClient.run(MeetingClient.class);
    }
}
