package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Message;
import service.MessageService;

public class MessageServlet extends HttpServlet {
	
	private MessageService service;
	
	public MessageServlet() {
		service = new MessageService(); // 생성자에서 서비스 객체 생성
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doProc(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doProc(req, resp);
	}
	
	protected void doProc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 여기서 모두 처리
		// 인코딩 처리
		req.setCharacterEncoding("utf-8");
		
		// 페이징 처리
		/*
		 * 처리해야할 요청
			 /write : 메세지 작성
			 /messageList : 메시지 목록 화면 요청
			 /pwCheck : 비밀번호를 확인해주는 요청
		 * 
		 */
		
		String contextPath = req.getContextPath(); // 이 애플리케이션의 기본주소
		String requestURI = req.getRequestURI(); // 요청을 받은 주소 
		
		// 기본 주소 + 요청 = 요청을 받은 주소
		if(requestURI.equals(contextPath + "/write")) {
			// 메세지 작성 요청
			// form 에서 입력한 값 파라미터로 추출
			String name = req.getParameter("guestName");
			String password = req.getParameter("password");
			String message = req.getParameter("message");
			
			Message mes = new Message();
			mes.setName(name);
			mes.setPassword(password);
			mes.setMessage(message);
			
			// 서비스의 메시지 삽입 기능 실행
			boolean result = service.writeMessage(mes);
			
			if(result == true) {
				req.setAttribute("msg", "정상 등록되었습니다.");
			} else {
				req.setAttribute("msg", "등록 실패하였습니다.");
			}
			
			req.setAttribute("url", "messageList");
			req.getRequestDispatcher("result.jsp").forward(req, resp);
		} else if (requestURI.equals(contextPath + "/messageList")) {
			// 메세지 목록 화면 요청
			//List<Message> messageList = service.getAllMessages();
			
			// request의 속성에 messageList 담기
			// 왜 request 에 담나요? 요청 한번만 처리하고 데이터를 세션에
			// 유지할 필요가 없기 때문에
			//req.setAttribute("messageList", messageList );
			
			// 요청 정보를 계속 이어 나갈 것이므로 forward 사용
			//req.getRequestDispatcher("messageList.jsp").forward(req, resp);
			
			// classNotFound exception : servlet ? jdbc oracle ?
			// servlet => servlet 클래스 파일을 못찾는것. => web.xml 오타
			// jdbc oracle => ConnectionProvider, WebContent/WEB-INF/lib 폴더 밑 
			// ojdbc 라이브러리 넣어줬나 확인
			
			// 페이지 처리 시작 
			int pageNumber = 1;
			String strPageNumber = req.getParameter("page");
			// 우리가 전에 저장한 페이지 정보가 있는지 확인
			
			if(strPageNumber != null) {
				// 우리가 전에 저장한 페이지 정보가 있다.
				pageNumber = Integer.parseInt(strPageNumber);
			} // 페이지 정보가 없다면 기본값인 1페이지가 현재 페이지가 될것입니다.
			
			// 페이지 출력을 위해 우리가 몽땅 집어넣었던 Map을 가져온다.
			Map<String, Object> viewData = service.getMessageList(pageNumber);
			
			req.setAttribute("viewData", viewData);
			req.getRequestDispatcher("messageList.jsp").forward(req, resp);
			
		} else if (requestURI.equals(contextPath + "/pwCheck")) {
			// 게시글의 id, 비밀번호가 일치하는지 비교
			String id = req.getParameter("id");
			String password = req.getParameter("password");
			
			// 문자열을 int 타입으로 바꾸기
			int idnum = Integer.parseInt(id);
			
			// service를 통해 메세지 삭제 기능 실행
			boolean result = service.deleteMessage(idnum, password);
			
			// result.jsp 에 전달할 메세지
			String msg = "삭제 실패하였습니다.";
			if(result) {
				// 삭제 성공시 result.jsp에 전달할 메시지를 바꿔주면 된다.
				msg = "성공적으로 삭제 되었습니다.";
			}
			// 결과 페이지에서 알람으로 띄워줄 문장
			req.setAttribute("msg", msg);
			// 결과 페이지에서 알람을 보고 난 뒤에 이동할 페이지 주소
			req.setAttribute("url", "messageList");
			
			// 그 결과를 result.jsp를 통해 알려주면 됩니다.
			req.getRequestDispatcher("result.jsp").forward(req, resp);
			
		} else if (requestURI.equals(contextPath + "/confirmDelete")) {
			// confirmDelete.jsp 로 화면 이동
			// 파라미터 정보를 유지한채로 이동해야하기 때문에 forward사용
			req.getRequestDispatcher("confirmDelete.jsp").forward( req, resp);
		}
		
	}
	
}
