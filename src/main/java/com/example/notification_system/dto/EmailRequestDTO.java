package com.example.notification_system.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailRequestDTO {
    public String toMail;
    public String subject;
    public String body;
}
