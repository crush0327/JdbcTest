package controller;

import java.io.*;
import java.sql.*;

public class GogakServiceImpl implements GogakService {

    private static final GogakService INSTANCE = new GogakServiceImpl();
    static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    static final BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(System.out));
    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement pstmt = null;
    static ResultSet rs = null;

    private static GogakService getInstance() {
        return INSTANCE;
    }

    @Override
    public void connect(String dsn, String uid, String pwd) throws SQLException {

    }

    @Override
    public void close() {

    }

    @Override
    public void menu() throws IOException, SQLException {

    }

    @Override
    public void selectAll(String className) throws SQLException, IOException {

    }

    @Override
    public void insert(String className) throws IOException {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete(String className) throws IOException {

    }

    @Override
    public void selectByGno(String className) throws SQLException, IOException {

    }
}
