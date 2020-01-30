package com.test.task.cameras;

import com.test.task.cameras.client.HttpClientConfiguration;
import com.test.task.cameras.client.LogHttpRequestInterceptor;
import com.test.task.cameras.client.RestTemplateConfiguration;
import com.test.task.cameras.endpoint.Camera;
import com.test.task.cameras.endpoint.Endpoint;
import com.test.task.cameras.endpoint.data.RawCamera;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {
		RestTemplateConfiguration.class,
		Endpoint.class,
		RestTemplateAutoConfiguration.class,
		HttpClientConfiguration.class,
		LogHttpRequestInterceptor.class
})
class CamerasApplicationTests {

	@Autowired
	private Endpoint endpoint;

	@Autowired
	private ApplicationArguments applicationArguments;

	@Test
	void endpointGetCameraList() {
		List<RawCamera> l = endpoint.getCamerasList();
		log.info("{}", l);
	}

	@Test
	void endpointGetCameraInfo() {
		RawCamera rawCamera = new RawCamera();
		rawCamera.setId(1);
		rawCamera.setSourceDataUrl("http://www.mocky.io/v2/5c51b230340000094f129f5d");
		rawCamera.setTokenDataUrl("http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s");
		Camera camera = endpoint.getCameraInfo(rawCamera);
		log.info("{}", camera);
	}


}

