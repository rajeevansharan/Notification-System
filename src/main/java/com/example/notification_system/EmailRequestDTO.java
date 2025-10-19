package com.example.notification_system;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailRequestDTO {
    public String toMail;
    public String subject;
    public String body;
}
