package com.qiguan.webchat.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午5:03:33  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public class PoolManager {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/webchat?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
	private static String Name = "root";
	private static String Password = "root";

	@SuppressWarnings("rawtypes")
	private static Class driverClass = null;
	private static ObjectPool connectionPool = null;

	public PoolManager() {

	}

	/**
	 * 装配配置文件
	 */
	private static void loadProperties() {
		try {
			InputStream stream = PoolManager.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties props = new Properties();
			props.load(stream);
			driver = props.getProperty("driver");
			url = props.getProperty("url");
			Name = props.getProperty("username");
			Password = props.getProperty("password");
		} catch (FileNotFoundException e) {
			System.out.println("读取配置文件异常");
		} catch (IOException ie) {
			System.out.println("读取配置文件时IO异常");
		}
	}

	/**
	 * 初始化数据源
	 */
	private static synchronized void initDataSource() {
		if (driverClass == null) {
			try {
				driverClass = Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 连接池启动
	 */
	@SuppressWarnings("unused")
	private static void startPool() {
		loadProperties();
		initDataSource();
		if (connectionPool != null) {
			destroyPool();
		}
		try {
			connectionPool = new GenericObjectPool(null);
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, Name, Password);
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,
					connectionPool, null, null, false, true);
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool("dbpool", connectionPool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接池
	 */
	private static void destroyPool() {
		try {
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.closePool("dbpool");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得连接池中的连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		if (connectionPool == null)
			startPool();
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:dbpool");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取连接 getConnection
	 * 
	 * @param name
	 * @return
	 */
	public static Connection getConnection(String name) {
		return getConnection();
	}

	/**
	 * 释放连接 freeConnection
	 * 
	 * @param conn
	 */
	private static void freeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放连接 freeConnection
	 * 
	 * @param name
	 * @param con
	 */
	public static void freeConnection(String name, Connection con) {
		freeConnection(con);
	}
}