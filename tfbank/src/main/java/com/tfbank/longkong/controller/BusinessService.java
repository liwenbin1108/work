package com.tfbank.longkong.controller;


import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tfbank.longkong.bean.ConfigBean;
import com.tfbank.longkong.server.SyncEventServer;
import com.tfbank.longkong.server.SyncPerfServer;
import com.tfbank.longkong.utils.ExcelUtil;

import net.sf.json.JSONObject;

@Component
@EnableScheduling
@Service
public class BusinessService implements InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(BusinessService.class);

	@Autowired
	ConfigBean configBean;
	@Autowired
	private SyncEventServer eventServer;
	@Autowired
	private SyncPerfServer perfServer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info(configBean.toString());
		Map<String, List<Map<String, String>>> tradeList = ExcelUtil.importExcle(configBean.getExclepath());
		logger.info(JSONObject.fromObject(tradeList).toString());
		perfServer.setPerfServerParam(configBean, tradeList);
		eventServer.setEventServerParam(configBean, tradeList);
		perfScheduled() ;
		eventScheduled();
	}
	
	@Async
	public void perfScheduled() {
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long begin = System.currentTimeMillis();
				try {
					perfServer.syncPerData();
					logger.info("perfScheduled execute time : {}", System.currentTimeMillis()-begin);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}, 0, configBean.getPerfInterval());
	}
	
	@Async
	public void eventScheduled() {
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long begin = System.currentTimeMillis();
				 try {
					eventServer.syncClearEvnetData();
					eventServer.syncOpenEvnetData();
					logger.info("eventScheduled execute time : {}", System.currentTimeMillis()-begin);
				 } catch (Exception e) {
						logger.error(e.getMessage(),e);
				 }
			}
		}, 0, configBean.getEventInterval());
	}
	
	

}
