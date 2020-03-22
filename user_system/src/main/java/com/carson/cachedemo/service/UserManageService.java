package com.carson.cachedemo.service;

import com.carson.cachedemo.entity.Friends;
import com.carson.cachedemo.entity.Login;
import com.carson.cachedemo.entity.Relation;
import com.carson.cachedemo.entity.User;

import java.util.List;

/**
 * <p></p>
 *
 * @author zhanghangfeng5 2019/8/6
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6
 * @modify by reason:{方法名}:{原因}
 */
public interface UserManageService {
    User findUser(int userid);

    int deleteUser(int userid);

    List<User> findAllUser();

    int saveUser(User user);


    int updateUser(User user);


    List<Friends> getFriends();

    Friends getFriendsByUserid(int userid);

    int addFriend(Relation relation);

    User findByFriendid(int friendid);

    int deleteFriend(Relation relation);


    User search(Login login);
}
