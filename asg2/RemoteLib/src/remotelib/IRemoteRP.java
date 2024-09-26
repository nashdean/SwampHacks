package remotelib;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface IRemoteRP {
    public int Register(int StuNo, String CourseNo, String SecNo);
    public List<String> List(String semester, String year);
    public List<String> List(int StuNo, String semester, String year);
}
