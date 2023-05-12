package model;

public class GogakDTO {

    public final String ClassName = "Gogak";

    private int gno;
    private String gname;
    private String jumin;
    private int point;

    public String getClassName() {
        return ClassName;
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
