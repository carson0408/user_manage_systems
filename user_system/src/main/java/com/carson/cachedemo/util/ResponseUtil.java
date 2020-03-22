package com.carson.cachedemo.util;


import com.carson.cachedemo.commons.ResponseDTO;

/**
 * <p></p>
 *
 * @author zhanghangfeng5 2019/8/8
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/8
 * @modify by reason:{方法名}:{原因}
 */
public class ResponseUtil {


    /**
     * 操作成功，自定义resultcode，data，message
     * @param resultCode
     * @param data
     * @param message
     * @return
     */
    public static ResponseDTO doSuccess(int resultCode, Object data, String message){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode(resultCode);
        responseDTO.setMessage(message);
        responseDTO.setData(data);
        return responseDTO;
    }

    /**
     * 操作成功，可自定义Data和message
     * @param data
     * @param message
     * @return
     */
    public static ResponseDTO doSuccess(Object data,String message){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode(200);
        responseDTO.setMessage(message);
        responseDTO.setData(data);
        return responseDTO;
    }

    /**
     * 操作成功，可定义message
     * @param message
     * @return
     */
    public static ResponseDTO doSuccess(String message){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode(200);
        responseDTO.setMessage(message);
        responseDTO.setData(null);
        return responseDTO;
    }


    /**
     * 请求失败返回
     * @param resultCode
     * @param data
     * @param message
     * @return
     */
    public static ResponseDTO doError(int resultCode,Object data,String message){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode(resultCode);
        responseDTO.setData(data);
        responseDTO.setMessage(message);

        return responseDTO;
    }

    /**
     * 请求失败返回
     * @param resultCode
     * @param message
     * @return
     */
    public static ResponseDTO doError(int resultCode,String message){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode(resultCode);
        responseDTO.setMessage(message);
        responseDTO.setData(null);
        return responseDTO;
    }
}
