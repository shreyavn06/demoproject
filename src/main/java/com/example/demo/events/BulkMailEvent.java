package com.example.demo.events;

import java.util.List;

public class BulkMailEvent {

    private final List<String> emails;

    public BulkMailEvent(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getEmails() {
        return emails;
    }
}