package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJdbcRepository {

    //    spring이 만들어주고(우리가 new 키워드로 생성 불가능 => final 사용할 필요 없음) 다형성(만들 필요도 없고, 내가 만든 것도 아니니까)을 사용할 필요가 없으니까 그냥 Autowired를 사용
//    DataSource는 DB와 JDBC에서 사용하는 DB연결 드라이버 객체
//    application.yml에 설정한 DB정보를 사용하여 DataSource객체 싱글통 생성
    @Autowired
    private DataSource dataSource;

//    jdbc의 단점
//    1. raw 쿼리이기 때문에 오타가 나도 디버깅 어려움
//    2. 데이터 추가시, 매개변수와 컬럼의 매핑을 직접 해줘야 함
//    3. 데이터 조회시, 객체 조립을 직접 해줘야 함

    public void save(Author author) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "insert into author(name, email, password) values(?, ?, ?)";
//            PreparedStatement 객체로 만들어서 실행가능한 상태로 만드는 것.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, author.getName());
            ps.setString(2, author.getEmail());
            ps.setString(3, author.getPassword());
            ps.executeUpdate(); // 추가, 수정의 경우는 executeUpdate, 조회는 executeQuery
        } catch (SQLException e) {
//            uncheck 예외는 스프링에서 트랜젝션 상황에서 롤백의 기준이 된다.
            throw new RuntimeException(e);
        }
    }

    public List<Author> findAll() {
        return null;
    }

    public Optional<Author> findById(Long id) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where id=?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next(); // 처음에는 컬럼명을 가르키고 있으니까

            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");

            author = new Author(id, name, email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(author);
    }

    public Optional<Author> findByEmail(String email) {
        Author author = null;

        try {
            Connection connection = dataSource.getConnection();

            String sql = "select * from author where email=?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                author = new Author(id, name, email, password);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(author);
    }

    public void delete(Long id) {
    }
}
