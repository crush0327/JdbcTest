package controller;

import java.io.IOException;
import java.sql.SQLException;

public interface GogakService {

    void connect(String dsn);

    void close();

    void info() throws IOException;

    void menu() throws IOException, SQLException;

    void selectAll(String className) throws IOException, SQLException;

    void insert(String className) throws IOException, SQLException;

    void update(String className) throws IOException, SQLException;

    void delete(String className) throws IOException, SQLException;

    void selectByGno(String className) throws IOException, SQLException;
}
