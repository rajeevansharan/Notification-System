package com.example.notification_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO implements Serializable {
    private String toMail;
    private String subject;
    private String body;
}
