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

public class VetitesListaServlet extends HttpServlet {

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
        out.println("<title>Vetítések</title>");
        out.println("<link rel='stylesheet' href='kartya.css'>");
        out.println("<link rel='stylesheet' href='menu.css'>");
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

        out.println("<h1>Vetítések listája</h1>");
        out.println("<div class='card-container'>");

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            String sql = "SELECT id, cim, leiras, idopont, ar FROM vetites";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String cim = rs.getString("cim");
                String leiras = rs.getString("leiras");
                String idopont = rs.getString("idopont");
                int ar = rs.getInt("ar");

                out.println("<div class='card'>");
                out.println("<h3>" + cim + "</h3>");
                out.println("<p>" + leiras + "</p>");
                out.println("<p><b>Időpont:</b> " + idopont + "</p>");
                out.println("<p><b>Ár:</b> " + ar + " Ft</p>");

                out.println("<form action='foglalas' method='GET'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<button type='submit'>Foglalás</button>");
                out.println("</form>");

                out.println("</div>");
            }

        } catch (SQLException e) {
            out.println("<h2>Hiba történt!</h2>");
            e.printStackTrace(out);
        }

        out.println("</div></body></html>");
    }
}
