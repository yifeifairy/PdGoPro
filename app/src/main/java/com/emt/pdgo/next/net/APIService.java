package com.emt.pdgo.next.net;

import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.net.bean.AppUpdateBean;
import com.emt.pdgo.next.net.bean.CommonBean;
import com.emt.pdgo.next.net.bean.HisPdListBean;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.HisTreatBean;
import com.emt.pdgo.next.net.bean.MachineUserTemporaryBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.PostCommonBean;
import com.emt.pdgo.next.net.bean.ResponseBaseBean;
import com.emt.pdgo.next.net.bean.UserInfoBean;
import com.emt.pdgo.next.net.bean.VersionBeanResponse;
import com.emt.pdgo.next.net.bean.upload.LoginYhTokenBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 所有的接口api
 *
 * @author chenjh
 * @date 2018/11/27 16:37
 */
public interface APIService {


//    @POST("auth/jwt/loginYhToken")
    @POST("auth/jwt/machineSnAndPhoneToken")
    Observable<MyResponse<LoginYhTokenBean>> getObToken(@Body RequestBody params);

    @POST("auth/jwt/machineSnAndPhoneToken")
    Call<MyResponse<LoginYhTokenBean>> getToken(@Body RequestBody params);

    @POST("auth/jwt/machineSnAndPhoneToken")
    Call<MyResponse<LoginYhTokenBean>> getCallToken(@Body RequestBody params);

//    /**
//     * 新增用户处方
//     * @param params 参数
//     * @return call
//     */
//    @POST("ckdcloud/apdRecipel/addUserSnMachineRcp")
//    Call<MyResponse<CommonBean>> addUserSnMachineRcp(@Body RequestBody params);

    // 远程处方数据
    @GET("ckdcloud/apdRecipel/getMachineUserRcp")
    Call<MyResponse<HisPrescriptionBean>> getRemotePrescription(@Query("machineSn") String machineSn);

    @POST("pro/proData/versionInfo")
    Call<MyResponse<VersionBeanResponse>> checkVersion(@Body RequestBody params);

    @POST("pro/proData/smsCode")
    Call<MyResponse<ResponseBaseBean>> smsCode(@Body RequestBody params);

    @POST("pro/proData/customer")
    Call<MyResponse<UserInfoBean>> getUserInfo(@Body RequestBody params);

//    //上传治疗数据
//    @POST("ckdcloud/treatmentRecord/addAPD")
//    Call<MyResponse<ResponseBaseBean>> addAPD(@Body RequestBody params);

//    //上传治疗数据
    @POST("pro/treatmentRecord/addAPD")
    Call<MyResponse<RBean>> postAPD(@Body RequestBody params);

    @GET("ckdcloud/wx/getMachineQRCode")//https://iot.ckdcloud.com/api/ckdcloud/wx/getMachineQRCode?id=PD00001A&url=pages/questionnaire/prepage/prepage
    Observable<MyResponse<String>> getMachineQRCode(@Query("id") String id, @Query("url") String url);

    // 历史处方数据
    @GET("ckdcloud/apdRecipel/getUserMachineLogRcpForPage")
    Call<MyResponse<HisPdListBean>> getHisRx(@Query("patientId") String patientId, @Query("page") String page);

    //查询机器临时关联用户信息
    @GET("ckdcloud/wx/getMachineUserTemporary")
    Call<MyResponse<MachineUserTemporaryBean>> getMachineUserTemporary(@Query("machineSn") String machineSn);

    //删除机器临时关联用户
    @POST("ckdcloud/wx/delTemporaryUser")
    Call<MyResponse<String>> delTemporaryUser(@Body RequestBody params);

    @GET("ckdcloud/wx/downloadAndroidVersion")
    Call<MyResponse<AppUpdateBean>> appUpdate(@Query("versionCode") String versionCode,
                                              @Query("versionName") String versionName,
                                              @Query("machineSn") String machineSn,
                                              @Query("versionType") String versionType);

    @GET("ckdcloud/wx/getApdRecordDataForMachine")
    Call<MyResponse<HisTreatBean>> getApdRecordData(@Query("machineSn") String machineSn,
                                        @Query("page") int page, @Query("limit") int limit);

    @POST("ckdcloud/wx/saveApdFaultCode")
    Call<MyResponse<CommonBean>> postApdCode(@Body RequestBody params);

    @POST("ckdcloud/wx/saveApdRecordDataRate")
    Call<MyResponse<PostCommonBean>> postAssess(@Body RequestBody body);

}
