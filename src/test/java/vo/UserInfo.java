package vo;

import com.haibin.common.annotation.XlsColumn;

/**
 * @author haibin.tang
 * @create 2018-07-03 下午4:41
 **/
public class UserInfo {

    @XlsColumn(index = 0)
    private String username;

    @XlsColumn(index = 1)
    private String mobile;

    @XlsColumn(index = 2)
    private String age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", age=" + age +
                '}';
    }
}
