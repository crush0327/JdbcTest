package view;

import controller.GogakService;
import controller.GogakServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

public class MainEntry {

    private static final GogakService SERVICE = GogakServiceImpl.getInstance();

    public static void main(String[] args) throws IOException, SQLException {

        SERVICE.connect("ORACLE");  // ORACLE JDBC
        SERVICE.menu();
    }
}
