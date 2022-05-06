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
		service = new MessageService(); // �����ڿ��� ���� ��ü ����
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
		// ���⼭ ��� ó��
		// ���ڵ� ó��
		req.setCharacterEncoding("utf-8");
		
		// ����¡ ó��
		/*
		 * ó���ؾ��� ��û
			 /write : �޼��� �ۼ�
			 /messageList : �޽��� ��� ȭ�� ��û
			 /pwCheck : ��й�ȣ�� Ȯ�����ִ� ��û
		 * 
		 */
		
		String contextPath = req.getContextPath(); // �� ���ø����̼��� �⺻�ּ�
		String requestURI = req.getRequestURI(); // ��û�� ���� �ּ� 
		
		// �⺻ �ּ� + ��û = ��û�� ���� �ּ�
		if(requestURI.equals(contextPath + "/write")) {
			// �޼��� �ۼ� ��û
			// form ���� �Է��� �� �Ķ���ͷ� ����
			String name = req.getParameter("guestName");
			String password = req.getParameter("password");
			String message = req.getParameter("message");
			
			Message mes = new Message();
			mes.setName(name);
			mes.setPassword(password);
			mes.setMessage(message);
			
			// ������ �޽��� ���� ��� ����
			boolean result = service.writeMessage(mes);
			
			if(result == true) {
				req.setAttribute("msg", "���� ��ϵǾ����ϴ�.");
			} else {
				req.setAttribute("msg", "��� �����Ͽ����ϴ�.");
			}
			
			req.setAttribute("url", "messageList");
			req.getRequestDispatcher("result.jsp").forward(req, resp);
		} else if (requestURI.equals(contextPath + "/messageList")) {
			// �޼��� ��� ȭ�� ��û
			//List<Message> messageList = service.getAllMessages();
			
			// request�� �Ӽ��� messageList ���
			// �� request �� �㳪��? ��û �ѹ��� ó���ϰ� �����͸� ���ǿ�
			// ������ �ʿ䰡 ���� ������
			//req.setAttribute("messageList", messageList );
			
			// ��û ������ ��� �̾� ���� ���̹Ƿ� forward ���
			//req.getRequestDispatcher("messageList.jsp").forward(req, resp);
			
			// classNotFound exception : servlet ? jdbc oracle ?
			// servlet => servlet Ŭ���� ������ ��ã�°�. => web.xml ��Ÿ
			// jdbc oracle => ConnectionProvider, WebContent/WEB-INF/lib ���� �� 
			// ojdbc ���̺귯�� �־��ᳪ Ȯ��
			
			// ������ ó�� ���� 
			int pageNumber = 1;
			String strPageNumber = req.getParameter("page");
			// �츮�� ���� ������ ������ ������ �ִ��� Ȯ��
			
			if(strPageNumber != null) {
				// �츮�� ���� ������ ������ ������ �ִ�.
				pageNumber = Integer.parseInt(strPageNumber);
			} // ������ ������ ���ٸ� �⺻���� 1�������� ���� �������� �ɰ��Դϴ�.
			
			// ������ ����� ���� �츮�� ���� ����־��� Map�� �����´�.
			Map<String, Object> viewData = service.getMessageList(pageNumber);
			
			req.setAttribute("viewData", viewData);
			req.getRequestDispatcher("messageList.jsp").forward(req, resp);
			
		} else if (requestURI.equals(contextPath + "/pwCheck")) {
			// �Խñ��� id, ��й�ȣ�� ��ġ�ϴ��� ��
			String id = req.getParameter("id");
			String password = req.getParameter("password");
			
			// ���ڿ��� int Ÿ������ �ٲٱ�
			int idnum = Integer.parseInt(id);
			
			// service�� ���� �޼��� ���� ��� ����
			boolean result = service.deleteMessage(idnum, password);
			
			// result.jsp �� ������ �޼���
			String msg = "���� �����Ͽ����ϴ�.";
			if(result) {
				// ���� ������ result.jsp�� ������ �޽����� �ٲ��ָ� �ȴ�.
				msg = "���������� ���� �Ǿ����ϴ�.";
			}
			// ��� ���������� �˶����� ����� ����
			req.setAttribute("msg", msg);
			// ��� ���������� �˶��� ���� �� �ڿ� �̵��� ������ �ּ�
			req.setAttribute("url", "messageList");
			
			// �� ����� result.jsp�� ���� �˷��ָ� �˴ϴ�.
			req.getRequestDispatcher("result.jsp").forward(req, resp);
			
		} else if (requestURI.equals(contextPath + "/confirmDelete")) {
			// confirmDelete.jsp �� ȭ�� �̵�
			// �Ķ���� ������ ������ä�� �̵��ؾ��ϱ� ������ forward���
			req.getRequestDispatcher("confirmDelete.jsp").forward( req, resp);
		}
		
	}
	
}
