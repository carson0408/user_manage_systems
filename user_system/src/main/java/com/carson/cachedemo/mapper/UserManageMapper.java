package com.carson.cachedemo.mapper;


import com.carson.cachedemo.entity.Friends;
import com.carson.cachedemo.entity.Login;
import com.carson.cachedemo.entity.Relation;
import com.carson.cachedemo.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

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

@Repository
public interface UserManageMapper {



    User findByUserid(int userid);

    int deleteByUserid(int userid);

    List<User> findAllUser();

    int insertUser(User user);

    int updateUser(User user);


    List<Friends> getFriends();

    Friends getFriendsByUserid(int userid);


    int insertFriend(Relation relation);

    User findByFriendid(int friendid);

    int deleteFriend(Relation relation);

    int deleteFriendByUserid(int userid);

    int deleteFriendByFriendid(int userid);

    Relation getRelation(Relation relation);

    User searchForLogin(Login login);

}
