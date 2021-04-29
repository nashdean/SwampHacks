package registrationbean;
import dblib.Registration;
import java.util.List;

import javax.ejb.Stateless;
import remotelib.IRemoteRP;

@Stateless(name = "RPEJB") //Might be "RPEJB" or something else
public class RPBean implements IRemoteRP {

    private Registration mdb;

    public RPBean() {
        mdb = new Registration("ism6236","ism6236bo");
        if (mdb == null)
            System.err.println("Connection null;");
    }

    //OUR CLASS
    @Override
    public int Register(int StuNo, String CourseNo, String SecNo) {
        return mdb.Register(StuNo, CourseNo, SecNo);
    }

    @Override
    public List<String> List(String semester, String year)  {
        return mdb.List(semester, year);
    }

    @Override
    public List<String> List(int StuNo, String semester, String year)  {
        return mdb.List(StuNo, semester, year);
    }


}