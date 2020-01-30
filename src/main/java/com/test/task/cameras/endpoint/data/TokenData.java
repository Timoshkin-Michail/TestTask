package com.test.task.cameras.endpoint.data;

import lombok.Data;

@Data
public class TokenData {
    private String value;
    private Integer ttl;
}
