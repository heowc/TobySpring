package com.tistory.tobyspring.dao.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        return c.prepareStatement("DELETE FROM USERS");
    }
}
