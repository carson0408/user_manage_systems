package com.carson.cachedemo.entity;

import java.util.List;

/**
 * <p></p>
 *构造的一个关系列表输出是一个一对多的关系
 * @author zhanghangfeng5 2019/8/6
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6
 * @modify by reason:{方法名}:{原因}
 */
public class Friends {



    private int userid;

    private List<UserInfo> friendInfos;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public List<UserInfo> getFriendInfos() {
        return friendInfos;
    }

    public void setUserInfos(List<UserInfo> friendInfos) {
        this.friendInfos = friendInfos;
    }
}
