package ch09;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/*
   Servlet implementation class StudentController
 */

//StudentController s = new StudentController(); 서블릿 객체 생성은 톰캣에서 해준다. (톰캣 = WAS = 서버)
@WebServlet("/studentControl")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StudentDAO dao;

	//init은 무조건 한번만 실행된다 (객체 생성은 한번만 된다)
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);	//서블릿 초기화
		dao = new StudentDAO();	//StudentDAO 객체가 딱 한번만 만들어진다. -> 공유해서 쓸 수 있다.
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// request : 뷰에서 넘어온 데이터, 정보가 들어있다.
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");	//insert
		
		System.out.println("service --action" + action);
		
		System.out.println("service --" + request.getParameter("action") + ":" + request.getParameter("email") + ":" +request.getParameter("univ"));
		String view = "";
		
		if (action == null) {	//만약 action(정보가 들어 있는 주소)의 값이 null이면
			//request를 또 한다 (list로 페이지를 이동시킨다) => action=list라는 의미는 action이 있는 곳이 list 페이지로 이동하는 의미이다
			System.out.println("action null 실행");
			getServletContext().getRequestDispatcher("/studentControl?action=list").forward(request, response);
		} else {
			switch(action) {	//action일때 스위치문 작동
				//action이 "list"일때
				case "list" :	view = list(request, response); break;
				//action이 "insert"일때/*34/*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
				case "insert" :	view = insert(request, response); break;
			}
			
			/*		//request와 response 객체를 매개변수로 넘겨준다
			view = insert(request, response);  	*/
			
			//getServletContext(ServletContext를 얻어옴)
			//getRequestDispatcher(이동 할 페이지 경로 지정)
			getServletContext().getRequestDispatcher("/ch09/" + view).forward(request, response);
			//forward(페이지를 이동시킨다, 내부에서 이동이 되므로 주소가 변하지 않는다)
			//forward(값을 전부다 끌어옴) (원래는 값이 이동하면 지금의 request와 response값만 이동하는데 전에 있던 흔적 값들도 함께 이동)
		}
		
		

	}
	
	public String list(HttpServletRequest request, HttpServletResponse response) {
		//setAttribute는 key, value로 hashMap처럼 쓰인다
		System.out.println("list실행");
		request.setAttribute("students", dao.getAll());	//setAttribute의 key값은 jstl의 items="${students}이다
		return "studentInfo.jsp";
	}
	
	//insert 메소드는 DAO의 insert 메소드와 값이 같다. 결국 자바의 값과 DB의 값이 같게 연동을 시켜주는 것이다
	//request의 데이터를 받는다 -> DAO에 있는 insert 실행(DB에 insert가 됨) -> 페이지명(studentInfo.jsp)으로 리턴
	public String insert(HttpServletRequest request, HttpServletResponse response) {
		Student s = new Student();

		
		try {
			BeanUtils.populate(s, request.getParameterMap());	  //-> 아래의 코드 역할을 대신 해준다

/*
		s.setUserName(request.getParameter("username"));
		s.setEmail(request.getParameter("email"));
		s.setUniv(request.getParameter("univ"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd");
		Date date = (Date)formatter.parse(request.getParameter("birth"));
		s.setBirth(request.getParameter("birth"));	//getParameter는 String 타입이기 때문에 Date 타입으로 바꿔야한다
		*/
		} catch (Exception e) {
			
		}
		// 값이 들어가는지 확인
		System.out.println( "control insert" + request.getParameter("username") + ":" + request.getParameter("email") + ":" +request.getParameter("univ"));
		dao.insert(s);	//컨트롤러는 DAO한테 있는 메소드를 사용한다.
		return "studentInfo.jsp";
	}

	
}
