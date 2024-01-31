package com.emt.pdgo.next.data.bean;

/**
 * 版本信息bean
 * @author chenjh
 * @date 2019-08-07 14:16
 * 
 */
public class VersionBean extends BaseBean {

    /***
     * 版本号
     */
    public int versionCode;
    /***
     * 版本名称
     */
    public String versionName;
    /***
     * 下载路径
     */
    public String appUrl;
    /***
     * 版本提交时间
     */
    public String createTime;
    /***
     * 更新说明
     */
    public String note;
    /***
     * 是否强制更新 0:否 1:是
     */
    public int isReplace;
//    /***
//     * apk名称
//     */
//    public String apkName;
    /***
     * apk大小，单位为比特
     */
    public long appSzie;

    
}
