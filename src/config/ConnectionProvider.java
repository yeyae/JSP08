package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	// �����ͺ��̽� ���� ���ִ� ģ��
	// url, userid, password
	// url : �츮�� ������ �����ͺ��̽��� ���� ( �����ͺ��̽����� �ٸ� )
	// user : �츮�� �����ͺ��̽����� ������ user 
	// password : �츮�� �����ͺ��̽����� ������ user�� password
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String USERNAME = "minseok";
	private static final String PASSWORD = "1234";
	
	// ������ ���� �ִ� �޼ҵ�
	// ����̹� Ŭ���� �ε� , Connection ��ü �����ؼ� ����
	// ���� Ÿ�� : java.sql.Connection
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// ����̹� Ŭ���� �ε�
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// Connection ��ü �����ؼ� ����
		Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		
		return c;
		// return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
