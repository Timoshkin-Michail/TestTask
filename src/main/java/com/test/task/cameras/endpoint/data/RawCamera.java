package com.test.task.cameras.endpoint.data;

import lombok.Data;

@Data
public class RawCamera {

    private Integer id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
