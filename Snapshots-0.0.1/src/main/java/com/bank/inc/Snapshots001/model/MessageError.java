package com.bank.inc.Snapshots001.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MessageError {
    private int code;
    private String message;

}
