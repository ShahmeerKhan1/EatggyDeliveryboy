
package com.yummiodmkschinky.deliveryboy.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RestResponse {

    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("Next_step")
    private String NextStep;

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return mResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        mResponseMsg = responseMsg;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getNextStep() {
        return NextStep;
    }

    public void setNextStep(String nextStep) {
        NextStep = nextStep;
    }
}
