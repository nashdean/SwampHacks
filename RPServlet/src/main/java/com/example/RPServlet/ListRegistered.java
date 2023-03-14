package com.example.RPServlet;

import dblib.Registration;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ListRegistered", value = "/Enrollment",
            initParams = {  @WebInitParam(name = "uid", value = "ism6236"),
                            @WebInitParam(name = "pass", value = "ism6236bo")})

public class ListRegistered extends HttpServlet {

    Registration mdb;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String uid = config.getInitParameter("uid");
        String pass = config.getInitParameter("pass");

        mdb = new Registration(uid,pass);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String headerstuff = "<meta charset=\"UTF-8\">\n "
                +  " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                +  "<link rel=\"stylesheet\" type =\"text/css\" href =\"servlet.css\" /> ";
        String sidString = request.getParameter("stuid");

        String semester = request.getParameter("semid");
        String year = request.getParameter("year");

        if (sidString == null)
            sidString = "1";
        if (year == null)
            year = "";
        if (semester == null)
            semester = "";

        int sid = Integer.parseInt(sidString);

        List<String> registered = mdb.List(sid, semester, year);

        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Enrollment</title>");
            out.println(headerstuff);
            out.println("</head>");
            out.println("<body>");
            out.println("<form name=\"section search\" ACTION = \"Enrollment\" METHOD=\"GET\"> ");
            out.println("<fieldset id=\"info\">");
            out.println("<legend > Enrollment: </legend>");
            String line = String.format("<label for=\"sid\"> Student ID: </label> <INPUT id= \"sid\" type=text size=20 name=\"stuid\" value=\"%s\"> <br>", sid);
            out.println(line);
            line = String.format("<label for=\"sem\"> Semester: </label> <INPUT id= \"sem\" type=text size=20 name=\"semid\" value=\"%s\"> <br>",semester);
            out.println(line);
            line = String.format("<label for=\"yr\"> Year: </label> <INPUT id= \"yr\" type=text size=20 name=\"year\" value=\"%s\"> <br>  ",year);
            out.println(line);
            out.println("<label for=\"sectionlist\"> Sections: </label> <br> ");
            out.println("<select id = \"section\" name =\"sectionlist\" size=\"10\"> ");

            for (String sections : registered) {
                line = String.format("<option>%s</option>", sections);
                out.println(line);
            }

            out.println("</select> <br>");
            out.println(" <INPUT TYPE=\"submit\" VALUE=\"Get Registered Classes\"> ");
            out.println(" <Input TYPE =\"submit\" formaction=\"index.jsp\" value=\"Main Menu\">");
            out.println("</fieldset>");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        }    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
