/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.websocket;

import com.google.gson.Gson;
import com.mycompany.model.Message;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

/**
 *
 * @author Himanshu.Devnani
 */
public class ChatDecoder implements Decoder.Text<Message>{
    
    private Gson gson = new Gson();

    @Override
    public Message decode(String inputString) throws DecodeException {
        return gson.fromJson(inputString, Message.class);
    }

    @Override
    public boolean willDecode(String inputString) {
        return (inputString != null);
    }
    
}
