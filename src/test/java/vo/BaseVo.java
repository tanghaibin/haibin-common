package vo;

import com.haibin.common.annotation.NotNull;

/**
 * @author haibin.tang
 * @create 2018-06-23 下午5:53
 **/
public class BaseVo  {

    @NotNull(message = "系统不能为空")
    private String os;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
