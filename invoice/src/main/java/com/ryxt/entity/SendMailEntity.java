package com.ryxt.entity;

import lombok.Data;

import java.util.List;
@Data
public class SendMailEntity {
    private String to;
    private String title;
    private String text;
    private List<String> urls;
}
