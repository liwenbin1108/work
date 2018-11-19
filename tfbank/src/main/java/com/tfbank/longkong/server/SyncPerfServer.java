package com.tfbank.longkong.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfbank.longkong.bean.ConfigBean;
import com.tfbank.longkong.utils.SendDataUtil;

import net.sf.json.JSONArray;

@Service
@SuppressWarnings("deprecation")
public class SyncPerfServer {
	@Autowired
	private SendDataUtil sendDataUtil;
	private ConfigBean configBean = null;;
	private Map<String, List<Map<String, String>>> tradeList = null;
	private final static Logger logger = LoggerFactory.getLogger(SyncPerfServer.class);
	public SyncPerfServer(ConfigBean configBean, Map<String, List<Map<String, String>>> tradeList) {
		super();
		this.configBean = configBean;
		this.tradeList = tradeList;
	}
	
	public SyncPerfServer() {
		super();
	}
	
	public void  setPerfServerParam(ConfigBean configBean, Map<String, List<Map<String, String>>> tradeList){
		this.configBean = configBean;
		this.tradeList = tradeList;
	}

	public  void syncPerData() throws Exception {
		for(Map<String, String> ciCodeMap : tradeList.get("CICODE")){
			if(null != ciCodeMap){
				 getRequestXml(ciCodeMap);
			}
		}
			
	}

	private  synchronized String getRequestXml(Map<String, String> ciCodeMap ) {
		InputStream in = null;
		Reader rd = null;
		StringBuilder sb = new StringBuilder(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">");
		sb.append("<soapenv:Body>");
		sb.append("<tem:GetDevvous>");
		sb.append("<tem:mgrObjId>" + ciCodeMap.get("MGROBJID") + "</tem:mgrObjId>");
		sb.append("</tem:GetDevvous>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		String soapRequestData = sb.toString();
		logger.info("soap request param："+soapRequestData);
		StringBuilder responseSB  = new StringBuilder();
		String currenttime = String.valueOf(System.currentTimeMillis());
		List<Map<String, Object>> perfDataArray = new ArrayList<Map<String, Object>>();
		try {
			@SuppressWarnings("resource")
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(configBean.getlWebserviceUrl());
			post.setHeader("Content-Type", "text/xml;charset=UTF-8");
			post.setHeader("SOAPAction", "http://tempuri.org/IDataService/GetDevvous");
			StringEntity entiy = new StringEntity(soapRequestData, "UTF-8");
			post.setEntity(entiy);
			HttpResponse response = client.execute(post);
			HttpEntity e = response.getEntity();
			logger.info("sopa response status code ："+response.getStatusLine().getStatusCode());
			in = e.getContent();
			rd = new InputStreamReader(in, "UTF-8");
			char[] buffer = new char[(int) e.getContentLength()];
			while (true) {
				int rsz = rd.read(buffer, 0, buffer.length);
				if (rsz < 0) {
					break;
				}
				responseSB.append(buffer, 0, rsz);
			}
			logger.info("sopa response data ："+responseSB);
			for (HashMap perfMap : pasrse(responseSB.toString())) {
				Map<String, Object> perfDataMap = new HashMap<String, Object>();
				String kpiId = (String) perfMap.get("Id");;
				if(null != kpiId && !"".equals(kpiId) && null != tradeList.get(ciCodeMap.get("CLZSS"))){
					for(Map<String,String> kpiMap : tradeList.get(ciCodeMap.get("CLZSS"))){
						if(kpiId.equalsIgnoreCase(kpiMap.get("ID"))){
							String unit = kpiMap.get("UNIT");
							perfDataMap.put("CI", ciCodeMap.get("CICODE"));
							if(null!=kpiMap.get("VALUE") && !"".equals(kpiMap.get("VALUE")) && !"NULL".equalsIgnoreCase(kpiMap.get("VALUE"))){
								perfDataMap.put("VALUE", kpiMap.get("VALUE").trim().equals(perfMap.get("Value").toString())?"正常":"断开");
							}else{
								perfDataMap.put("VALUE", perfMap.get("Value"));
							}
							perfDataMap.put("TIME", currenttime);
							perfDataMap.put("KPI", (ciCodeMap.get("ADDKPI")==null?"":ciCodeMap.get("ADDKPI"))+kpiMap.get("KPI"));
							perfDataMap.put("INSTANCE", ciCodeMap.get("INSTANCE"));
							perfDataMap.put("DESC","");
							if(null == unit ){
								unit = "";
							}
							perfDataMap.put("UNIT", unit);
							perfDataArray.add(perfDataMap);
							break;
						}
					}
				}
				if(perfDataArray.size() == configBean.getMonitorBatch()){
					sendDataUtil.sendDataToNoah(configBean.gettPerfUrl(), JSONArray.fromObject(perfDataArray));
					perfDataArray = null;
				}
			}
			if(null != perfDataArray && perfDataArray.size() != 0){
				sendDataUtil.sendDataToNoah(configBean.gettPerfUrl(), JSONArray.fromObject(perfDataArray));
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			try {
				if(null != in){
					in.close();
				}
				if(null != rd){
					rd.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return responseSB.toString();
	}

	private  ArrayList<HashMap> pasrse(String xml) {
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element employees = doc.getRootElement();
			for (Iterator i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				for (Iterator j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					for (Iterator k = node.elementIterator(); k.hasNext();) {
						Element node1 = (Element) k.next();
						for (Iterator l = node1.elementIterator(); l.hasNext();) {
							Element node2 = (Element) l.next();
							for (Iterator m = node2.elementIterator(); m.hasNext();) {
								Element node3 = (Element) m.next();
								for (Iterator n = node3.elementIterator(); n.hasNext();) {
									Element node4 = (Element) n.next();
									String key = null;
									String value = null;
									HashMap map = new HashMap();
									for (Iterator o = node4.elementIterator(); o.hasNext();) {
										Element node5 = (Element) o.next();
										key = node5.getName();
										value = node5.getText();
										if (key.equalsIgnoreCase("MgrObjId") || key.equalsIgnoreCase("Id")
												|| key.equalsIgnoreCase("Value")) {
											map.put(key, value);
										} else if (key.equalsIgnoreCase("AgentBM")) {
											list.add(map);
										}

									}

								}

							}
						}

					}

				}

			}
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		return list;
	}

}
