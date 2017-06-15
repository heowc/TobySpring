package com.tistory.tobyspring.dao.jdbc;

import com.tistory.tobyspring.dao.statement.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stat) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stat.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            // PrepareStatement 객체 닫기
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }

            // Connection 객체 닫기
            if (c != null) { try { c.close(); } catch (SQLException e) {} }
        }
    }
}
