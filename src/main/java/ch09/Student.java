package ch09;

import java.sql.Date;

/* 1. ERD 설계
	2. DB에서 테이블, 데이터 생성
	3. 엔티티 클래스 생성	(엔티티 클래스는 DB에서의 table에 해당하는 클래스이다)
	4. 필요한 라이브러리 다 넣어주기(빌드툴 이용 혹은 [WEB-INF]-[라이브러리]
	5. 뷰, 모델, 컨트롤러 역할을 하는 파일들 만들기(파일명만)
	6. DAO 클래스 만들기
	7. 뷰, 컨트롤러 작성
*/

//엔티티 클래스는 데이터베이스와 대응된다. 컬럼명 = 속성
public class Student {
	private int id;
	private String username;
	private	String univ;
	private Date birth;	//import를 하는데 java.sql로 해야한다
	private String email;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUniv() {
		return univ;
	}
	public void setUniv(String univ) {
		this.univ = univ;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
