package com.seguros.fcm;



public class TokenObject {

//    @SerializedName("token")
    private String token;

    public TokenObject(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
