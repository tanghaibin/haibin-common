import com.haibin.common.util.ExcelReadUtil;
import com.haibin.common.util.NotNullUtil;
import org.junit.Test;
import vo.UserInfo;
import vo.UserVo;

import java.io.FileInputStream;
import java.util.List;

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

    @Test
    public void testReadExcel() throws Exception {
//        List<UserInfo> datas = ExcelReadUtil.read("/app/test-import.xlsx", 1, 0, 0, UserInfo.class);
//        List<UserInfo> datas = ExcelReadUtil.read(new FileInputStream("/app/test-import.xlsx"), 1, 0, 0, UserInfo.class);
//        for (UserInfo data : datas) {
//            System.out.println(data);
//        }
    }
}
