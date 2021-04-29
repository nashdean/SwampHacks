package dblib;

import com.sun.org.glassfish.external.statistics.CountStatistic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
//import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.DriverManager;
import java.util.GregorianCalendar;

public class Registration {

    // <editor-fold defaultstate="collapsed" desc=" Data ">
    static private String mservername;
    static private String mdbname;
    static private String url;
    static private Connection mcn;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Constructors ">
    //static constructor
    static {
        mservername = "MSSQLSERVER";
        mdbname = "registration";
    }

    public Registration(String uid, String pass) {

        setConnection(uid, pass);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Utility Functions  ">
    public boolean IsConnected()    {
        return (mcn != null ? true : false);
    }

    public void setConnection(String uid, String pass) {
        try {
            //Per microsoft documentation no need to load the driver explicitly. Get connection does that on our behalf.
            // See http://msdn.microsoft.com/en-us/library/ms378526.aspx

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost\\" + mservername
                    + ";databaseName=" + mdbname + ";user=" + uid + ";password=" + pass + ";";

            mcn = DriverManager.getConnection(connectionUrl);
            if (mcn == null)
                System.out.println("Connection Failed");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error code:" + ex.getErrorCode());//ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private int TransactSQL(String[] sql) {
        Statement st = null;
        int n = 0;
        try {
            st = mcn.createStatement();
            //System.out.println(mcn.getAutoCommit());
            mcn.setAutoCommit(false);

            for (int i = 0; i < sql.length; i++) {
                n += st.executeUpdate(sql[i]);
            }
            //mcn.rollback();
            mcn.commit();
        } catch (SQLException ex) {
            try {
                //rollback if there is an error
                mcn.rollback();
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return n;
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=" Database Functions  ">


    public List<String> List(String semester, String year) {
        ArrayList<String> rval = new ArrayList<String>();
        try {

            Statement s = mcn.createStatement();
            String sql = String.format("Select CourseNo, SectionNo, Room, Days, Time from SECTION where SECTION.Year =%s AND SECTION.Semester ='%s' AND TotalEnrolled < Capacity" , year, semester);
            ResultSet rs = s.executeQuery(sql);
            String buf ="";
            while (rs.next()) {

                String courseNo = rs.getString(1);
                String secNo = rs.getString(2);
                String room = rs.getString(3);
                String days = rs.getString(4);
                String time = rs.getString(5);
                buf = String.format("%10s | %10s | %10s | %10s | %10s",courseNo, secNo, room, days, time);
                rval.add(buf);
            }

            s.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return rval;
    }

    public List<String> List(int StuNo, String semester, String year) {
        ArrayList<String> rval = new ArrayList<String>();
        try {

            Statement s = mcn.createStatement();
            String sql = String.format("Select e.CourseNo, e.SectionNo, s.Room, s.Days, s.Time from [ENROLLMENT] e INNER JOIN SECTION s ON e.SectionNo=s.SectionNo AND e.CourseNo=s.CourseNo where s.Year = %s AND s.Semester = '%s' AND e.StuNo = %s;" , year, semester, StuNo);
            ResultSet rs = s.executeQuery(sql);
            String buf ="";
            while (rs.next()) {

                String courseNo = rs.getString(1);
                String secNo = rs.getString(2);
                String room = rs.getString(3);
                String days = rs.getString(4);
                String time = rs.getString(5);
                buf = String.format("%10s | %10s | %10s | %10s | %10s",courseNo, secNo, room, days, time);
                rval.add(buf);
            }

            s.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return rval;
    }

    public int Register(int StuNo, String CourseNo, String SecNo) {
        int rval = 0;
        boolean realStudent = false;
        boolean realCourse = false;
        boolean realSec = false;

        ArrayList<Integer> stuNoList = new ArrayList<>();
        ArrayList<Integer> stuNoRegistered = new ArrayList<>();
        ArrayList<String> cnoList = new ArrayList<>();
        ArrayList<String> secNoList = new ArrayList<>();

        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;

        // <editor-fold defaultstate="collapsed" desc=" CREATE A NEW ORDER ID ">
        try{

            String prepState1 = String.format("Select  s.TotalEnrolled, s.Capacity from SECTION s where s.CourseNo = '%s' AND s.SectionNo ='%s'", CourseNo, SecNo);
            String prepState2 = String.format("Select e.StuNo from [ENROLLMENT] e INNER JOIN SECTION s ON e.SectionNo=s.SectionNo AND e.CourseNo=s.CourseNo where s.CourseNo = '%s' AND s.SectionNo = '%s'", CourseNo, SecNo);
            String prepState3 = String.format("Select StuNo from STUDENT");

            pst1 = mcn.prepareStatement(prepState1);
            ResultSet rs1 = pst1.executeQuery();
            int enrolled =0;
            int capacity =3;
            //Make a new oid
            while(rs1.next()) {
                enrolled = rs1.getInt(1);
                capacity = rs1.getInt(2);
            }
            pst1.close();

            if(enrolled == capacity) {
                System.out.printf("\nThe course %s section %s is at Capacity.  Cannot register Student %d\n", CourseNo, SecNo, StuNo);
                rval = -100;
                return rval;
            }

            if(enrolled > 0) {

                pst3 = mcn.prepareStatement(prepState3);
                ResultSet rs3 = pst3.executeQuery();
                int sno = 0;

                while (rs3.next()) {
                    sno = rs3.getInt(1);
                    stuNoList.add(sno);

                }
                pst3.close();
                for(int s: stuNoList) {
                    if (s == StuNo) {
                        realStudent = true;
                        break;
                    }
                }

                if(!realStudent){
                    System.out.println("\nThe Student No you have entered is not a student in the registry");
                    rval = -200;
                    return rval;
                }

                pst2 = mcn.prepareStatement(prepState2);
                ResultSet rs2 = pst2.executeQuery();
                sno = 0;

                //Make a new oid
                while (rs2.next()) {
                    sno = rs2.getInt(1);
                    stuNoRegistered.add(sno);

                }
                pst2.close();

                for(int s: stuNoRegistered){
                    if (s == StuNo){
                        System.out.printf("\nStudent %d is already registered.  Cannot double register Student %d\n", StuNo, StuNo);
                        rval = -300;
                        return rval;
                    }
                }
            }

            //</editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" CREATE A NEW ORDER ">

            String [] sql = new String[2];
            sql[0] = String.format("Insert Into ENROLLMENT(StuNo, CourseNo, SectionNo) Values (%d, '%s', '%s');",StuNo,CourseNo,SecNo);

            //</editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" For each item purchased create an orderdetail record and decrement the onhand by 1 ">


//            sql[1] = String.format("Insert Into OrderDetails(oid,pid,Quantity,Price) Values (%s, '%s', %s,%s);", oid,vals[0],vals[3],vals[2]);
            sql[1] = String.format("UPDATE SECTION SET TotalEnrolled = TotalEnrolled+1 WHERE CourseNo = '%s' AND SectionNo = '%s';",CourseNo, SecNo);


            rval = TransactSQL(sql);

            // </editor-fold>
        }
        catch (SQLException ex) {rval =-1; }
        return rval;
    }

// </editor-fold>

//  public static void main(String[] args) {
//        // TODO code application logic here
//        Registration o = new Registration("ism6236","ism6236bo");
//
//        System.out.println("Registration Successful");
//      List<String> l = o.List("Fall", "2019");
//      System.out.printf("%10s | %10s | %10s | %10s | %10s\n","CourseNo", "SectionNo", "Room", "Days", "Time");
//
//      for (int i = 0; i < l.size(); i++) {
//          String sec = l.get(i); //a[i];
//
//          System.out.println(String.format("%s", sec));
//      }
//      List<String> nl = o.List(1,"Fall", "2019");
//      for (int i = 0; i < nl.size(); i++) {
//          String sec = nl.get(i); //a[i];
//
//          System.out.println(String.format("%s", sec));
//      }
//
////      int rows = o.Register(2, "QMB6755", "1S19");
////      System.out.println("There were " + rows + " affected in this transaction");
//    }
}
