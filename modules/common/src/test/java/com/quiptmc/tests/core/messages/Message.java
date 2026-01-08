package com.quiptmc.tests.core.messages;

public class Message {

    String message;

    public Message(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void message(String message) {
        this.message = message;
    }


    public void send() {
        System.out.println(message);
    }
}
