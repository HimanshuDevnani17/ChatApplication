/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.websocket;

import com.google.gson.Gson;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import java.util.List;

/**
 *
 * @author Himanshu.Devnani
 */
public class UserlistEncoder implements Encoder.Text<List<String>>{
    
    private Gson gson = new Gson();

    @Override
    public String encode(List<String> userList) throws EncodeException {
        return gson.toJson(userList);
    }
    
}
