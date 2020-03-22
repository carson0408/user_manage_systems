package com.carson.cachedemo.exception;


import com.carson.cachedemo.Enum.Result;

/**
 * <p></p>
 *操作异常
 * @author zhanghangfeng5 2019/8/8
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/8
 * @modify by reason:{方法名}:{原因}
 */
public class OperationException extends  RuntimeException {

    private int resultCode;

    public OperationException(Result result){
        super(result.getMessage());
        this.resultCode = result.getResultCode();
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
