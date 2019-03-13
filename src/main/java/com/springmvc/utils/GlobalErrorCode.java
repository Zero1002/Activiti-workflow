package com.springmvc.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GlobalErrorCode {
    //
    SUCESS(200, "Success"),

    //访问权限
    UNAUTHORIZED(401, "Unauthorized"),
    AUTH_UNKNOWN(401000, "登陆校验未知错误"),
    MOBILE_PHONE_VERIFY_CODE_ERROR(401001, "验证码错误"),
    MOBILE_PHONE_USER_NOT_EXIST(401002, "该手机号尚未注册"),
    BAD_CREDENTIALS(401003, "未设置密码"),
    PASSWORD_VERIFY_ERROR(401004, "密码错误"),
    USER_NOT_EXIST(401005, "该用户不存在"),
    WECHAT_OAUTH_CODE_NOT_VAILD(401050, "微信第三方登陆失败, 请重试"),
    WECHAT_USER_NOT_BINDED(401051, "您还没有绑定过微信, 请使用其他方式登录"),
    LOGIN_REGION_NOT_COMMON(401060, "异地登录，请使用验证码或微信扫一扫登录"),
    TOO_MANY_VERIFY_ERROR(401070, "连续三次输错密码，帐号已锁定，请于30分钟后尝试或使用其他方式登录"),
    WECHAT_MINI_PROGRAM_LOGIN_ERROR(401080, "微信小程序登陆失败, 请重试"),
    ALREADY_JOIN_SPACE(401090, "已经加入过该空间，请勿重复申请"),
    NOT_JOIN_SPACE(401110, "您并未加入该文章所属空间"),
    //
    NOT_FOUND(404, "Resource not found"),
    //
    INTERNAL_ERROR(500, "Server internal error"),
    //
    INVALID_ARGUMENT(11001, "Invalid argument"),
    //错误的参数，原参数已修改， 页面需重新刷新
    INVALID_ARGUMENT_2(11002, "Invalid argument"),
    //
    THIRDPLANT_BUZERROR(700, "Business error"),
    //
    UNKNOWN(-1, "Unknown error");

    private static final Map<Integer, GlobalErrorCode> values = new HashMap<Integer, GlobalErrorCode>();

    static {
        for (GlobalErrorCode ec : GlobalErrorCode.values()) {
            values.put(ec.errorCode, ec);
        }
    }

    private int errorCode;
    private String error;

    private GlobalErrorCode(int errorCode, String error) {
        this.errorCode = errorCode;
        this.error = error;
    }

    public static GlobalErrorCode valueOf(int code) {
        GlobalErrorCode ec = values.get(code);
        if (ec != null)
            return ec;
        return UNKNOWN;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }

    public String render() {
        return errorCode + ":" + error;
    }

}
