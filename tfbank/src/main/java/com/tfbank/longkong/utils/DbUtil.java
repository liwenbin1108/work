package com.tfbank.longkong.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;


public class DbUtil {
	
	private static final Logger logger = Logger.getLogger(DbUtil.class);
        
    public static DbUtil db = null;
    private static Connection conn = null;
    private static DruidDataSource dataSource = null;
    
    public static void main(String[] args) {
		String dbType = "MYSQL";
		String username = "root";
		String password = "123456";
		String url = "jdbc:mysql://127.0.0.1:3306/db_vmdb?useUnicode=true&characterEncoding=UTF-8";
		DbUtil dbUtil = DbUtil.getInstance(dbType, username, password, url);
		String sql = "update PERFORMANCE_CURRENT t set instance='CPU_2'";
		Map<Integer, Object> param = new HashMap<>();
		param.put(1, "CPU_1");
		try {
			System.out.println(dbUtil.update(sql,param));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    
    public DbUtil (String deviceClassName, String username, String password, String rul) {
    	if (dataSource == null || dataSource.isClosed()) {
    		dataSource = creatDataSource(deviceClassName, username, password, rul);
    	}
    	
    	if (conn == null || dataSource.isClosed()) {
    		conn = getConnection(deviceClassName, username, password, rul);
    	}
    }
    
    public synchronized static DbUtil getInstance (String deviceClassName, String username, String password, String rul) {
    	if (db == null) {
    		db = new DbUtil(deviceClassName, username, password, rul);
    	}
    	return db;
    }
    
    private static DruidDataSource creatDataSource (String deviceClassName, String username, String password, String url) {
    	try{
	    	if (StringUtils.isEmpty(url.trim())) {
	    		throw new Exception("OUT DB param-url is empty!");
	    	}
	    	if (StringUtils.isEmpty(username)) {
	    		throw new Exception("OUT DB param-username is empty!");
	    	}
	    	if (StringUtils.isEmpty(password)) {
	    		throw new Exception("OUT DB param-password is empty!");
	    	}
	    	if (StringUtils.isEmpty(deviceClassName)) {
	    		throw new Exception("OUT DB param-dbType is not exist!");
	    	}
	    	
	    	dataSource = new DruidDataSource();
	    	dataSource.setUrl(url);
	    	dataSource.setUsername(username);
	    	dataSource.setPassword(password);
	    	dataSource.setDriverClassName(deviceClassName);
	    	dataSource.setInitialSize(2);
	    	dataSource.setMaxActive(10);// 设置最大链接数
	    	dataSource.setMinIdle(2);// 最新连接数
	    	dataSource.setMaxWait(60000);
	    	return dataSource;
    	} catch (Exception e) {
    		logger.error("Create datasource",e);
    		return null;
    	}
    }
    
    private static Connection getConnection (String dbType, String username, String password, String rul) {
    	try {
    		conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return conn;
    }
    
    /**
     * 批量保存数据
     * 
     * @param sql
     * @param datas
     * @param batch
     */
    public void executeBatch(String sql, List<Map<Integer, Object>> datas, Integer batch) {
    	long start = System.currentTimeMillis();
        PreparedStatement pst = null;
        try{
        	System.out.println("connection ========> " + conn.toString());
	        conn.setAutoCommit(false);
	        pst = conn.prepareStatement(sql);
	        int paramsSize = datas.size();
	        for (int i = 0; i < paramsSize; i++) {
	        	Map<Integer, Object> data = datas.get(i);
	        	for (Integer key : data.keySet()) {
	        		pst.setObject(key, data.get(key));
	        	}
	        	pst.addBatch();
	        	if ((i+1) % batch == 0) {
        			pst.executeBatch();
	        	}
	        }
            pst.executeBatch();
	        conn.commit();
        } catch (Exception e) {
        	logger.error("Batch execution exceptions!The sql statement ===>" + sql, e);
        } finally {
			if(null != pst){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        long end = System.currentTimeMillis();
        logger.info("Batch execution time consuming " + (end - start) + "ss");
    }
    
    /**
     * 查询数据
     * 
     * @param sql
     * @param params
     * @return
     */
    public List<Map<String,Object>> query(String sql,Object...params){
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = conn.prepareStatement(sql);
            if (params != null) {
                int paramsIndex = 1;
                for(Object p : params){
                    pst.setObject(paramsIndex++, p);
                }
            }
            rs = pst.executeQuery();
            ResultSetMetaData rst = rs.getMetaData();
            int column = rst.getColumnCount();
            List<Map<String,Object>> rstList = new ArrayList<Map<String,Object>>();
            while(rs.next()){
                Map<String,Object> m = new HashMap<String,Object>();
                for(int i=1;i<=column;i++){
                    m.put(rst.getColumnName(i), rs.getObject(i));
                }
                rstList.add(m);
            }
            return rstList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
     
     public List<Map<String,Object>> query(String sql,List<Object> params){
            PreparedStatement pst = null;
            ResultSet rs = null;
            try{
                pst = conn.prepareStatement(sql);
                int paramsIndex = 1;
                for(Object p : params){
                    pst.setObject(paramsIndex++, p);
                }
                rs = pst.executeQuery();
                ResultSetMetaData rst = rs.getMetaData();
                int column = rst.getColumnCount();
                List<Map<String,Object>> rstList = new ArrayList<Map<String,Object>>();
                while(rs.next()){
                    Map<String,Object> m = new HashMap<String,Object>();
                    for(int i=1;i<=column;i++){
                        m.put(rst.getColumnName(i), rs.getObject(i));
                    }
                    rstList.add(m);
                }
                return rstList;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    
    public long queryLong(String sql,Object...params){
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = conn.prepareStatement(sql);
            int paramsIndex = 1;
            for(Object p : params){
                pst.setObject(paramsIndex++, p);
            }
            rs = pst.executeQuery();
            while(rs.next()){
                return Long.valueOf(rs.getLong(1));
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    @SuppressWarnings("unused")
	public int queryBackUp(String sql){
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    //插入
    public boolean insert(String sql,Object...params){
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement(sql);
            //处理将数据插入占位符
            int paramsIndex = 1;
            for(Object p : params){
                pst.setObject(paramsIndex++, p);
            }
            //执行sql语句
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int update (String sql, Map<Integer, Object> param) throws SQLException {
    	System.out.println("connection ========> " + conn.toString());
    	PreparedStatement pst = null;
        pst = conn.prepareStatement(sql);
        for (Integer key : param.keySet()) {
        	pst.setObject(key, param.get(key));
        }
        return pst.executeUpdate();
    }
    
    //修改
    public boolean update(String sql,Object...params){
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement(sql);
            //处理将数据插入占位符
            int paramsIndex = 1;
            for(Object p : params){
                pst.setObject(paramsIndex++, p);
            }
            //执行sql语句
            int ret = pst.executeUpdate();
            System.out.println("======"+ret);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //删除
    public boolean delete(String sql,Object...params){
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement(sql);
            int paramsIndex = 1;
            for(Object p : params){
                pst.setObject(paramsIndex++, p);
            }
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //关闭资源
    public static void close(ResultSet rs,PreparedStatement pst,Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(pst!=null){
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pst = null;
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
