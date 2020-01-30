package com.test.task.cameras.endpoint;

import lombok.Data;

@Data
public class Camera {
    private Integer id;
    private String urlType = "";
    private String videoUrl = "";
    private String value = "";
    private Integer ttl = 0;
}
