package com.emt.pdgo.next.net;


import android.util.Log;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.net.bean.CommonBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.LoginYhTokenBean;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * api接口调度管理
 *
 * @author chenjh
 * @date 2018/11/28 11:25
 */
public class APIServiceManage extends RetrofitUtil {

    private static APIServiceManage mServiceManage;

    public Gson myGson = new Gson();

    public synchronized static APIServiceManage getInstance() {
        if (mServiceManage == null) {
            mServiceManage = new APIServiceManage();
        }
        return mServiceManage;
    }


    /**
     * 短信接口
     *
     * @param phone
     * @return
     */
//    public rx.Observable<ResponseBaseBean> smsCodeApi(String phone) {
//
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        paramsMap.put("loginphone", phone);
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        String content = myGson.toJson(paramsMap);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//
//        return getService().smsCode(params).compose(this.<ResponseBaseBean>applySchedulers());
//    }


    /**
     * 获取token
     *
     * @return
     */
    public Observable<MyResponse<LoginYhTokenBean>> getToken() {

        Map<String, Object> paramsMap = new HashMap<>();

//        paramsMap.put("username", "18582556015");
//        paramsMap.put("password", "123456");

        paramsMap.put("machineSn", PdproHelper.getInstance().getMachineSN());

        if (myGson == null) {
            myGson = new Gson();
        }
        String content = myGson.toJson(paramsMap);

        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);

        return getService().getObToken(params);
    }

    /**
     * 获取远程处方信息
     */

//    public Observable<HisPrescriptionBean> getRemotePrescription(String machineSn) {
//        return getService().getRemotePrescription(machineSn).compose(this.applySchedulers());
//    }

//    public Observable<AppUpdateBean> appUpdate(String versionCode) {
//        return getService().appUpdate(versionCode).compose(this.applySchedulers());
//    }

//    public Observable<PostCommonBean> postAssess(TreatAssessRequestBean bean) {
//        String content = myGson.toJson(bean);
//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        return getService().postAssess(body).compose(this.applySchedulers());
//    }

    /**
     * 获取历史处方数据
     */
//    public Observable<HisPdListBean> getHisRx(String patientId, String page) {
//        return getService().getHisRx(patientId, page).compose(this.applySchedulers());
//    }

//    public Observable<HisTreatBean> getApdRecordData(String machineSn, int page, int limit ) {
//        return getService().getApdRecordData(machineSn, page, limit).compose(this.applySchedulers());
//    }

    /**
     * 添加处方
     * @param machineSn 机器码
     * @param icodextrinTotal 腹透液总量
     * @param inFlowCycle 每周期灌注量
     * @param cycle 周期
     * @param retentionTime 留腹时间-(分钟)
     * @param lastRetention 最末留腹量
     * @param treatTime 预估治疗时间--（分钟）
     * @param agoRetention 上次留腹量
     * @param predictUlt 本次预估超滤量
     * @param firstInFlow 首次灌注量
     */
//    public Observable<CommonBean> addUserSnMachineRcp(String machineSn,
//                                                      int icodextrinTotal,
//                                                      int inFlowCycle,
//                                                      int cycle,
//                                                      int retentionTime,
//                                                      int lastRetention,
//                                                      String treatTime,
//                                                      int agoRetention,
//                                                      int predictUlt,
//                                                      int firstInFlow) {
//        Map<String, Object> paramsMap = new HashMap<>();
//
////        paramsMap.put("username", "18582556015");
////        paramsMap.put("password", "123456");
//
//        paramsMap.put("machineSn",machineSn);
//        paramsMap.put("icodextrinTotal",icodextrinTotal);
//        paramsMap.put("inFlowCycle",inFlowCycle);
//        paramsMap.put("cycle",cycle);
//        paramsMap.put("retentionTime",retentionTime);
//        paramsMap.put("lastRetention",lastRetention);
//        paramsMap.put("treatTime",treatTime);
//        paramsMap.put("agoRetention",agoRetention);
//        paramsMap.put("predictUlt",predictUlt);
//        paramsMap.put("firstInFlow",firstInFlow);
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        String content = myGson.toJson(paramsMap);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        return getService().addUserSnMachineRcp(params).compose(this.applySchedulers());
//    }

    /**
     * 获取最新版本
     *
     * @param verCode
     * @return
     */
//    public Observable<VersionBeanResponse> checkVersion(int verCode) {
//
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        paramsMap.put("verCode", verCode);
//        paramsMap.put("type", 1);
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        String content = myGson.toJson(paramsMap);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//
//        return getService().checkVersion(params).compose(this.<VersionBeanResponse>applySchedulers());
//    }

    /**
     * 上传治疗数据
     *
     * @return
     */
