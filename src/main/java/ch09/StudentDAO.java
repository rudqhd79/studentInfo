package ch09;

import java.sql.*;
import java.util.*;

//DB와 자바를 연결해주는 중요한 역할
public class StudentDAO {
	Connection conn = null; // Connection는 데이터베이스랑 연결해주는 객체이다
	PreparedStatement pstmt; // DB의 쿼리문의 실행을 담당해주는 객체

	// 오라클과 관련된 상수값
	// (JDBC는 자바와 DB를 연결해주는 API)
	// ojdbc6.jar 파일은 오라클 DB와 연결해주는 것 (DB는 프로그램이 다양하다)
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver"; // DB의 경로(자바와 DB를 연결시켜주는 driver이다)
	final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // DB의 계정 (계정의 아이디와, 비밀번호)
	// 데이터베이스의 호스트 이름 : localhost
	// 데이터베이스의 포트 : 1521
	// SID : xe 라는 의미이다

	// DB 연결 메소드
	public void open() {
		try {
			// Class의 정적 메소드 forName()메소드
			// 드라이버 로드
			Class.forName(JDBC_DRIVER); // Class에 대한 정보를 가져온다
			// JDBC_URL은 DB의 계정 연결부분
			// getConnection에서는 (연결부분, 아이디, 비밀번호)를 적어준다
			conn = DriverManager.getConnection(JDBC_URL, "test", "test1234");
		} catch (Exception e) { // 다른 Exception과는 다르게 그냥 Exception은 최상위 객체
			e.printStackTrace();
		}
	}

	// DB 닫는 메소드
	public void close() {
		try {
			// pstmt, conn은 리소스(데이터를 읽고 쓰는 객체) 이므로 사용 후 반드시 닫아줘야 한다
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 학생 정보를 다 불러온다 (select * from Student);)
	public ArrayList<Student> getAll() {
		open(); // db오픈
		ArrayList<Student> students = new ArrayList<>(); // student 객체를 담을 리스트 준비

		try {
			// statement와 prepareStatement의 차이점은 캐시를 이용하여 사용한다는 점이다
			// statement는 캐시로 저장되지 않아 재사용하기에 부적합하다
			// 따라서 prepareStatement로 사용하는 것을 권장한다
			pstmt = conn.prepareStatement("select * from student"); // pstmt는 실행한다는 의미가 있을 뿐 아직은 동작하는 쿼리문이 적혀있지 않았다.
			// pstmt conn으로 DB에 연결하여 prepareStatement로 동작, (student 테이블의 정보를 모두 보여주는 것을 입력)

			// ResultSet : 데이터베이스 데이터를 받는 역할을 한다
			ResultSet rs = pstmt.executeQuery(); // 쿼리문 실행(select문으로)

			// re.next()는 더 이상 내용이 없으면 false를 리턴
			while (rs.next()) { // 한 행씩 값이 있는지 없는지 판단한다
				Student s = new Student();
				// student 클래스에 set메소드로 매개변수에 값 주기
				// *******rs의 함수는 rs가 boolean형으로 판단하게 해준다*******
				// 매개변수는 rs에 대해 id가 int면 true 아니면 false
				// 매개변수는 rs에 대해 univ가 String이면 true 아니면 false
				// 매개변수는 rs에 대해 birth가 Date면 true 아니면 false
				// 매개변수는 rs에 대해 email이 String이면 true 아니면 false
				s.setId(rs.getInt("id")); // Student 객체에 rs의 쿼리문에 id값에 해당하는 값을 넣는다
				s.setUsername(rs.getString("username")); // Student 객체에 rs의 쿼리문에 username값에 해당하는 값을 넣는다
				s.setUniv(rs.getString("univ")); // Student 객체에 rs의 쿼리문에 univ값에 해당하는 값을 넣는다
				s.setBirth(rs.getDate("birth")); // Student 객체에 rs의 쿼리문에 birth값에 해당하는 값을 넣는다
				s.setEmail(rs.getString("email")); // Student 객체에 rs의 쿼리문에 email값에 해당하는 값을 넣는다

				students.add(s); // ArrayList에 저장
			}
		} catch (SQLException e) {
			e.printStackTrace();
		//catch 절 다음에는 finally 블록이 이어지는데, 여기에는 앞서 try 블록에서 일어난 일에 관계없이 항상 실행이 보장되어야 할 뒷정리용 코드가 포함됩니다.
		} finally {
			close();
		}
		return students;
	}

	// 학생 정보를 입력
	public void insert(Student s) {
		open();
		// ? : 어떤 데이터가 들어올지 모른다
		//id_seq 시퀀스의 다음 값 (현재 시퀀스는 1씩 증가되게 되어있다)
		String sql = "insert into student values(id_seq.nextval, ?, ?, ?, ?)";

		//실행 확인
		System.out.println("dao insert :" + s.getUsername() + ":" + s.getUniv() + ":" + s.getBirth());
		// 실행할 쿼리문 입력
		// prepareStatement은 오라클 DB로 변환할때 String형을 varchar형으로 자동으로 변환해주는 것이다
		try {
			// DB로 데이터 변환 준비
			pstmt = conn.prepareStatement(sql);
			// DB에서 값 넣기 (insert문)
			// DB 테이블에 있는 열의 순서대로 넣어준다
			pstmt.setString(1, s.getUsername()); // pstmt.setString(값을 넣어줄 위치, 넣어줄 데이터);
			pstmt.setString(2, s.getUniv());
			pstmt.setDate(3, s.getBirth());
			pstmt.setString(4, s.getEmail());

			// DB에서 insert, delete, update문 사용 시
			pstmt.executeUpdate(); // executeUpdate는 DB에서 insert, delete, update 실행시 써주면 된다

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
