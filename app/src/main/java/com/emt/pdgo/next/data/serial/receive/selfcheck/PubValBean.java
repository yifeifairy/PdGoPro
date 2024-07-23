package com.emt.pdgo.next.data.serial.receive.selfcheck;

import com.google.gson.annotations.SerializedName;

public class PubValBean {

    @SerializedName("perfuse open")
    public String perfuse;

    @SerializedName("safe open")
    public String safe;

    @SerializedName("supply1 open")
    public String supply;

    @SerializedName("supply2 open")
    public String supply2;

    @SerializedName("drain open")
    public String drain;

    @SerializedName("vaccum open")
    public String vaccum;
}
