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

public class ErtekelesFelvitelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><meta charset='UTF-8'><title>Értékelés leadása</title>");
        out.println("<link rel='stylesheet' href='ertekeles.css'>");
        out.println("</head><body>");

        out.println("<h1>Értékelés leadása</h1>");

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            
            String sql = "SELECT id, cim FROM vetites";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            out.println("<div class='form-container'>");   // középre helyezett űrlap
            out.println("<form method='POST' action='ertekeles-felvitel' class='form-box'>");

            out.println("<label>Válassz vetítést:</label><br>");
            out.println("<select name='vetites_id' required>");

            while (rs.next()) {
                out.println("<option value='" + rs.getInt("id") + "'>" +
                        rs.getString("cim") + "</option>");
            }

            out.println("</select><br><br>");

            out.println("<label>Értékelés (1 – 5):</label><br>");
            out.println("<input type='number' name='csillag' min='1' max='5' required><br><br>");

            out.println("<button type='submit' class='btn-link'>Mentés</button>");
            out.println("</form>");
            out.println("</div>");

        } catch (SQLException e) {
            out.println("<h3>Hiba történt az adatbázis lekérdezésekor!</h3>");
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int vetitesId = Integer.parseInt(req.getParameter("vetites_id"));
        int csillag = Integer.parseInt(req.getParameter("csillag"));

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            // Cím lekérése (az ertekeles táblába is kell)
            String cimSql = "SELECT cim FROM vetites WHERE id = ?";
            PreparedStatement cimPs = conn.prepareStatement(cimSql);
            cimPs.setInt(1, vetitesId);
            ResultSet cimRs = cimPs.executeQuery();
            cimRs.next();
            String cim = cimRs.getString("cim");

            // Beszúrás az ertekeles táblába
            String insertSql =
                    "INSERT INTO ertekeles (vetites_id, vetites_cim, csillag) VALUES (?, ?, ?)";
            PreparedStatement insPs = conn.prepareStatement(insertSql);
            insPs.setInt(1, vetitesId);
            insPs.setString(2, cim);
            insPs.setInt(3, csillag);
            insPs.executeUpdate();

            out.println("<html><head><meta charset='UTF-8'><title>Mentve</title>");
            out.println("<link rel='stylesheet' href='ertekeles.css'>");
            out.println("</head><body>");

            out.println("<h1>Értékelés sikeresen mentve!</h1>");
            out.println("<p>Film: <b>" + cim + "</b></p>");
            out.println("<p>Leadott értékelés: <b>" + csillag + "</b> ⭐</p>");

            out.println("<a class='btn-link' href='ertekeles-felvitel'>Új értékelés leadása</a><br><br>");
            out.println("<a class='btn-link' href='ertekelesek'>Értékelések listája</a>");

            out.println("</body></html>");

        } catch (SQLException e) {
            out.println("<h2>Hiba történt a mentés során!</h2>");
            e.printStackTrace(out);
        }
    }
}

