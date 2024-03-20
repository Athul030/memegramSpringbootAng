package com.athul.memegramspring.controller;

import com.athul.memegramspring.utils.MessageSockIO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component

public class SocketIOController {

    private SocketIOServer socketServer;

    SocketIOController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        this.socketServer.addEventListener("messageSendToUser", MessageSockIO.class, onSendMessage);

    }
    public ConnectListener onUserConnectWithSocket = client -> System.out.println("Perform operation on user connect in controller");
    public DisconnectListener onUserDisconnectWithSocket = client -> System.out.println("Perform operation on user disconnect in controller");

    public DataListener<MessageSockIO> onSendMessage = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, MessageSockIO message, AckRequest acknowledge) {
            /**
             * target user should subscribe the socket event with his/her name.
             */
            System.out.println(message.getSenderName() + " user send message to user " + message.getTargetUserName() + " and message is " + message.getMessage());
            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(), client, message);
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };
}
