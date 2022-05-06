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
	
	
	// 메세지 생성(삽입)
	public int insertMessage(Message message) {
		int result = 0;
		// 시퀀스 : create sequence message_seq start with 1 increment by 1 maxvalue 9999999 cycle;
		String sql = "insert into message values(message_seq.nextval, ?, ?, ?)";
		
		//연결 객체
		Connection conn = null;
		// sql 실행 객체
		PreparedStatement pstmt = null;
		
		try {
			// 연결 가져오기
			conn = ConnectionProvider.getConnection();
			// 연결에서 sql 실행 객체인 pstmt 가져오기
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, message.getPassword());
			pstmt.setString(2, message.getName());
			pstmt.setString(3, message.getMessage());
			result = pstmt.executeUpdate();
			// executeUpdate() : 결과 처리가 필요없는 sql문 실행
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
	
	// 메세지 삭제
	public int deleteMessage(int id) {
		int result = 0;
		String sql ="delete from message where id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate(); // 꼭 sql 실행문을 실행 시켜줘야한다.
			
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
	
	// 메세지 하나 가져오기
	public Message selectOne(int id) {
		Message result = null;
		String sql = "select * from message where id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// 처리할 결과가 있을 경우
		ResultSet rs = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			// 결과 처리가 필요한 경우는 executeQuery()
			
			// id 는 primary key니까 결과는 무조건 1개
			if(rs.next()) {
				result = new Message(); // 이게 우리가 반환할 Message 객체
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
	
	// 메세지 전부 가져오기
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
			
			// 결과가 여러 행일수도 있다.
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setName(rs.getString("name"));
				message.setPassword(rs.getString("password"));
				message.setMessage(rs.getString("message"));
				// 우리가 반환할 list에 넣어줘야 됩니다.
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
	
	// 페이지를 사용해서 데이터 가져오기
	public List<Message> selectList(int firstRow, int endRow){
		// firstRow ==> 전체 행을 기준으로 우리가 가져오기 시작할 행의 번호
		// endRow ==> 가져오는 마지막 행
		// firstRow : 1, endRow :10 ==> 1행부터 10까지 가져온다.
		// 데이터를 가져오는 기준? 최신꺼부터 가져오려면 정렬기준을 뭘로 해야할까?
		// id기준 ==> 나중에 생성될수록 큰값
		// 내림차순으로 하면 큰값부터 가져온다. ===> desc를 사용해서 가져온다.
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
				// 리스트에 추가 꼭 해주세요.
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
	
	//총 메세지의 개수를 구하는 메소드
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
				// 결과의 첫번째 컬럼 값 가져오기
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
