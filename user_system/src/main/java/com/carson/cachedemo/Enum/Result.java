package com.carson.cachedemo.Enum;

/**
 * <p></p>
 *
 * @author zhanghangfeng5 2019/8/8
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/8
 * @modify by reason:{方法名}:{原因}
 */
public enum Result {
    COMMIT_FAILURE(-1,"提交失败"),COMMIT_SUCCESS(1001,"提交成功"),
    DELETE_FAILURE(-2,"删除失败"),DELETE_SUCCESS(1002,"删除成功"),
    GET_FAILURE(-3,"获取失败"),GET_SUCCESS(1003,"获取成功"),
    UPDATE_FAILURE(-4,"更新失败"),UPDATE_SUCCESS(1004,"更新成功"),
    FRIENDS_EXIST(-5,"好友已存在"),USER_EXIST(-6,"用户已存在"),FRIENDS_NOT_EXIST(-7,"好友关系不存在"),
    LOGIN_FAILURE(-8,"登陆失败"),LOGIN_SUCCESS(2000,"登陆成功"),
    LOGIN_FAILURE_INFO("账号或者密码错误，请重新登陆"),LOGIN_SUCCESS_INFO("恭喜你，登陆成功");

    int resultCode;
    String message;

    Result(String message){
        this.message = message;
    }
    Result(int resultCode, String message){
        this.resultCode =resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
