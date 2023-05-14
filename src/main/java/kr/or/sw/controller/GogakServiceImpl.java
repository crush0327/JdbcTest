package kr.or.sw.controller;

import kr.or.sw.model.GogakDTO;
import kr.or.sw.util.ConnectionHelper;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GogakServiceImpl implements GogakService {

    private static final GogakService INSTANCE = new GogakServiceImpl();
    private static final Logger LOGGER = Logger.getLogger(GogakServiceImpl.class.getName());    // Logging for Important Information
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(System.out));
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;

    // Singtleton Pattern
    private GogakServiceImpl() {
    }

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
            BW.write("고객관리 시스템에 접속하신 것을 환영합니다.\n\n");
        } catch (SQLException e) {
            LOGGER.severe("SQLException Occured\n");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.severe("ClassNotFoundException Occured\n");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.severe("IOException Occured\n");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {

        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                conn.close();
            BW.write("프로그램을 종료합니다.\n");
            BW.flush();
            BW.close();
            LOGGER.info("JDBC CLOSED");
        } catch (SQLException e) {
            LOGGER.severe("SQLException Occured\n");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.severe("IOEException Occured\n");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void info() throws IOException {

        BW.write("-=-=-=-=-= JDBC Query =-=-=-=-=-\n" +
                "\t0. ROLLBACK\n" +
                "\t1. 전체 보기\n" +
                "\t2. 레코드 삽입(추가)\n" +
                "\t3. 레코드 수정\n" +
                "\t4. 레코드 삭제\n" +
                "\t5. 조건부 검색\n" +
                "\t6. 프로그램 종료\n" +
                "\t9. COMMIT\n" +
                "\n원하는 메뉴를 선택하세요: ");
        BW.flush();
    }

    @Override
    public synchronized void menu() throws SQLException, IOException {

        GogakDTO dto = new GogakDTO();
        while (true) {
            info();
            switch (BR.readLine().trim()) {
                case "0":
                    BW.write("ROLLBACK 하시겠습니까(Y/N)?");
                    BW.flush();
                    if (BR.readLine().trim().equalsIgnoreCase("Y")) {
                        conn.rollback();
                        LOGGER.info("ROLLBACK COMPLETE");
                    }
                    break;
                case "1":
                    selectAll(dto.getClassName());
                    break;
                case "2":
                    insert(dto.getClassName());
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
                    return;
                case "9":
                    BW.write("COMMIT 하시겠습니까(Y/N)?");
                    BW.flush();
                    if (BR.readLine().trim().equalsIgnoreCase("Y")) {
                        conn.commit();
                        LOGGER.info("COMMIT COMPLETE");
                    }
                    break;
                default:
                    BW.write("잘못된 입력입니다.\n\n");
                    break;
            }
        }
    }

    @Override
    public synchronized void selectAll(String className) throws SQLException, IOException {

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
    }

    @Override
    public synchronized void insert(String className) throws IOException, SQLException, NumberFormatException {

        BW.write("GNO : ");
        BW.flush();
        int gno = Integer.parseInt(BR.readLine());

        BW.write("GNAME : ");
        BW.flush();
        String gname = BR.readLine();

        BW.write("JUMIN : ");
        BW.flush();
        String jumin = BR.readLine();

        BW.write("POINT : ");
        BW.flush();
        int point = Integer.parseInt(BR.readLine());

        pstmt = conn.prepareStatement("INSERT INTO " + className + " VALUES (?, ?, ?, ?)");
        pstmt.setInt(1, gno);
        pstmt.setString(2, gname);
        pstmt.setString(3, jumin);
        pstmt.setInt(4, point);

        int res = pstmt.executeUpdate();
        LOGGER.info("INSERT COMPLETE\n");
        BW.write(res > 0 ? String.format("%d개의 데이터 추가 완료.%n", res) : "고객정보가 존재하지 않습니다.\n\n");
    }

    @Override
    public synchronized void update(String className) throws IOException, SQLException, NumberFormatException {

        BW.write("수정할 고객 번호를 입력해주세요.\n");
        BW.flush();
        int gno = Integer.parseInt(BR.readLine());
        BW.write("수정할 번호를 입력하세요.: ");
        BW.flush();
        int newGno = Integer.parseInt(BR.readLine());
        BW.write("수정할 고객 이름을 입력하세요: ");
        BW.flush();
        String gname = BR.readLine();
        BW.write("수정할 고객 주민번호를 입력하세요.: ");
        BW.flush();
        String jumin = BR.readLine();
        BW.write("수정할 고객 포인트를 입력하세요.: ");
        BW.flush();
        int point = Integer.parseInt(BR.readLine());

        String sql = "UPDATE gogak SET gno = ?, gname = ?, jumin = ?, point = ? WHERE gno = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, newGno);
        pstmt.setString(2, gname);
        pstmt.setString(3, jumin);
        pstmt.setInt(4, point);
        pstmt.setInt(5, gno);

        int res = pstmt.executeUpdate();
        LOGGER.info("UPDATE COMPLETE\n");
        BW.write(res > 0 ? String.format("%d개의 데이터 수정 완료.%n", res) : "고객정보가 존재하지 않습니다.\n\n");
    }

    @Override
    public synchronized void delete(String className) throws IOException, SQLException, NumberFormatException {

        BW.write("GNO: ");
        BW.flush();
        int gno = Integer.parseInt(BR.readLine());

        pstmt = conn.prepareStatement("DELETE FROM " + className + " WHERE gno = ?");
        pstmt.setInt(1, gno);

        int res = pstmt.executeUpdate();
        LOGGER.info("DELETE COMPLETE\n");
        BW.write(res > 0 ? String.format("%d개의 데이터 삭제 완료.%n", res) : "고객정보가 존재하지 않습니다.\n\n");
    }

    @Override
    public synchronized void selectByGno(String className) throws SQLException, IOException, NumberFormatException {

        pstmt = conn.prepareStatement("SELECT * FROM " + className + " WHERE gno = ?");
        BW.write("검색할 사람의 GNO(고객번호) 입력: ");
        BW.flush();
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
            BW.write("고객정보가 존재하지 않습니다.\n\n");
            BW.flush();
            return;
        }

        for (GogakDTO gogak : list) {
            BW.write("GNO: " + gogak.getGno()
                    + "\nGNAME: " + gogak.getGname()
                    + "\nJUMIN: " + gogak.getJumin()
                    + "\nPOINT: " + gogak.getPoint() + "\n\n");
        }
    }
}
