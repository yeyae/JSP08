package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.MessageDao;
import model.Message;

public class MessageService {
	// 서블릿에서 직접 Dao 를 사용하게 하지 않고
	// service를 거쳐서 사용하도록
	// 그 중간 로직을 분리
	// dao : 데이터베이스에 쿼리를 날리는 일만 함
	// servlet : 페이지 관련 처리 ( 사용자에게 받은 요청 처리 )
	// service : servlet에서 데이터베이스 관련 필요한 기능을 메소드로 정의
	
	private MessageDao messageDao;
	
	// 한 페이지에 표시할 메세지 개수
	private static final int NUM_OF_MESSAGE_PER_PAGE = 10;
	// 한번에 표시할 네비게이션의 개수 ( 페이지 버튼의 개수 )
	private static final int NUM_OF_NAVI_PAGE = 10;
	
	public MessageService() {
		messageDao = new MessageDao();
	}
	
	// 모든 메세지 목록 가져오기
	public List<Message> getAllMessages() {
		return messageDao.selectAll();
	}
	
	// 메세지 하나 가져오기
	public Message getMessage(int id) {
		return messageDao.selectOne(id);
	}
	
	// 메세지 쓰기
	public boolean writeMessage(Message message) {
		// 메세지 쓰기 (삽입) 이 성공하면 ? true 리턴
		
		// 메세지 삽입이 실패하면 false 리턴
		int result = messageDao.insertMessage(message);
		
		if(result == 1) {
			return true;
		} else {
			return false;
		}
		
		// 성공과 실패 여부는 insertMessage() 메소드의 리턴 값이
		// 0이냐 아니냐
		// 리턴값이 0이다. ==> 데이터베이스에 삽입된(영향을 받은) 행수가 0
		// 0 이 아니다 ==> 데이터베이스에 삽입된 행수가 0이 아니다. ==> 삽입이 됬다. 
	}
	
	// 메세지 삭제하기
	public boolean deleteMessage(int id, String password) {
		// 메세지 한개를 가져옵니다. 어떻게? id로
		Message message = messageDao.selectOne(id);
		
		// 해당하는 id 의 메세지가 있으면 그때 비밀번호가 같은지를 비교
		// 같으면 삭제후 결과를 리턴
		if(message != null && message.getPassword().equals(password)) {
			int result = messageDao.deleteMessage(id);
			
			return true;
		}
		// 비밀번호가 다르다거나, 해당하는 id의 메세지가 없다 ==> return false
		
		return false;
	}
	
	// 총 페이지 개수 구하기
	private int calPageTotalCount(int totalCount) {
		int pageCount = 0;
		// 총 페이지의 개수 를 반환
		/*
		 *  메세지 수 : 1~10  페이지수 : 1
		 *  메세지 수 : 11~20 페에지수 : 2
		 *  100 10
		 *  200 20
		 *  333 33.33
		 *  
		 *  Math.ceil() 천장함수 , 올림
		 */
		// totalCount : 총 메세지 개수
		if(totalCount != 0) {
			pageCount = (int)Math.ceil((double)totalCount / NUM_OF_MESSAGE_PER_PAGE);
		}
		
		return pageCount;
	}
	
	public int getStartPage(int pageNum) {
		int startPage = 0;
		//pageNum : 현재 페이지 번호
		//현재 페이지번호가 6
		//현재 페이지 버튼을 몇번부터 몇번까지 나타내야 될까??
		//1 ~ 10 번까지
		// 현재 페이지 번호가 16 번이면 11 ~ 20
		// 여기서 우리가 구하는거는 11
		startPage = ((pageNum-1)/NUM_OF_NAVI_PAGE)*NUM_OF_NAVI_PAGE + 1;
		// 20
		// (19/10) * 10 + 1 = 11 
		// 21
		// (20/10) * 10 + 1 = 21
		// 
		
		return startPage;
	}
	
	public int getEndPage(int pageNum) {
		int endPage = 0;
		endPage = (((pageNum-1)/NUM_OF_NAVI_PAGE)+1) * NUM_OF_NAVI_PAGE;
		// 6
		// (5/10) = 0 + 1  * 10 = 10
		// 16
		// (15/10) = 1 + 1 * 10 = 20
		// 20
		// (19/10) = 1 + 1 * 10 = 20
		// 21
		// (20/20) = 2 + 1 * 10 = 30
		
		return endPage;
	}
	
	public Map<String, Object> getMessageList(int pageNumber) {
		// 현재 페이지에 표시될 메세지 목록 가져오기
		
		// 화면을 표시하기 위한 데이터들의 모음
		Map<String ,Object> viewData = new HashMap<>();
		
		int totalCount = messageDao.selectCount(); // 메세지 전체 개수 가져오기
		int firstRow = 0;
		int endRow = 0;
		/*				firstRow    endRow
		 * 1 페이지 :      1           10
		 * 2 페이지 :      11          20
		 * 3 페이지 :      21          30
		 * 
		 */
		firstRow = (pageNumber-1)*NUM_OF_MESSAGE_PER_PAGE + 1;
		endRow = pageNumber * NUM_OF_MESSAGE_PER_PAGE;
		
		List<Message> messageList = messageDao.selectList(firstRow, endRow);
		
		// Map에 화면에 필요한 데이터들 몽땅 넣기
		viewData.put("messageList", messageList);
		
		// 총 페이지 개수
		viewData.put("pageTotalCount", calPageTotalCount(totalCount));
		
		// 시작 페이지
		viewData.put("startPage", getStartPage(pageNumber));
		
		// 마지막 페이지
		viewData.put("endPage", getEndPage(pageNumber));
		
		// 현재 페이지 번호
		viewData.put("currentPage", pageNumber);
		
		return viewData;
	}
}