//    public Observable<RBean> addAPD(TreatmentDataUploadBean mUploadBean) {
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        String content = myGson.toJson(mUploadBean);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        return getService().postAPD(params).compose(this.applySchedulers());
//
//    }

    public void postApdCode(String faultCode) {
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("machineSn", PdproHelper.getInstance().getMachineSN());
        paramsMap.put("faultCode", faultCode);
        paramsMap.put("faultTime", EmtTimeUil.getTime());
//        PostApdCodeBean postApdCodeBean = new PostApdCodeBean();
//        postApdCodeBean.setMachineSn(PdproHelper.getInstance().getMachineSN());
//        postApdCodeBean.setFaultTime(EmtTimeUil.getTime());
//        postApdCodeBean.setFaultCode(faultCode);
        String content = myGson.toJson(paramsMap);
        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().postApdCode(params).enqueue(new Callback<MyResponse<CommonBean>>() {
            @Override
            public void onResponse(Call<MyResponse<CommonBean>> call, Response<MyResponse<CommonBean>> response) {
                if (response.body() != null) {
                    if (response.body().data != null) {
                        Log.e("上传编码数据","faultCode--"+faultCode+"返回的信息--"+response.body().data.getMsg());
                    } else {
                        Log.e("上传编码数据", "数据--"+myGson.toJson(response.body()));
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<CommonBean>> call, Throwable t) {

            }
        });
    }

//    public Observable<CommonBean> postApdCode(String faultCode) {
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        paramsMap.put("machineSn", PdproHelper.getInstance().getMachineSN());
//        paramsMap.put("faultCode", faultCode);
//        paramsMap.put("faultTime", EmtTimeUil.getTime());
////        PostApdCodeBean postApdCodeBean = new PostApdCodeBean();
////        postApdCodeBean.setMachineSn(PdproHelper.getInstance().getMachineSN());
////        postApdCodeBean.setFaultTime(EmtTimeUil.getTime());
////        postApdCodeBean.setFaultCode(faultCode);
//        String content = myGson.toJson(paramsMap);
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        return getService().postApdCode(params).compose(this.transformer);
//    }

//    public void postApdCode(String faultCode) {
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        PostApdCodeBean postApdCodeBean = new PostApdCodeBean();
//        postApdCodeBean.setMachineSn(PdproHelper.getInstance().getMachineSN());
//        postApdCodeBean.setFaultTime(EmtTimeUil.getTime());
//        postApdCodeBean.setFaultCode(faultCode);
//        String content = myGson.toJson(postApdCodeBean);
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        RetrofitUtil.getService().postApdCode(params).enqueue(new Callback<MyResponse<CommonBean>>() {
//            @Override
//            public void onResponse(Call<MyResponse<CommonBean>> call, Response<MyResponse<CommonBean>> response) {
//                if (response.body() != null) {
//                    Log.e("上传编码数据","faultCode--"+faultCode+"返回的信息--"+response.body().data.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse<CommonBean>> call, Throwable t) {
//
//            }
//        });
//    }

//
//    /**
//     * 获取设备登录二维码
//     *
//     * @return
//     */
//    public Observable<String> getMachineQRCode(String sn) {
////        VersonTokenRetroFitFactory factory = new VersonTokenRetroFitFactory();
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        paramsMap.put("id", sn);
//        paramsMap.put("url", "pages/questionnaire/prepage/prepage");
//
//        String url = "pages/questionnaire/prepage/prepage";
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//        String content = myGson.toJson(paramsMap);
//
//
////        RequestBody params = RequestBody.create(
////                MediaType.parse("application/json; charset=utf-8"),
////                content);
//
//        return getService().getMachineQRCode(sn, url).compose(this.<String>applySchedulers());
//
//    }


    /**
     * 获取设备绑定的用户信息
     *
     * @return
     */
//    public Observable<MachineUserTemporaryBean> getMachineUserTemporary(String machineSn) {
////        VersonTokenRetroFitFactory factory = new VersonTokenRetroFitFactory();
//
////        Map<String, Object> paramsMap = new HashMap<>();
////        paramsMap.put("machineSn",machineSn);
////
////        if (myGson == null) {
////            myGson = new Gson();
////        }
////        String content = myGson.toJson(paramsMap);
////
////
////        RequestBody params = RequestBody.create(
////                MediaType.parse("application/json; charset=utf-8"),
////                content);
//
//        return getService().getMachineUserTemporary(machineSn).compose(this.<MachineUserTemporaryBean>applySchedulers());
//
//    }

//    /**
//     * 删除机器临时关联用户
//     *
//     * @param machineSn
//     * @return
//     */
//    public Observable<String> delTemporaryUser(String machineSn) {
//
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        paramsMap.put("machineSn", machineSn);
//
//        if (myGson == null) {
//            myGson = new Gson();
//        }
//
//        String content = myGson.toJson(paramsMap);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//
//        return getService().delTemporaryUser(params).compose(this.<String>applySchedulers());
//    }

//    public void setLocalFaultMsg(Activity activity, String event, String status, String msg) {
//        FaultMsgEntity entity = new FaultMsgEntity();
//        entity.time = EmtTimeUil.getTime();
//        entity.event = event;
//        entity.msg = msg;
//    }


}
