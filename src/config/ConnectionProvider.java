package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	// 데이터베이스 연결 해주는 친구
	// url, userid, password
	// url : 우리가 연결할 데이터베이스의 정보 ( 데이터베이스마다 다름 )
	// user : 우리가 데이터베이스에서 생성한 user 
	// password : 우리가 데이터베이스에서 생성한 user의 password
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String USERNAME = "minseok";
	private static final String PASSWORD = "1234";
	
	// 연결을 만들어서 주는 메소드
	// 드라이버 클래스 로딩 , Connection 객체 생성해서 리턴
	// 리턴 타입 : java.sql.Connection
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// 드라이버 클래스 로딩
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// Connection 객체 생성해서 리턴
		Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		
		return c;
		// return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
