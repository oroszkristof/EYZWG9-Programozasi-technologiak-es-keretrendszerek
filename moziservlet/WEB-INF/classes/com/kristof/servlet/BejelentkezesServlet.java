package com.kristof.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import com.kristof.db.Kapcsolat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class BejelentkezesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("""
            <!DOCTYPE html>
            <html lang='hu'>
            <head>
                <meta charset='UTF-8'>
                <title>Bejelentkezés</title>
                <link rel='stylesheet' href='urlap.css'>
            </head>
            <body>

            <div class='container form-box'>
                <h2>Bejelentkezés</h2>

                <form action='bejelentkezes' method='POST'>
                    <label>Email:</label>
                    <input type='email' name='email' required>

                    <label>Jelszó:</label>
                    <input type='password' name='jelszo' required>

                    <button type='submit'>Bejelentkezés</button>
                </form>

                <p>Nincs még fiókja?
                    <a href='regisztracio'>Regisztráljon!</a>
                </p>
            </div>

            </body>
            </html>
        """);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        String email = req.getParameter("email");
        String jelszo = req.getParameter("jelszo");

        try (Connection conn = Kapcsolat.getKapcsolat()) {
            String sql = "SELECT * FROM felhasznalok WHERE email = ? AND jelszo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, jelszo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = req.getSession();
                session.setAttribute("felhasznalo", rs.getString("nev"));
                session.setAttribute("email", rs.getString("email"));

                resp.sendRedirect("vetitesek");

            } else {
                PrintWriter out = resp.getWriter();
                out.println("<h1>Hibás email vagy jelszó!</h1>");
                out.println("<a href='bejelentkezes'>Vissza</a>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
