package com.example.jingjing.xin.constant;

/**
 * Created by jingjing on 2018/5/8.
 */

public class Conatant {

   //public static String URL = "http://192.168.1.105:8080/"; //连寝室wifi IP地址
    public static String URL = "http://10.0.2.2:8080/"; //手机热点IP地址(8080是端口号)
    public static String URL_LOGIN  = URL+"Login_Servlet";//登录
    public static String URL_Register = URL+"Register_Servlet";//注册
    public static String URL_UpdateUser = URL+"UpdateUser_Servlet";//修改信息
    public static String URL_SELECTUSERBYUSERID = URL+"SelectUserByUserId_Servlet";//用户信息
    public static String URL_LOADINGORDER = URL+"LoadingOrder_Servlet";
    public static String URL_PICTURE = URL+"Image/";
    public static String URL_NOTICE = URL+"Notice_Servlet";//公告栏
    public static String URL_SEARCHSTADIUM = URL+"SearchStadiumByName_Servlet";//寻找场地
    public static String URL_ORDERSTADIUM = URL+"OrderStadium_Servlet";//预约场地
    public static String URL_ORDERINFORMATION = URL+"OrderInformation_Servlet";//预约信息
    public static String URL_DELETEORDERINFORMATION = URL+"DeleteOrderInformation_Servlet";//删除预约信息
    public static String URL_PLACENAME = URL+"PlaceName_Servlet";//场地
    public static String URL_INSERTNEED = URL+"InsertNeed_Servlet";//发布需求
    public static String URL_SENDPICTURE = URL+"Send_Servlet";
    public static String URL_DELETENEEDINFORMATION = URL+"DeleteNeedInformation_Servlet";//删除发布需求
    public static String URL_NEEDINFORMATION = URL+"NeedInformation_Servlet";//需求信息
    public static String URL_FINDINFORMATION = URL+"FindInformation_Servlet";//发现
    public static String URL_UPDATEPASSWORD = URL+"UpdatePassword_Servlet";//修改密码
    public static String URL_JOINFIND = URL+"JoinFind_Servlet";//加入需求
    public static String URL_INSERTCOLLECTION = URL+"InsertCollection_Servlet";//添加收藏
    public static String URL_DELETECOLLECTION = URL+"DeleteCollection_Servlet";//取消收藏
    public static String URL_ISCOLLECTED = URL+"IsCollected_Servlet";//记住收藏状态
    public static String URL_SEARCHCOLLECTSTADIUM = URL+"SearchCollectStadium_Servlet";//我的收藏
    public static String URL_DELETEJOINFIND = URL+"DeleteJoinFind_Servlet";//取消加入

    public static String URL_SPORTSTYPE = URL+"SportsIcon/";
    public static String URL_PROFLIE = URL+"Proflie/";//上传头像
    public static String URL_EVALUATEINFORMATION = URL+"EvaluateInformation_Servlet";
    public static String URL_EVALUATESTADIUM = URL+"EvaluateStadium_Servlet";
    public static String URL_SELECTEVALUATION = URL+"SelectEvaluation_Servlet";
    public static String URL_SPORTS = URL+"LoadingSports_Servlet";
    public static String URL_CITY = URL+"LoadingCity_Servlet";
    public static String URL_LOADINGICON = URL+"LoadingIcon_Servlet";
    public static String URL_GETEVALUATEINFORMATION = URL+"GetEvaluateInformation_Servlet";
    public static String URL_JOINEDUSERINFORMATION = URL+"JoinedUserInformation_Servlet";
    public static String URL_UPDATEPROFILE = URL+"UpdateProfile_Servlet";
}