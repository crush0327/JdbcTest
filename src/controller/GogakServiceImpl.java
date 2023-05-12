package controller;

import java.io.*;
import java.sql.*;

import dbConn.util.ConnectionSingletonHelper;
import model.GogakDTO;

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
		try {
			conn = ConnectionSingletonHelper.getConnection("oracle");
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
	public void menu() throws SQLException, IOException {
		
	}

	@Override
	public void selectAll(String className) throws SQLException, IOException {

	}

	@Override
	public void insert(String className) throws SQLException, IOException {
		System.out.println("GNO : ");
		String gno = BR.readLine();

		System.out.println("GNAME : ");
		String gname = BR.readLine();

		System.out.println("JUMIN : ");
		String jumin = BR.readLine();

		System.out.println("POINT : ");
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

	}

	@Override
	public void selectByGno(String className) throws SQLException, IOException {

	}
}
