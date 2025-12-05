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

public class FoglalasListaServlet extends HttpServlet {

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
        out.println("<title>Foglalások listája</title>");
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

        out.println("<h1>Foglalások listája</h1>");
        out.println("<div class='card-container'>");  // GRID kezdete

        try (Connection conn = Kapcsolat.getKapcsolat()) {

            String vetitesSql = "SELECT id, cim, idopont FROM vetites";
            PreparedStatement vetPs = conn.prepareStatement(vetitesSql);
            ResultSet vetRs = vetPs.executeQuery();

            while (vetRs.next()) {

                int vetitesId = vetRs.getInt("id");
                String cim = vetRs.getString("cim");
                String idopont = vetRs.getString("idopont");

                out.println("<div class='card'>");  // 🔵 Egy vetítés kártya

                out.println("<h3>" + cim + "</h3>");
                out.println("<p><b>Időpont:</b> " + idopont + "</p>");
                out.println("<p><b>Foglalt helyek:</b></p>");

                // Foglalások lekérése
                String helySql = "SELECT sor, szek FROM foglalas WHERE vetites_id = ?";
                PreparedStatement helyPs = conn.prepareStatement(helySql);
                helyPs.setInt(1, vetitesId);
                ResultSet helyRs = helyPs.executeQuery();

                boolean van = false;

                out.println("<ul>");

                while (helyRs.next()) {
                    van = true;
                    int sor = helyRs.getInt("sor");
                    int szek = helyRs.getInt("szek");
                    out.println("<li>Sor: " + sor + ", Szék: " + szek + "</li>");
                }

                out.println("</ul>");

                if (!van) {
                    out.println("<p>Nincs foglalt hely ennél a vetítésnél.</p>");
                }

                out.println("</div>"); // kártya vége
            }

        } catch (SQLException e) {
            out.println("<h2>Hiba történt az adatbázis lekérdezése során!</h2>");
            e.printStackTrace(out);
        }

        out.println("</div>"); // GRID vége
        out.println("</body></html>");
    }
}
