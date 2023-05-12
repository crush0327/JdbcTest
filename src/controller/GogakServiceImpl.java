package controller;

import db_conn.util.ConnectionHelper;
import model.GogakDTO;

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

    public static GogakService getInstance() {
        return INSTANCE;
    }

    @Override
    public void connect(String dsn) throws SQLException {
        try {
            conn = ConnectionHelper.getConnection("ORACLE");
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (rs != null)
                rs.close();

            if (stmt != null)
                stmt.close();

            if (pstmt != null)
                pstmt.close();

            if (conn != null)
                conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void info() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n-=-=-=-=-= JDBC Query =-=-=-=-=-\n");
        sb.append("\t0. ROLLBACK\n");
        sb.append("\t1. 전체 보기\n");
        sb.append("\t2. 레코드 삽입(추가)\n");
        sb.append("\t3. 레코드 수정\n");
        sb.append("\t4. 레코드 삭제\n");
        sb.append("\t5. 조건부 검색\n");
        sb.append("\t6. 프로그램 종료\n");
        sb.append("\t9. COMMIT\n");
        sb.append("\n원하는 메뉴를 선택하세요: ");
        System.out.print(sb);
    }

    @Override
    public void menu() throws SQLException, IOException {
        GogakDTO dto = new GogakDTO();
        while (true) {
            System.out.println();
            info();
            switch (BR.readLine().trim()) {
                case "0":
                    System.out.print("ROLLBACK 하시겠습니까(Y/N)?");
                    if (BR.readLine().trim().equalsIgnoreCase("Y")) {
                        conn.rollback();  // 예외 발생
                        selectAll(dto.getClassName());
                    }
                    break;
                case "1":
                    selectAll(dto.getClassName());
                    break;
                case "2":
                    insert(dto.getClassName());
                    selectAll(dto.getClassName());
                    break;
                case "3":
                    break;
                case "4":
                    delete(dto.getClassName());
                    break;
                case "5":
                    selectByGno(dto.getClassName());
                    break;
                case "6":
                    close();
                    System.out.println("프로그램을 종료합니다.");
                    return;
                case "9":
                    System.out.print("COMMIT 하시겠습니까(Y/N)?");
                    if (BR.readLine().trim().equalsIgnoreCase("Y")) {
                        conn.commit();  // 예외 발생
                        selectAll(dto.getClassName());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void selectAll(String className) throws SQLException, IOException {

    }

    @Override
    public void insert(String className) throws SQLException, IOException {
        System.out.print("GNO : ");
        String gno = BR.readLine();

        System.out.print("GNAME : ");
        String gname = BR.readLine();

        System.out.print("JUMIN : ");
        String jumin = BR.readLine();

        System.out.print("POINT : ");
        String point = BR.readLine();

        try {
            pstmt = conn.prepareStatement("INSERT INTO " + className + " VALUES(?, ?, ?, ?)");
            pstmt.setString(1, gno);
            pstmt.setString(2, gname);
            pstmt.setString(3, jumin);
            pstmt.setString(4, point);

            int result = pstmt.executeUpdate();

            System.out.println(result + "개 데이터가 추가 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String className) throws SQLException, IOException {

    }

    @Override
    public void delete(String className) throws SQLException, IOException {

        System.out.print("GNO: ");
        int gno = Integer.parseInt(BR.readLine());

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + className + " WHERE gno = ?");
            pstmt.setInt(1, gno);

            int res = pstmt.executeUpdate();
            System.out.printf("%d개 데이터가 삭제되었습니다.", res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectByGno(String className) throws SQLException, IOException {

    }
}
