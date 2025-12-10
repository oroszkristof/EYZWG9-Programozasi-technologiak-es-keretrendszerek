package com.kristof.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Kapcsolat {

    private static final String ADATBAZIS_URL =
            "jdbc:mariadb://localhost:3306/mozirendszer?useUnicode=true&characterEncoding=UTF-8";
    private static final String FELHASZNALO = "root";
    private static final String JELSZO = ""; 

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Nem található a MariaDB JDBC driver. Ellenőrizze, hogy a mariadb-java-client JAR a WEB-INF/lib mappában van-e!",
              
            );
        }
    }

    public static Connection getKapcsolat() throws SQLException {
        return DriverManager.getConnection(ADATBAZIS_URL, FELHASZNALO, JELSZO);
    }
}
