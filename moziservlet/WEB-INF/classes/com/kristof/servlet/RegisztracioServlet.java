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

public class RegisztracioServlet extends HttpServlet {

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
                <title>Regisztráció</title>
                <link rel='stylesheet' href='urlap.css'>
            </head>
            <body>

            <div class='container form-box'>
                <h2>Regisztráció</h2>

                <form action='regisztracio' method='POST'>
                    <label>Név:</label>
                    <input type='text' name='nev' required>

                    <label>Email:</label>
                    <input type='email' name='email' required>

                    <label>Jelszó:</label>
                    <input type='password' name='jelszo' required>

                    <button type='submit'>Regisztráció</button>
                </form>

                <p>Már van fiókja?
                    <a href='bejelentkezes'>Jelentkezzen be!</a>
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
        req.setCharacterEncoding("UTF-8");

        String nev = req.getParameter("nev");
        String email = req.getParameter("email");
        String jelszo = req.getParameter("jelszo");

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            String sql = "INSERT INTO felhasznalok (nev, email, jelszo) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nev);
            ps.setString(2, email);
            ps.setString(3, jelszo);

            ps.executeUpdate();

      
            resp.sendRedirect("bejelentkezes");

        } catch (SQLException e) {
            PrintWriter out = resp.getWriter();
            out.println("<h1>Hiba történt a regisztráció során!</h1>");
            out.println("<a href='regisztracio'>Próbáld újra</a>");
        }
    }
}
