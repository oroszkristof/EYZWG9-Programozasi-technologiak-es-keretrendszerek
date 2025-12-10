package com.kristof.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html lang='hu'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Főoldal</title>");

        out.println("<link rel='stylesheet' href='menu.css'>");
        out.println("<link rel='stylesheet' href='index.css'>");

        out.println("</head><body>");

        Object user = req.getSession().getAttribute("felhasznalo");

        out.println("<nav class='menu'>");
        if (user == null) {
            out.println("<a href='bejelentkezes'>Bejelentkezés</a>");
            out.println("<a href='regisztracio'>Regisztráció</a>");
        } else {
            out.println("<a href='vetitesek'>Vetítések</a>");
            out.println("<a href='foglalasok'>Foglalások</a>");
            out.println("<a href='ertekelesek'>Értékelések</a>");
            out.println("<a href='profil'>Profil</a>");
            out.println("<a href='kijelentkezes'>Kijelentkezés</a>");
        }
        out.println("</nav>");

        
        out.println("<div class='container'>");

       
        out.println("<div class='left-img'>");
        out.println("<img src='mozi.jpg' alt='Mozi kép'>");
        out.println("</div>");

        
        out.println("<div class='right-text'>");
        out.println("<h1>Üdvözlöm a mozis jegyfoglaló rendszerben!</h1>");
        out.println("<p>Nézzen körül a mozis kínálatban, válasszon egy filmet és foglaljon jegyet online!</p>");
        out.println("</div>");

        out.println("</div>"); // container vége

        out.println("</body></html>");
    }
}
