package controller;

import java.io.IOException;
import java.sql.SQLException;

public interface GogakService {

    void connect(String dsn, String uid, String pwd) throws SQLException;

    void close();

    void menu() throws IOException, SQLException;

    void selectAll(String className) throws SQLException, IOException;

    void insert(String className) throws IOException;

    void update();

    void delete(String className) throws IOException;

    void selectByGno(String className) throws SQLException, IOException;
}
