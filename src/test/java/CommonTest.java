import com.haibin.common.util.NotNullUtil;
import org.junit.Test;
import vo.UserVo;

/**
 * @author haibin.tang
 * @create 2018-06-23 下午5:02
 **/
public class CommonTest {

    @Test
    public void notNullAnnotation() {
        UserVo userVo = new UserVo();
        userVo.setName("aa");
        userVo.setMobile("23");
        userVo.setOs("wchat");
        NotNullUtil.check(userVo);
    }
}
