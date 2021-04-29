package com.example.RPServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import dblib.Registration;

@WebServlet(name = "Register", value = "/Register",
        initParams = {@WebInitParam(name = "uid", value = "ism6236"),
                @WebInitParam(name = "pass", value = "ism6236bo")})
public class Register extends HttpServlet {

    Registration mdb;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String uid = config.getInitParameter("uid");
        String pass = config.getInitParameter("pass");

        mdb = new Registration(uid, pass);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String sidString = request.getParameter("stuid");
        String cno = request.getParameter("course");
        String sec = request.getParameter("section");
        String success = "";
        String error = "";

        List<String> registered = new ArrayList<>();

        String[] courses = {"ISM6128", "ISM6129", "ISM6215", "ISM6215", "ISM6216", "ISM6216", "ISM6222",
                            "ISM6222", "ISM6223", "ISM6236", "QMB6755", "QMB6755", "QMB6756", "QMB6756"};
        String[] sections = {"1S19","1F19"};

        int n = 0;

        if (sidString == null)
            sidString = "1";
        if (cno == null)
            cno = "";
        if (sec == null)
            sec = "";

        int sid = Integer.parseInt(sidString);

        if ((sidString != null && cno != null && sec != null && sidString != "" && cno != "" && sec != "")) {

            n = mdb.Register(sid, cno, sec);
            int temp = n;

            for (String c : courses) {
                if (c.compareTo(cno) == 0) {
                    error = "";
                    n = temp;
                    break;
                }
                else{
                    if(!error.contains("The course code you have entered is invalid."))
                        error += "The course code you have entered is invalid.<br>";
                    n = -400;
                }
            }
            for (String s : sections) {
                if (s.compareTo(sec) == 0) {
                    if(error.contains("The course code you have entered is invalid")) {
                        error = "The course code you have entered is invalid.<br>";
                        n = -400;
                    }
                    else
                        n = temp;
                    break;
                }
                else{
                    if(!error.contains("The section number you have entered is invalid."))
                        error += "The section number you have entered is invalid.<br>";
                    n = -400;
                }
            }
        }
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registration Response </title>");
            out.println("</head>");
            out.println("<body>");
            if (n == 2) {
                out.println("<b>Student Successfully Registered</b><br>");
                success = String.format("%10d | %10s | %10s", sid, cno, sec);
                registered.add(success);
            } else if (n == -1)
                out.println("<b>SQL Exception: Transaction Failed</b><br>");
            else if (n == -100)
                out.printf("<b>The course %s section %s is at capacity.  Cannot register Student %d</b><br>\n", cno, sec, sid);
            else if (n == -200)
                out.printf("<b>The Student No %d you have entered is not a student in the registry</b><br>\n", sid);
            else if (n == -300)
                out.printf("<b>Student %d is already registered.  Cannot double register Student %d</b><br>\n", sid, sid);
            else if (n == -400)
                out.println("<b>"+error+"</b>");
            out.println("<br>");


            out.println("<form name=\"Register Student\" ACTION = \"Register\" METHOD=\"GET\"> ");
            out.println("<fieldset id=\"info\">");
            out.println("<legend > Register Student: </legend>");
            String line = String.format("<label for=\"sid\"> Student ID: </label> <INPUT id= \"sid\" type=text size=20 name=\"stuid\" value=\"%s\"> <br>", sid);
            out.println(line);
            line = String.format("<label for=\"cno\"> Course No: </label> <INPUT id= \"cno\" type=text size=20 name=\"course\" value=\"%s\"> <br>", cno);
            out.println(line);
            line = String.format("<label for=\"sec\"> Section No: </label> <INPUT id= \"sec\" type=text size=20 name=\"section\" value=\"%s\"> <br>  ", sec);
            out.println(line);
            out.println("<label for=\"sectionlist\"> Sections: </label> <br> ");
            out.println("<select id = \"section\" name =\"sectionlist\" size=\"10\"> ");

            if (registered.isEmpty() == false) {
                for (String s : registered) {
                    line = String.format("<option>%s</option>", s);
                    out.println(line);
                }
            }

            out.println("</select> <br>");
            out.println(" <INPUT TYPE=\"submit\" VALUE=\"Register Student\"> ");
            out.println(" <Input TYPE =\"submit\" formaction=\"index.jsp\" value=\"Main Menu\">");
            out.println("</fieldset>");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


//                out.println("<a href=\"index.jsp\"> Main Menu </a> <br>");
//                out.println("</body>");
//                out.println("</html>");
    }

}


