package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import dbConn.util.ConnectionHelper;
import model.GogakDTO;

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
    public void connect(String dsn) {
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
    	rs = stmt.executeQuery("SELECT * FROM " + className);

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= count; i++) {
				switch (rsmd.getColumnType(i)) {
				case Types.NUMERIC:
				case Types.INTEGER:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getInt(i) + " ");
					break;
				case Types.FLOAT:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getFloat(i) + " ");
					break;
				case Types.DOUBLE:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getDouble(i) + " ");
					break;
				case Types.CHAR:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getString(i) + " ");
					break;
				case Types.DATE:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getDate(i) + " ");
				default:
					System.out.println(rsmd.getColumnName(i) + " : " + rs.getString(i) + " ");
					break;
				}
			}
			System.out.println();
		}
    }

    @Override
    public void insert(String className) throws IOException {
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
    public void update(String className) throws IOException {

    }

    @Override
    public void delete(String className) throws IOException {

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
		pstmt = conn.prepareStatement("select * from " + className + " where gno =? ");
		System.out.print("검색할 사람의 GNO(고객번호)을 입력: ");
		int gno = Integer.parseInt(BR.readLine());
		pstmt.setInt(1, gno);
		rs = pstmt.executeQuery();
		ArrayList<GogakDTO> list = new ArrayList();
		while (rs.next()) {
			GogakDTO gogak = new GogakDTO();
			gogak.setGno(rs.getInt("GNO"));
			gogak.setGname(rs.getString("GNAME"));
			gogak.setJumin(rs.getString("JUMIN"));
			gogak.setPoint(rs.getInt("POINT"));
			list.add(gogak);
		}

		if (list.size() == 0) {
			System.out.println("검색된 고객번호가 없습니다.");
			System.out.println();
			return;
		}

		for (GogakDTO gogak : list) {
			System.out.println("GNO: " + gogak.getGno());
			System.out.println("GNAME: " + gogak.getGname());
			System.out.println("JUMIN: " + gogak.getJumin());
			System.out.println("POINT: " + gogak.getPoint());
			System.out.println();
		}
	}
}
