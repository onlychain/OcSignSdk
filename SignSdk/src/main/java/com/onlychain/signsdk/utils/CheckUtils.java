package com.onlychain.signsdk.utils;

/**
 * 验证签名
 */
public class CheckUtils {

    /**
     * 检查是否属于OC合法地址
     * @param input
     * @return
     */
    public static boolean checkAddress(String input){
        if(input.length()==42){
            String head=input.substring(0,2).toLowerCase();
            String body=input.substring(2,input.length()).toLowerCase();
            String regex="^[A-Fa-f0-9]+$";
            if(head.equals("oc")&&body.matches(regex))
                return true;
        }
        return false;
    }



}
