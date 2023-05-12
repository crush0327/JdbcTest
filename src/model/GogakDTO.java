package model;

public class GogakDTO {

    private int gno;    // 고객번호
    private String gname;   // 고객명
    private String jumin;   // 주민등록번호
    private int point;  // 마일리지 점수

    public static final String CLASS_NAME = "Gogak";

    public String getClassName() {
        return CLASS_NAME;
    }

    public int getGno() {
        return gno;
    }

    public void setGno(int gno) {
        this.gno = gno;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getJumin() {
        return jumin;
    }

    public void setJumin(String jumin) {
        this.jumin = jumin;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
