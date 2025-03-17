package com.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    
    // setters and getters- 
 
    // empty constructor -=
    public UserDTO() {
    }
    public UserDTO(String username){
        this.username = username;
    }
    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUsename(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
