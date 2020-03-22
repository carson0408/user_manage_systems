package com.carson.cachedemo.service;


import com.carson.cachedemo.Enum.Result;
import com.carson.cachedemo.entity.Friends;
import com.carson.cachedemo.entity.Login;
import com.carson.cachedemo.entity.Relation;
import com.carson.cachedemo.entity.User;
import com.carson.cachedemo.exception.OperationException;
import com.carson.cachedemo.mapper.UserManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Service
public class UserManageServiceImpl implements UserManageService{

    @Autowired
    UserManageMapper userManageMapper;


    /**
     * 根据用户id获取用户信息
     * @param userid
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Cacheable(value = "user",key = "#userid",unless = "#result==null")
    public User findUser(int userid) {
        return userManageMapper.findByUserid(userid);
    }

    /**
     * 删除的时候需要把好友关系表中的关系也删除。
     * @param userid
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @CacheEvict(value = "user",key = "#userid")
    public int deleteUser(int userid) {

        int res2 = userManageMapper.deleteFriendByUserid(userid);
        int res3= userManageMapper.deleteFriendByFriendid(userid);
        int res1= userManageMapper.deleteByUserid(userid);
        return res1;
    }

    /**
     *  查询所有好友
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public List<User> findAllUser() {
        return userManageMapper.findAllUser();
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public int saveUser(User user) {
        return userManageMapper.insertUser(user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @CachePut(value = "user",key = "#userid")
    public int updateUser(User user) {
        return userManageMapper.updateUser(user);
    }

    /**
     * 获取所有好友信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public List<Friends> getFriends() {
        return userManageMapper.getFriends();
    }

    /**
     * 根据userid获取好友信息
     * @param userid
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public Friends getFriendsByUserid(int userid) {
        return userManageMapper.getFriendsByUserid(userid);
    }

    /**
     * 先对friendid进行查询，如果用户表中没有friendid，以防安全，禁止添加，成对添加
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public int addFriend(Relation relation) {

       Relation search=userManageMapper.getRelation(relation);
       if(search!=null){
           throw new OperationException(Result.FRIENDS_EXIST);
       }
       Relation newRelation = new Relation();
       newRelation.setUserid(relation.getFriendid());
       newRelation.setFriendid(relation.getUserid());
       int res1 = userManageMapper.insertFriend(relation);
       int res2 = userManageMapper.insertFriend(newRelation);
       return res1*res2;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public User findByFriendid(int friendid) {
        return userManageMapper.findByFriendid(friendid);
    }

    /**
     * 删除好友关系，逻辑是成对删除
     * @param relation
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public int deleteFriend(Relation relation) {
        Relation search=userManageMapper.getRelation(relation);
        if(search==null){
            throw new OperationException(Result.FRIENDS_NOT_EXIST);
        }
        Relation newRelation = new Relation();
        newRelation.setUserid(relation.getFriendid());
        newRelation.setFriendid(relation.getUserid());
        int res1 = userManageMapper.deleteFriend(relation);
        int res2 = userManageMapper.deleteFriend(newRelation);
        return res1*res2;

    }

    /**
     * 用于查询是否存在该用户
     * @param login
     * @return
     */
    @Override
    public User search(Login login) {
        return userManageMapper.searchForLogin(login);
    }


}
