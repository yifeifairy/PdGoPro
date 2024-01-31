package com.emt.pdgo.next.data.serial.treatment;

public class SerialTreatmentParamBean {

    public SerialTreatmentPrescriptBean prescript;
    public SerialTreatmentDrainBean drain;
    public SerialTreatmentPerfuseBean perfuse;
    public SerialTreatmentSupplyBean supply;
    public SerialTreatmentRetainBean retain;


//    {
//        "request": {
//        "method": "treatment/start",
//                "params": {
//            "prescript": {
//                "totalVolume": 10000,            // 腹透液总量
//                        "cycle": 5,                      // 循环治疗周期
//                        "firstPerfuseVolume": 1500,      // 首次灌注量
//                        "cyclePerfuseVolume": 1000,      // 每周期灌注量
//                        "lastRetainVolume": 1500,        // 上次留腹量
//                        "finalRetainVolume": 1500,       // 末次留腹量
//                        "retainTime": 120,               // 留腹时间
//                        "ultraFiltrationVolume": 500     // 预估超滤量
//            },
//            "drain": {
//                "drainFlowRate": 30,             // 引流流速阈值
//                        "drainFlowPeriod": 60,           // 引流流速周期
//                        "drainRinseVolume": 200,         // 引流冲洗量
//                        "drainRinseTimes": 3,            // 引流次数
//                        "firstDrainPassRate": 60,        // 0周期引流合格率
//                        "drainPassRate": 80,             // 周期引流合格率
//                        "isFinalDrainEmpty": 1,          // 最末引流是否排空
//                        "isFinalDrainEmptyWait": 1,      // 最末引流排空是否等待
//                        "finalDrainEmptyWaitTime": 30,   // 最末引流排空等待时间
//                        "isVaccumDrain": 1               // 是否开启负压引流
//            },
//            "perfuse": {
//                "perfuseFlowRate": 30,           // 灌注流速阈值
//                        "perfuseFlowPeriod": 60,         // 灌注流速周期
//                        "perfuseMaxVolume": 3000         // 最大灌注量
//            },
//            "supply": {
//                "supplyFlowRate": 30,            // 补液流速阈值
//                        "supplyFlowPeriod": 60,          // 补液流速周期
//                        "supplyProtectVolume": 500,      // 补液目标保护值
//                        "supplyMinVolume": 500           // 启动补液的加热袋重量最低值
//            },
//            "retain": {
//                "isFinalRetainDeduct": 1,        // 末次流量扣除
//                        "isFiltCycleOnly": 1             //
//            }
//        }
//    },
//        "sign": "3956807de546faf36d5a0b40205fbea5"
//    }

}
