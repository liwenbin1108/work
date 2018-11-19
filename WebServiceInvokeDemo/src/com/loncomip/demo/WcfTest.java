package com.loncomip.demo;

import java.net.URL;  

import javax.xml.namespace.QName;  
//import javax.xml.rpc.ServiceException;  
  
import org.apache.axis.client.Call;  
import org.apache.axis.client.Service;  
//import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;  
//import org.dom4j.Document;  
//import org.dom4j.DocumentHelper;  
//import org.dom4j.Element;

public class WcfTest {
	public static void main(String[] args) throws Exception {  
        Service sv = new Service();  //new 一个服务  
        Call call = (Call)sv.createCall();  //创建一个call对象  
        call.setTargetEndpointAddress(new URL("http://10.182.194.222:9998/IDataService.svc?wsdl"));  //设置要调用的接口地址以上一篇的为例子  
        call.setOperationName(new QName("http://tempuri.org/", "GetMgrObjs"));  //设置要调用的接口方法  
        call.addParameter(new QName("http://tempuri.org/", "mgrobjId"), org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);//设置参数名 id  第二个参数表示String类型,第三个参数表示入参  
       // call.addParameter(new QName("http://tempuri.org/", "cmd"), org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);//设置参数名 id  第二个参数表示String类型,第三个参数表示入参  
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_BOOLEAN);//返回参数类型  
        call.setUseSOAPAction(true); 
        call.setSOAPActionURI("http://tempuri.org/IDataService/GetMgrObjs"); 
        
        //开始调用方法,假设我传入的参数id的内容是1001   调用之后会根据id返回users信息，以xml格式的字符串返回，也可以json格式主要看对方用什么方式返回  
        Boolean result = (Boolean) call.invoke(new Object[]{"mjobjid","02300011kt01"});  
        
        //System.out.println(result);//打印字符串  
        //Document doc = DocumentHelper.parseText(result);//转成Document对象  
        //Element root = doc.getRootElement();//用dom4j方式拿到xml的根节点然后打印结果信息  
        //System.out.println("id="+root.element("UsersID").getText()+"    name="+root.element("UsersName").getText()+"     sex="+root.element("UsersSex").getText());  
          
    }  
}
