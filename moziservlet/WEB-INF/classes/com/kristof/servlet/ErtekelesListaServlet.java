package com.kristof.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kristof.db.Kapcsolat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ErtekelesListaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='hu'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Értékelések</title>");
        out.println("<link rel='stylesheet' href='kartya.css'>");
        out.println("<link rel='stylesheet' href='menu.css'>");
        out.println("<link rel='stylesheet' href='gomb.css'>");   
        out.println("</head>");
        out.println("<body>");

        Object user = req.getSession().getAttribute("felhasznalo");
        out.println("<nav class='menu'>");
        if (user == null) {
            out.println("<a href='bejelentkezes'>Bejelentkezés</a>");
            out.println("<a href='regisztracio'>Regisztráció</a>");
        } else {
            out.println("<a href='vetitesek'>Vetítések</a>");
            out.println("<a href='foglalasok'>Foglalások</a>");
            out.println("<a href='ertekelesek'>Értékelések</a>");
            out.println("<a href='kijelentkezes'>Kijelentkezés</a>");
        }
        out.println("</nav>");

       
        out.println("<div style='text-align:right; margin:20px;'>");
        out.println("<a class='gomb' href='ertekeles-felvitel'>Új értékelés hozzáadása</a>");
        out.println("</div>");

        out.println("<h1>Értékelések listája</h1>");
        out.println("<div class='card-container'>");

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            String sql = "SELECT vetites_cim, csillag FROM ertekeles";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean van = false;

            while (rs.next()) {
                van = true;

                String cim = rs.getString("vetites_cim");
                int csillag = rs.getInt("csillag");

                out.println("<div class='card'>");
                out.println("<h3>" + cim + "</h3>");
                out.println("<p><b>Értékelés:</b> " + csillag + " / 5 ⭐</p>");
                out.println("</div>");
            }

            if (!van) {
                out.println("<p>Nincs még értékelés.</p>");
            }

        } catch (SQLException e) {
            out.println("<h2>Hiba történt az adatbázis lekérdezése során!</h2>");
            e.printStackTrace(out);
        }

        out.println("</div>"); 
        out.println("</body></html>");
    }
}



