/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.websocket;

import com.google.gson.Gson;
import com.mycompany.model.Message;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

/**
 *
 * @author Himanshu.Devnani
 */
public class ChatEncoder implements Encoder.Text<Message>{
    
    private final Gson gson = new Gson();

    @Override
    public String encode(Message message) throws EncodeException {
        return gson.toJson(message);
    }
    
}
