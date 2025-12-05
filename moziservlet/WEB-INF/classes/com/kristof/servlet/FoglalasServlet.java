package com.kristof.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kristof.db.Kapcsolat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class FoglalasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int vetitesId = Integer.parseInt(req.getParameter("id"));

        out.println("<!DOCTYPE html>");
        out.println("<html lang='hu'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Foglalás</title>");
        out.println("<link rel='stylesheet' href='menu.css'>");
        out.println("<link rel='stylesheet' href='foglalas.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Helyfoglalás</h1>");
        out.println("<form method='POST' action='foglalas'>");

        out.println("<input type='hidden' name='vetites_id' value='" + vetitesId + "'>");

        out.println("<div class='seats'>");
        int seatNum = 1;

        for (int sor = 1; sor <= 5; sor++) {
            out.println("<div class='row'>Sor " + sor + ": ");
            for (int szek = 1; szek <= 4; szek++) {
                out.println("<label>");
                out.println("<input type='checkbox' name='szek' value='" + sor + "-" + szek + "'>");
                out.println("[" + sor + "/" + szek + "]");
                out.println("</label> ");
                seatNum++;
            }
            out.println("</div><br>");
        }

        out.println("</div>");
        out.println("<button type='submit'>Foglalás mentése</button>");
        out.println("</form>");
        out.println("</body></html>");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int vetitesId = Integer.parseInt(req.getParameter("vetites_id"));
        String[] seats = req.getParameterValues("szek");

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            if (seats != null) {
                for (String place : seats) {
                    String[] parts = place.split("-");
                    int sor = Integer.parseInt(parts[0]);
                    int szek = Integer.parseInt(parts[1]);

                    PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO foglalas (vetites_id, sor, szek) VALUES (?, ?, ?)");
                    ps.setInt(1, vetitesId);
                    ps.setInt(2, sor);
                    ps.setInt(3, szek);
                    ps.executeUpdate();
                }
            }

            out.println("<h1>Foglalások sikeresen elmentve!</h1>");
            out.println("<a href='foglalasok'>Foglalások listája</a>");

        } catch (SQLException e) {
            out.println("<h2>Hiba történt mentés közben!</h2>");
            e.printStackTrace(out);
        }
    }
}
