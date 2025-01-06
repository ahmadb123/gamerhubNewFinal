package com.dto;

public class AuthResponseDTO {
    private String token;
    private long id;
    private String username;


    // construction

    public AuthResponseDTO(String token, long id, String username){
        this.token = token;
        this.id = id;
        this.username = username;
    }
    public String getToken(){
        return token;
    }

    public long getid(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setid(long id){
        this.id = id;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
}
