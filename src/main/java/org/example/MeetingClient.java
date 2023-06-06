package org.example;

import org.example.client.annotation.RpcClientApplication;
import org.example.common.constant.Protocol;
import org.example.shell.ShellContext;

@RpcClientApplication(rpcApiPackages = {"org.example.communication.server.api"}, protocols = {Protocol.TCP, Protocol.UDP})
public class MeetingClient {
    public static void main(String[] args) throws Exception{
        RpcClient.run(MeetingClient.class);
        Thread.sleep(2000);
        ShellContext.run(MeetingClient.class);
    }
}
