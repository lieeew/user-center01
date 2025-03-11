package com.dashi.usercenter.model;

import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    private String age;
    private String email;
}
