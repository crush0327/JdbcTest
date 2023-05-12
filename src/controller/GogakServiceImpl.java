package controller;

import dbConn.util.ConnectionHelper;
import model.GogakDTO;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GogakServiceImpl implements GogakService {

	private static final GogakService INSTANCE = new GogakServiceImpl();
	private static final Logger LOGGER = Logger.getLogger(GogakServiceImpl.class.getName());
	private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
	private static final BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(System.out));
	private static Connection conn = null;
	private static Statement stmt = null;
	private static PreparedStatement pstmt = null;
	private static ResultSet rs = null;

	public static GogakService getInstance() {
		return INSTANCE;
	}

	@Override
	public void connect(String dsn) {
		try {
			conn = ConnectionHelper.getConnection(dsn);
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			LOGGER.info(String.format("%s JDBC SUCCESS%n", dsn));
		} catch (Exception e) {
			LOGGER.severe(String.format("%s JDBC FAIL%n", dsn));
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
			LOGGER.info("JDBC CLOSED");
		} catch (Exception e) {
			LOGGER.severe("FATAL ERROR OCCURED");
			e.printStackTrace();
		}
	}

	@Override
	public void info() {
		StringBuilder sb = new StringBuilder();
		sb.append("-=-=-=-=-= JDBC Query =-=-=-=-=-\n");
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
			info();
			switch (BR.readLine().trim()) {
			case "0":
				System.out.print("ROLLBACK 하시겠습니까(Y/N)?");
				if (BR.readLine().trim().equalsIgnoreCase("Y")) {
					conn.rollback(); // 예외 발생
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
				update(dto.getClassName());
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
					conn.commit(); // 예외 발생
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
					BW.write(String.format("%s: %d%n", rsmd.getColumnName(i), rs.getInt(i)));
					break;
				case Types.FLOAT:
					BW.write(String.format("%s: %f%n", rsmd.getColumnName(i), rs.getFloat(i)));
					break;
				case Types.DOUBLE:
					BW.write(String.format("%s: %f%n", rsmd.getColumnName(i), rs.getDouble(i)));
					break;
				case Types.CHAR:
					BW.write(String.format("%s: %s%n", rsmd.getColumnName(i), rs.getString(i)));
					break;
				case Types.DATE:
					BW.write(String.format("%s: %s%n", rsmd.getColumnName(i), rs.getDate(i)));
					break;
				default:
					BW.write(String.format("%s: %s%n", rsmd.getColumnName(i), rs.getString(i)));
					break;
				}
			}
			BW.write("\n");
		}
		BW.flush();
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
		System.out.println("수정할 고객 번호를 입력해주세요.");

		String str = BR.readLine();

		System.out.println("변경할 번호를 입력하세요");
		String gno = BR.readLine();
		System.out.println("변경할 고객 이름을 입력하세요");
		String gname = BR.readLine();
		System.out.println("변경할 고객 주민번호를 입력하세요.");
		String jumin = BR.readLine();
		System.out.println("변경할 고객 포인트를 입력하세요.");
		String point = BR.readLine();

		String sql = "update gogak set GNO =? , GNAME =? , JUMIN =? , POINT =? where GNO= ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, gno);
			pstmt.setString(2, gname);
			pstmt.setString(3, jumin);
			pstmt.setString(4, point);
			pstmt.setString(5, str);

			pstmt.executeUpdate();

			System.out.println("데이터 수정되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String className) throws IOException {

		System.out.print("GNO: ");
		int gno = Integer.parseInt(BR.readLine());

		try {
			pstmt = conn.prepareStatement("DELETE FROM " + className + " WHERE gno = ?");
			pstmt.setInt(1, gno);

			int res = pstmt.executeUpdate();
			System.out.printf("%d개 데이터가 삭제되었습니다.%n%n", res);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void selectByGno(String className) throws SQLException, IOException {
		pstmt = conn.prepareStatement("SELECT * FROM " + className + " WHERE gno =? ");
		System.out.print("검색할 사람의 GNO(고객번호)을 입력: ");
		int gno = Integer.parseInt(BR.readLine());
		pstmt.setInt(1, gno);
		rs = pstmt.executeQuery();
		ArrayList<GogakDTO> list = new ArrayList<>();
		while (rs.next()) {
			GogakDTO gogak = new GogakDTO();
			gogak.setGno(rs.getInt("GNO"));
			gogak.setGname(rs.getString("GNAME"));
			gogak.setJumin(rs.getString("JUMIN"));
			gogak.setPoint(rs.getInt("POINT"));
			list.add(gogak);
		}

		if (list.isEmpty()) {
			System.out.println("검색된 고객번호가 없습니다.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (GogakDTO gogak : list) {
			sb.append("GNO: ").append(gogak.getGno()).append("\n");
			sb.append("GNAME: ").append(gogak.getGname()).append("\n");
			sb.append("JUMIN: ").append(gogak.getJumin()).append("\n");
			sb.append("POINT: ").append(gogak.getPoint()).append("\n");
		}
		System.out.println(sb);
	}
}
