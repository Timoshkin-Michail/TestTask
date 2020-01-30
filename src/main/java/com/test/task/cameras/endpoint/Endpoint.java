package com.test.task.cameras.endpoint;

import com.test.task.cameras.endpoint.data.RawCamera;
import com.test.task.cameras.endpoint.data.SourceData;
import com.test.task.cameras.endpoint.data.TokenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Lazy
@Component
public class Endpoint {
    private final RestTemplate restTemplate;

    @Value("${camera.url}")
    private String url;

    public Endpoint(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RawCamera> getCamerasList() {
        RawCamera[] rawCameras = restTemplate.getForObject(url, RawCamera[].class);
        if (rawCameras == null || rawCameras.length == 0) {
            throw new RuntimeException("Получен пустой ответ от сервиса " + url);
        }
        List<RawCamera> rawCamerasList = new ArrayList<>(Arrays.asList(rawCameras));
        log.debug("Получен ответ: {}", rawCamerasList);
        return rawCamerasList;
    }

    public Camera getCameraInfo(RawCamera rawCamera) {
        log.debug("Получение данных для камеры {}", rawCamera);
        Camera camera = new Camera();
        camera.setId(rawCamera.getId());
        SourceData sourceData = restTemplate.getForObject(rawCamera.getSourceDataUrl(), SourceData.class);
        log.debug("Получена информация: {}", sourceData);
        if (sourceData != null) {
            camera.setUrlType(sourceData.getUrlType());
            camera.setVideoUrl(sourceData.getVideoUrl());
        }
        TokenData tokenData = restTemplate.getForObject(rawCamera.getTokenDataUrl(), TokenData.class);
        log.debug("Получена информация: {}", tokenData);
        if (tokenData != null) {
            camera.setValue(tokenData.getValue());
            camera.setTtl(tokenData.getTtl());
        }
        return camera;
    }


}
