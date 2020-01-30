package com.test.task.cameras;

import com.test.task.cameras.endpoint.Camera;
import com.test.task.cameras.endpoint.Endpoint;
import com.test.task.cameras.endpoint.data.RawCamera;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Slf4j
@Configuration
public class Aggregator implements ApplicationRunner {
    private final Endpoint endpoint;

    @Value("${threads:1}")
    private int threadsNum;

    private volatile int currentCamera = 0;
    private volatile List<RawCamera> rawCameras;
    private volatile List<Camera> cameras;

    public Aggregator(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Сбор информации о камерах");
        rawCameras = Collections.synchronizedList(endpoint.getCamerasList());
        cameras = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, Loader> threads = new HashMap<>();
        for (int i = 0; i < threadsNum; i++) {
            Loader thread = new Loader();
            thread.start();
            threads.put(i, thread);
        }
        int currentThread = 0;
        log.debug("Threads num {}", threadsNum);
        while (currentCamera < rawCameras.size()) {
            if (!threads.get(currentThread).isAlive()) {
                Loader newThread = new Loader();
                newThread.start();
                threads.put(currentThread, newThread);
            }
            currentThread = (currentThread + 1) % threadsNum;
        }
        for (Loader thread : threads.values()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Полученный результат: {}", cameras);
    }

    private synchronized RawCamera getCurrentCamera() {
        if (currentCamera >= rawCameras.size()) {
            return null;
        }
        RawCamera rawCamera = rawCameras.get(currentCamera);
        currentCamera++;
        return rawCamera;
    }

    class Loader extends Thread {
        @Override
        public void run() {
            RawCamera rawCamera = getCurrentCamera();
            if (rawCamera != null) {
                cameras.add(endpoint.getCameraInfo(rawCamera));
            }
            log.debug("Поток {} закончил работу", currentThread());
        }
    }

}
