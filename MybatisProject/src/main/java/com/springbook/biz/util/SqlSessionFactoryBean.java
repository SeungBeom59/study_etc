package com.springbook.biz.util;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryBean {
	
	private static SqlSessionFactory sessionFactory = null;
	
	static {
		try {
			if(sessionFactory == null) {
				// config파일을 읽기 위해 입력 스트림 생성하여 읽고 이를 이용해 SqlSessionFactory 객체를 만들어낸다.
				Reader reader = Resources.getResourceAsReader("sql-map-config.xml");
				sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 위에서 만들어낸 SqlSessionFactory 객체로 부터 SqlSession을 얻어낸다. 
	public static SqlSession getSqlSessionInstance() {
		return sessionFactory.openSession();
	}
}
