package controller;

import java.io.IOException;
import java.sql.SQLException;

public interface GogakService {

    void connect(String dsn) throws SQLException;

    void close();

    void info();

    void menu() throws SQLException, IOException;

    void selectAll(String className) throws SQLException, IOException;

    void insert(String className) throws SQLException, IOException;

    void update(String className) throws SQLException, IOException;

    void delete(String className) throws SQLException, IOException;

    void selectByGno(String className) throws SQLException, IOException;
}
