/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.websocket;

import com.mycompany.model.Message;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Himanshu.Devnani
 */

@ServerEndpoint(value = "/chat/{username}",
        encoders = {ChatEncoder.class, UserlistEncoder.class},
        decoders = ChatDecoder.class)
public class ChatEndPoint {
    
    private Session session;
    private static final Set<ChatEndPoint> activeConnections = new CopyOnWriteArraySet<>();
    private static final HashMap<String, String> users = new HashMap<>();
    
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        this.session = session;
        activeConnections.add(this);
        users.put(session.getId(), username);
        
        // Creating a message to inform all the connected clients that a new client have joined the conection
        Message message = new Message();
        message.setSenderName("System");
        message.setMessageContent(username + " has joined the chat");
        broadcast(message);
        broadcastUserlist();
    }
    
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    String username = users.get(session.getId());
    String reason = throwable.getMessage();

    // Check for a harmless connection reset
    if (reason != null && reason.contains("Connection reset")) {
        System.out.println("User disconnected abruptly: " + username + " [Connection reset]");
        return; // Skip logging full stack trace for this common scenario
    }

    Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, 
        "Connection error with user: " + username + ", Reason: " + reason, throwable);
}
   
    @OnMessage
    public void onMessage(Session session, Message message){
        String senderName = users.get(session.getId());
        if(senderName != null){
            message.setSenderName(senderName);
            broadcast(message);
        }else{
            Logger.getLogger(ChatEndPoint.class.getName())
                    .log(Level.SEVERE, "Sender not found for session:{0},", session.getId());
        }
    }
    
    @OnClose
    public void onClose(Session session){
        String sessionId = session.getId();
        String username = users.remove(sessionId);
        activeConnections.remove(this);
        
        // Creating a message to inform all the connected clients that a client have left the chat
        
        Message message = new Message();
        message.setSenderName("System");
        message.setMessageContent(username + " has left the chat.");
        broadcast(message);
        broadcastUserlist();
    }
    
    private static void broadcast(Message message){
        activeConnections.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    
    private static void broadcastUserlist(){
        // creating a list of all the connected users
        List<String> userList = new ArrayList<>(users.values()); 
        activeConnections.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(userList);
                } catch (IOException ex) {
                    Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
    
    } 
}
