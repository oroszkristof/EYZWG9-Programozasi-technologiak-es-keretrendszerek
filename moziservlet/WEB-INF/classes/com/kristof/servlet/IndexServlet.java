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

        
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 0; background: #f4f4f4; }");
        out.println(".container { display: flex; justify-content: space-between; align-items: center; padding: 50px; }");
        out.println(".left-img { width: 45%; }");
        out.println(".left-img img { width: 100%; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.2); }");
        out.println(".right-text { width: 45%; text-align: right; padding: 20px; }");
        out.println(".right-text h1 { font-size: 32px; margin-bottom: 20px; }");
        out.println(".right-text p { font-size: 18px; font-weight: bold; }");
        out.println("</style>");

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

        out.println("</div>"); 

        out.println("</body></html>");
    }
}
