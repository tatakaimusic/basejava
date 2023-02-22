package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.Writer;

public class ResumeServlet extends HttpServlet {
    private final Storage storage = Config.get().getSqlStorage();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        Writer writer = response.getWriter();
        writer.write("<html lang=\"en\">\n" +
                "               <head>\n" +
                "                   <meta charset=\"UTF-8\">\n" +
                "                   <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "                   <title>Resumes</title>\n" +
                "               </head>\n" +
                "               <body>\n" +
                "                    <header>Приложение вебинара <a href=\"http://javawebinar.ru/basejava/\" target=\"_blank\">Практика Java. Разработка Web\n" +
                "                            приложения.</a>" +
                "                    </header>" +
                "                    <table>\n" +
                "                         <tr>\n" +
                "                             <th>uuid</th>\n" +
                "                             <th>full_name</th>\n" +
                "                         </tr>\n");
        for (Resume resume : storage.getAllSorted()) {
            writer.write("            <tr>\n" +
                    "                         <td>" + resume.getUuid() + "</td>\n" +
                    "                         <td>" + resume.getFullName() + "</td>\n" +
                    "                     </tr>\n");
        }
        writer.write("           </table>\n" +
                "                    <footer>Приложение вебинара <a href=\"http://javawebinar.ru/basejava/\" target=\"_blank\">Практика Java. Разработка Web\n" +
                "                            приложения.</a>" +
                "                    </footer>" +
                "                </body>\n" +
                "         </html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
