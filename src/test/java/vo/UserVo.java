package vo;

import com.haibin.common.annotation.NotNull;

/**
 * @author haibin.tang
 * @create 2018-06-23 下午5:03
 **/
public class UserVo {

    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "手机不能为空")
    private String mobile;

    private String address;

    @NotNull(message = "年龄不能为空")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
