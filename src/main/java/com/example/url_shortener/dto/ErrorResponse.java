package com.example.url_shortener.dto;

public class ErrorResponse
{

    private String message;
    private int status;
    private String timestamp;

    public ErrorResponse(){
    }

    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }




}
