/*
 * This class is responsible of handling the callback response data
 */
package com.models;

public class CallBackResponse {
    private String success;
    private String uhs;
    private String XSTS_token;
    private String error;

     // Getters and setters
     public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getUhs() {
        return uhs;
    }

    public void setUhs(String uhs) {
        this.uhs = uhs;
    }

    public String getXSTS_token() {
        return XSTS_token;
    }

    public void setXSTS_token(String XSTS_token) {
        this.XSTS_token = XSTS_token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
