package com.tfbank.longkong.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfbank.longkong.bean.ConfigBean;
import com.tfbank.longkong.utils.SendDataUtil;

import net.sf.json.JSONArray;
@Service
public class SyncEventServer {
	private final static String  CLEAR_TABEL_FIELD = "RemoveTime";
	private final static String  OPEN_TABEL_FIELD = "AlarmTime";
	@Autowired
	private SendDataUtil sendDataUtil;
	private ConfigBean configBean = null;;
	private Map<String, List<Map<String, String>>> tradeList = null;
	private final static Logger logger = LoggerFactory.getLogger(SyncEventServer.class);
	
	
	public SyncEventServer() {
		super();
	}
	public SyncEventServer(ConfigBean configBean, Map<String, List<Map<String, String>>> tradeList) {
		super();
		this.configBean = configBean;
		this.tradeList = tradeList;
	}
	public void setEventServerParam(ConfigBean configBean, Map<String, List<Map<String, String>>> tradeList){
		this.configBean = configBean;
		this.tradeList = tradeList;
	}
	public void syncOpenEvnetData(){
		String lastTime = getLastAlarmTime(OPEN_TABEL_FIELD);
		//String lastTime = "2018-04-13 02:52:17.0";
		String sql = "select a.AlarmTime,a.RemoveTime,a.MgrObjId,a.Ch,a.Value,a.Level,a.Content,a.EvtLogId,b.Unit,b.devname,d.Name "
				+ "from EventLog a INNER JOIN Devvou b ON a.MgrObjId=b.MgrObjId and a.Id=b.ID INNER JOIN MgrObj c on a.MgrObjId=c.Id "
				+ "INNER JOIN AddrNode d on c.AddrId=d.Id where a.RemoveTime is null and a.AlarmTime>'"
				+ lastTime + "' order by a.AlarmTime desc";
		logger.info("Alarm  sql : "+sql);
		syncEvnetData("1", sql, OPEN_TABEL_FIELD);
	}
	public void syncClearEvnetData(){
		String lastTime = getLastAlarmTime(CLEAR_TABEL_FIELD);
		String sql = "select a.AlarmTime,a.RemoveTime,a.MgrObjId,a.Ch,a.Value,a.Level,a.Content,a.EvtLogId,b.Unit,b.devname,d.Name "
		+ "from EventLog a INNER JOIN Devvou b ON a.MgrObjId=b.MgrObjId and a.Id=b.ID "
		+ "INNER JOIN MgrObj c on a.MgrObjId=c.Id INNER JOIN AddrNode d on c.AddrId=d.Id where a.RemoveTime is not null and a.RemoveTime>'"
		+ lastTime + "' order by a.RemoveTime desc";
		logger.info("clear Alarm sql : "+sql);
		syncEvnetData("2", sql, CLEAR_TABEL_FIELD);
	}

	private  void syncEvnetData(String status,String sql,String tableFieldName){
		String mgrObjId = null;
		String currentLastestTime = null;
		boolean first = true;
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> eventDataArray = new ArrayList<Map<String, Object>>();
		try{
			Class.forName(configBean.getlDeviceCalssName());
			dbConn = DriverManager.getConnection(configBean.getlDbUrl(), configBean.getlDbUserName(), configBean.getlDbUserPwd());
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if(first){
					first = false;
					currentLastestTime = rs.getString(tableFieldName);
					updateLastAlarmTime(tableFieldName, currentLastestTime);
				}
				mgrObjId = rs.getString("MgrObjId");
				
				Map<String, Object>  eventDataMap = new HashMap<String, Object>();
				eventDataMap.put("SourceCIName",mgrObjId);
				
				for(Map<String, String>  ciMap : tradeList.get("CICODE")){
					if(ciMap.get("MGROBJID").equalsIgnoreCase(mgrObjId)){
						eventDataMap.put("SourceCIName",ciMap.get("CICODE"));
						break;
					}
				}
				eventDataMap.put("Summary",rs.getString("Content"));
				eventDataMap.put("Severity",rs.getString("Level"));
				eventDataMap.put("Status",status);
				eventDataMap.put("SourceID","1");
				eventDataMap.put("LastOccurrence",rs.getString(tableFieldName).substring(0, 19));
				eventDataMap.put("SourceIdentifier","Identifier");
				eventDataMap.put("SourceEventID",rs.getString("EvtLogId"));
				eventDataMap.put("SourceAlertKey",rs.getString("Ch"));
				eventDataMap.put("sourceSeverity",getLevel(rs.getString("Level")));
				eventDataArray.add(eventDataMap);
				if(eventDataArray.size() == configBean.getMonitorBatch()){
					sendDataUtil.sendDataToNoah(configBean.gettEventUrl(), JSONArray.fromObject(eventDataArray));
					eventDataArray = null;
				}
			}
			if(null != eventDataArray && eventDataArray.size() != 0){
				sendDataUtil.sendDataToNoah(configBean.gettEventUrl(), JSONArray.fromObject(eventDataArray));
			}
		}catch(SQLException e){
			logger.error(e.getMessage(),e);
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			closeConn(dbConn, stmt, null, rs);
		}
	}

	
	private synchronized void updateLastAlarmTime(String tableFieldName,String fieldValue) {
		String updateSql = "update tfbank_longkong_event_time set " + tableFieldName +"='" + fieldValue + "'";
		updateVmdb(updateSql);
	}
	private String getLevel(String level){
		try{
			return String.valueOf(5-Integer.parseInt(level));
		}catch(NumberFormatException e){
			logger.error(e.getMessage(),e);
			return level;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			return level;
		}
	}

	private synchronized String getLastAlarmTime(String tableFieldName) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet ret = null;
		String lastAlarmTime = null;
		try {
			Class.forName(configBean.gettDeviceClassName());// 指定连接类型
			conn = DriverManager.getConnection(configBean.gettDbUrl(), configBean.gettUserName(), configBean.gettPassword());// 获取连接
			String sql = "select * from tfbank_longkong_event_time";
			logger.debug("select LastestClearAlarmTime sql ：" + sql);
			pst = conn.prepareStatement(sql);// 准备执行语句
			ret = pst.executeQuery();
			while (ret.next()) {
				lastAlarmTime  = ret.getString(tableFieldName);
				logger.debug(tableFieldName + " is :" + lastAlarmTime );
			} 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			closeConn(conn, null, pst, ret);
		}
		return lastAlarmTime ;
	}

	/*private  void clearAllEvent() {
		String updateSql = "delete from mon_eap_event_memory where sourceid='1'";
		updateVmdb(updateSql);
	}*/

	private synchronized void updateVmdb(String sql){
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(configBean.gettDeviceClassName());// 指定连接类型
			conn = DriverManager.getConnection(configBean.gettDbUrl(), configBean.gettUserName(), configBean.gettPassword());// 获取连接
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			closeConn(conn, stmt, null, null);
		}
	}
	private void closeConn(Connection conn,Statement stmt,PreparedStatement pst, ResultSet rs){
		try {
			if(null != rs){
				rs.close();
			}
			if(null != stmt){
				stmt.close();
			}
			if(null != pst){
				pst.close();
			}
			if(null != conn){
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
