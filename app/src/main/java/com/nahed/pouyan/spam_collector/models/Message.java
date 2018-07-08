package com.nahed.pouyan.spam_collector.models;

/**
 * Created by pouyan on 3/19/18.
 */

public class Message {

    private String id;
    private String body;
    private String address;
    public boolean selected;

    public Message(String id, String body, String address) {
        this.id = id;
        this.body = body;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getAddress() {
        return address;
    }
}
