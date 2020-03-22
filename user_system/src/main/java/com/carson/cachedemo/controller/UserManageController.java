package com.carson.cachedemo.controller;


import com.carson.cachedemo.Enum.Result;
import com.carson.cachedemo.commons.ResponseDTO;
import com.carson.cachedemo.entity.Friends;
import com.carson.cachedemo.entity.Login;
import com.carson.cachedemo.entity.Relation;
import com.carson.cachedemo.entity.User;
import com.carson.cachedemo.exception.OperationException;
import com.carson.cachedemo.service.UserManageService;
import com.carson.cachedemo.util.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.PostConstruct;
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
@RestController
@RequestMapping("/user")
public class UserManageController {


    private Log logger = LogFactory.getLog(UserManageController.class);

    @Autowired
    UserManageService userManageServiceImpl;





    @PostConstruct
    private void init(){

    }


    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResponseDTO login(String username, String passwd){
        Login login = new Login();
        login.setUsername(username);
        login.setPasswd(passwd);
        User user = userManageServiceImpl.search(login);
        if(user==null){
            Result result = Result.LOGIN_FAILURE;
            ResponseDTO responseDTO = ResponseUtil.doError(result.getResultCode(),Result.LOGIN_FAILURE_INFO.getMessage(),result.getMessage());
            return responseDTO;
        }else{
            Result result = Result.LOGIN_SUCCESS;

            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),Result.LOGIN_SUCCESS_INFO.getMessage(),result.getMessage());
            return responseDTO;
        }

    }


    /**
     * 用户查询接口
     * 根据userid获取某个用户信息
     * @param userid
     * @return
     */
    @RequestMapping(value="/get/{userid}",method = RequestMethod.GET)
    public ResponseDTO get(@PathVariable("userid") int userid){

        try {
            User user = userManageServiceImpl.findUser(userid);
            Result result = Result.GET_SUCCESS;
            return ResponseUtil.doSuccess(result.getResultCode(),user,result.getMessage());
        }catch (Exception e){
            Result result = Result.GET_FAILURE;
            throw new OperationException(result);
        }

    }


    /**
     * 获取所有用户信息
     * @return
     */
    @RequestMapping(value="/getall",method = RequestMethod.GET)
    public ResponseDTO getAll(){
        try {
            List<User> users = userManageServiceImpl.findAllUser();
            Result result = Result.GET_SUCCESS;
            return ResponseUtil.doSuccess(result.getResultCode(),users,result.getMessage());
        }catch (Exception e){
            Result result = Result.GET_FAILURE;
            throw new  OperationException(result);
        }

    }


    /**
     * 用户删除接口
     * 根据userid删除用户信息
     * @param userid
     * @return
     */
    @RequestMapping(value="/delete/{userid}",method = RequestMethod.DELETE)
    public ResponseDTO delete(@PathVariable("userid") int userid){
        logger.debug("删除userid："+userid+"的用户信息");
        int code = userManageServiceImpl.deleteUser(userid);
        if(code>0){
            Result result = Result.DELETE_SUCCESS;
            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),code,result.getMessage());
            return responseDTO;
        }else{
            Result result = Result.DELETE_FAILURE;
            throw new OperationException(result);
        }

    }

    /**
     * 用户新增接口
     * 插入用户信息
     * @param user
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public ResponseDTO save( User user){

        User searchUser = userManageServiceImpl.findUser(user.getUserid());
        //根据userid唯一识别查看用户表中是否已经存在该用户，已存在则抛出异常
        if(searchUser!=null){
            throw new OperationException(Result.USER_EXIST);
        }
        int code = userManageServiceImpl.saveUser(user);
        if(code>0){
            Result result = Result.COMMIT_SUCCESS;
            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),user,result.getMessage());
            logger.info("保存用户成功");
            return responseDTO;
        }else{
            Result result = Result.COMMIT_FAILURE;
            throw new OperationException(result);
        }

    }


    /**
     * 用户更新接口
     * 更新用户信息
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.PUT)
    public ResponseDTO update(User user){
        int userid = user.getUserid();

        User newUser = userManageServiceImpl.findUser(userid);
        if(user.getAge()!=null){
            newUser.setAge(user.getAge());
        }
        if(user.getUsername()!=null){
            newUser.setUsername(user.getUsername());
        }
        if(user.getPasswd()!=null){
            newUser.setPasswd(user.getPasswd());
        }

        int code = userManageServiceImpl.updateUser(newUser);

        if(code>0){
            Result result = Result.UPDATE_SUCCESS;
            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),newUser,result.getMessage());
            logger.info("更新用户成功");
            return responseDTO;
        }else{
            Result result = Result.UPDATE_FAILURE;
            throw new OperationException(result);
        }
    }


    /**
     * 用户之间好友关系的接口
     * 根据用户的好友关系表
     *
     * @return
     */
    @RequestMapping(value="/getfriends",method = RequestMethod.GET )
    public ResponseDTO getFriendsInfo(){
        try {
            List<Friends> friends = userManageServiceImpl.getFriends();
            Result result = Result.GET_SUCCESS;
            return ResponseUtil.doSuccess(result.getResultCode(),friends,result.getMessage());
        }catch (Exception e){
            Result result = Result.GET_FAILURE;
            throw new  OperationException(result);
        }
    }


    /**
     * 根据userid查询好友关系
     * @param userid
     * @return
     */
    @RequestMapping(value="/getFriendsByUserid/{userid}",method = RequestMethod.GET)
    public ResponseDTO getFriendsByUserid(@PathVariable("userid")@Validated int userid){

        try {
            Friends friends = userManageServiceImpl.getFriendsByUserid(userid);
            Result result = Result.GET_SUCCESS;
            return ResponseUtil.doSuccess(result.getResultCode(),friends,result.getMessage());
        }catch (Exception e){
            Result result = Result.GET_FAILURE;
            throw new  OperationException(result);
        }

    }


    /**
     * 通过添加relation对象添加好友关系，其中@RequestBody表示输入为JSon格式，@valid进行数据验证
     * @param relation
     * @param
     * @return
     */
    @RequestMapping(value="/addFriend",method = RequestMethod.POST)
    public ResponseDTO addFriend( Relation relation){

        int code = userManageServiceImpl.addFriend(relation);
        if(code>0){
            Result result = Result.COMMIT_SUCCESS;
            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),relation,result.getMessage());
            logger.info("保存用户成功");
            return responseDTO;
        }else{
            Result result = Result.COMMIT_FAILURE;
            throw new OperationException(result);
        }


    }



    /**
     * 删除好友关系接口
     * @param relation
     * @return
     */
    @RequestMapping(value="/deletefriend",method = RequestMethod.DELETE,produces = "application/json")
    public ResponseDTO deleteFriend(Relation relation){

        int code = userManageServiceImpl.deleteFriend(relation);
        if(code>0){
            Result result = Result.DELETE_SUCCESS;
            ResponseDTO responseDTO = ResponseUtil.doSuccess(result.getResultCode(),code,result.getMessage());
            return responseDTO;
        }else{
            Result result = Result.DELETE_FAILURE;
            throw new OperationException(result);
        }


    }
}
