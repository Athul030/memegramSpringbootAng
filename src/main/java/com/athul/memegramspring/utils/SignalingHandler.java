package com.athul.memegramspring.utils;

import com.athul.memegramspring.dto.SignalDataDTO;
import com.athul.memegramspring.enums.SignalType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new LinkedList<>();
    ConcurrentHashMap<String,WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    final ObjectMapper map1=new ObjectMapper();
    Logger log1= LoggerFactory.getLogger(SignalingHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        final String  msg1=message.getPayload();
        SignalDataDTO sigData=map1.readValue(msg1, SignalDataDTO.class);
        log1.debug("Receive message from client:",msg1);

        SignalDataDTO sigResp=new SignalDataDTO();
        if(sigData.getType().equalsIgnoreCase(SignalType.Login.toString()))	{
            SignalDataDTO sigResp2=new SignalDataDTO();
            sessionMap.put(sigData.getUsersId(), session);
            String sessionId= UUID.randomUUID().toString();
            sigResp2.setUsersId("signaling");
            sigResp2.setType(SignalType.UserId.toString());
            sigResp2.setData(sessionId);
            session.sendMessage(new TextMessage(map1.writeValueAsString(sigResp2)));


        }
        else if(sigData.getType().equalsIgnoreCase(SignalType.NewMember.toString()))	{

            sessionMap.values().forEach(a->{

                SignalDataDTO sigResp2=new SignalDataDTO();
                sigResp2.setUsersId(sigData.getUsersId());
                sigResp2.setType(SignalType.NewMember.toString());
                try	{
                    //Check if websocket is open
                    if(a.isOpen())	{
                        log1.debug("Sending New Member from",sigData.getUsersId());
                        a.sendMessage(new TextMessage(map1.writeValueAsString(sigResp2)));
                    }
                }
                catch(Exception e)	{
                    log1.error("Error Sending message:",e);
                }
            });


            return ;
        }
        else if(sigData.getType().equalsIgnoreCase(SignalType.offer.toString()))	{

            WebSocketSession toSession = sessionMap.get(sigData.getToUid());
            if (toSession != null) {
                sigResp=new SignalDataDTO();
                sigResp.setUsersId(sigData.getUsersId());
                sigResp.setType(SignalType.offer.toString());
                sigResp.setData(sigData.getData());
                sigResp.setToUid(sigData.getToUid());
                sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
            } else {
                log1.error("WebSocket session not found for user ID: {}", sessionMap);
            }

        }
        else if(sigData.getType().equalsIgnoreCase(SignalType.answer.toString()))	{
            WebSocketSession toSession = sessionMap.get(sigData.getToUid());
            if (toSession != null) {


            sigResp=new SignalDataDTO();
            sigResp.setUsersId(sigData.getUsersId());
            sigResp.setType(SignalType.answer.toString());
            sigResp.setData(sigData.getData());
            sigResp.setToUid(sigData.getToUid());
                sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
            } else {
                log1.error("WebSocket session not found for user ID: {}", sessionMap);
            }

        }
        else if(sigData.getType().equalsIgnoreCase(SignalType.Ice.toString()))	{
            WebSocketSession toSession = sessionMap.get(sigData.getToUid());
            if (toSession != null) {


            sigResp=new SignalDataDTO();
            sigResp.setUsersId(sigData.getUsersId());
            sigResp.setType(SignalType.Ice.toString());
            sigResp.setData(sigData.getData());
            sigResp.setToUid(sigData.getToUid());
            sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
            } else {
                log1.error("WebSocket session not found for user ID: {}", sessionMap);
            }

        }


    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }



}
