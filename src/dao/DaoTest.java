package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.Test;

import model.Message;

public class DaoTest {
	
	@Test
	public void doTest() {
		MessageDao dao = new MessageDao();
//		
		for(int i=100; i<300; i++) {
			Message m = new Message();
			m.setPassword("123");
			m.setName("name" + (i + 1));
			m.setMessage("HELLO MESSAGE " + i);
			dao.insertMessage(m);
		}
//		
//		List<Message> list = dao.selectAll();
//		assertEquals(20, list.size());
//		
//		Message m1 = dao.selectOne(5);
//		assertNotNull(m1);
//		// assert + not null
//		// (m1) ==> m1 객체가 null이 아닐것이다 라고 예상
//		
//		int result = dao.deleteMessage(5);
//		assertEquals(1, result);
//		// assert ==> 단언하다
//		// equals ==> 같다
//		// (1, result) ==> 1하고 result 값이 같을 것이다 라고 예상
	}
	
}
