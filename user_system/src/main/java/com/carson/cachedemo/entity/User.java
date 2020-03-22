package com.carson.cachedemo.entity;

/**
 * <p></p>
 *
 * @author zhanghangfeng5 2019/8/6
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6
 * @modify by reason:{方法名}:{原因}
 */



import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 179429694L;

    @NotNull
    @Pattern(regexp = "[0-9]")
    private int userid;//用户id，唯一识别

    @NotBlank(message = "用户名不能为空")
    private String username;//用户名

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[A-Z][a-z][0-9]")
    private String passwd;

    @Min(1)
    private Integer age;//年龄大于1


    private String creattime;



    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }
}