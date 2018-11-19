package com.tfbank.longkong.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import net.sf.json.JSONArray;

@Component
public class SendDataUtil {
	private final static Logger logger = LoggerFactory.getLogger(SendDataUtil.class);
	@Autowired
	private RestTemplate restTemplate;
	public synchronized void sendDataToNoah(String url,JSONArray data){
		try {
			logger.info("send data url :" + url);
			logger.info("send data :" + data.toString());
			Object result = restTemplate.postForEntity(url, data, String.class);
			logger.info("send data result :" + result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
}
