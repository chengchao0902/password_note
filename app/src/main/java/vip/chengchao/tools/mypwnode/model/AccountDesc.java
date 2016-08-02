package vip.chengchao.tools.mypwnode.model;

import java.util.Date;

/**
 * Created by chengchao on 16/6/30.
 */
public class AccountDesc {
    private int id;
    private String type;
    private String account;
    private String password;
    private String desc;
    private Date createTime;
    private Date updateTime;

    {
        createTime = new Date();
        updateTime = new Date();
    }

    public AccountDesc() {
    }

    public AccountDesc(String type, String account, String password) {
        this.type = type;
        this.account = account;
        this.password = password;
    }

    public AccountDesc(String type, String account, String password, String desc) {
        this(type, account, password);
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AccountDesc{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", desc='" + desc + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
