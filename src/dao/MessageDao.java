package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionProvider;
import model.Message;

public class MessageDao {
	
	
	// �޼��� ����(����)
	public int insertMessage(Message message) {
		int result = 0;
		// ������ : create sequence message_seq start with 1 increment by 1 maxvalue 9999999 cycle;
		String sql = "insert into message values(message_seq.nextval, ?, ?, ?)";
		
		//���� ��ü
		Connection conn = null;
		// sql ���� ��ü
		PreparedStatement pstmt = null;
		
		try {
			// ���� ��������
			conn = ConnectionProvider.getConnection();
			// ���ῡ�� sql ���� ��ü�� pstmt ��������
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, message.getPassword());
			pstmt.setString(2, message.getName());
			pstmt.setString(3, message.getMessage());
			result = pstmt.executeUpdate();
			// executeUpdate() : ��� ó���� �ʿ���� sql�� ����
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	// �޼��� ����
	public int deleteMessage(int id) {
		int result = 0;
		String sql ="delete from message where id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate(); // �� sql ���๮�� ���� ��������Ѵ�.
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// �޼��� �ϳ� ��������
	public Message selectOne(int id) {
		Message result = null;
		String sql = "select * from message where id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// ó���� ����� ���� ���
		ResultSet rs = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			// ��� ó���� �ʿ��� ���� executeQuery()
			
			// id �� primary key�ϱ� ����� ������ 1��
			if(rs.next()) {
				result = new Message(); // �̰� �츮�� ��ȯ�� Message ��ü
				result.setId(rs.getInt("id"));
				result.setName(rs.getString("name"));
				result.setPassword(rs.getString("password"));
				result.setMessage(rs.getString("message"));
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	// �޼��� ���� ��������
	public List<Message> selectAll() {
		List<Message> result = new ArrayList<Message>();
		String sql = "select * from message order by id";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			// ����� ���� ���ϼ��� �ִ�.
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setName(rs.getString("name"));
				message.setPassword(rs.getString("password"));
				message.setMessage(rs.getString("message"));
				// �츮�� ��ȯ�� list�� �־���� �˴ϴ�.
				result.add(message);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// �������� ����ؼ� ������ ��������
	public List<Message> selectList(int firstRow, int endRow){
		// firstRow ==> ��ü ���� �������� �츮�� �������� ������ ���� ��ȣ
		// endRow ==> �������� ������ ��
		// firstRow : 1, endRow :10 ==> 1����� 10���� �����´�.
		// �����͸� �������� ����? �ֽŲ����� ���������� ���ı����� ���� �ؾ��ұ�?
		// id���� ==> ���߿� �����ɼ��� ū��
		// ������������ �ϸ� ū������ �����´�. ===> desc�� ����ؼ� �����´�.
		String sql = "select * \r\n" + 
				"from (select rownum as rnum, m.id, m.password, m.name, m.message\r\n" + 
				"        from (select id, password, name, message \r\n" + 
				"                from message order by id desc) m\r\n" + 
				"                )\r\n" + 
				"where rnum between ? and ?";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		List<Message> result = new ArrayList<>();
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, firstRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setName(rs.getString("name"));
				message.setPassword(rs.getString("password"));
				message.setMessage(rs.getString("message"));
				// ����Ʈ�� �߰� �� ���ּ���.
				result.add(message);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	//�� �޼����� ������ ���ϴ� �޼ҵ�
	public int selectCount() {
		int result = 0;
		String sql = "select count(*) from message";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
				// ����� ù��° �÷� �� ��������
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null)
					conn.close();
				if(pstmt!=null)
					pstmt.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		return result;
	}
}
