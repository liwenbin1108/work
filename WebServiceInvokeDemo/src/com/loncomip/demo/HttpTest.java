package com.loncomip.demo;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpTest {

	public static void main(String[] args) {
		
        String retVal = "";
		//服务地址
		String method = "http://10.182.194.222:9998/IDataService.svc/web/GetEvents";
		HttpClient client = new DefaultHttpClient();
		//HttpPost request = new HttpPost(method)
		HttpGet request = new HttpGet(method);
		
		try {
			//JSONObject params = new JSONObject();
			//params.put("mgrobjId", "123");
			//params.put("id", "456");
			//System.out.println(params.toString());
			//request.setEntity(new StringEntity(params.toString()));
			
			HttpParams params=new BasicHttpParams();
			/*params.setParameter("mgrobjId", "02300011kt01");
			params.setParameter("id", "2010500010");
			request.setParams(params);*/
			
			request.setHeader(HTTP.CONTENT_TYPE, "text/json");
			HttpResponse response = client.execute(request);
			retVal = EntityUtils.toString(response.getEntity());
			System.out.println(retVal);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
}
