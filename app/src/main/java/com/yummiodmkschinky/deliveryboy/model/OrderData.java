
package com.yummiodmkschinky.deliveryboy.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderData {

    @SerializedName("total_accept_order")
    private String mTotalAcceptOrder;
    @SerializedName("total_complete_order")
    private String mTotalCompleteOrder;
    @SerializedName("total_reject_order")
    private String mTotalRejectOrder;
    @SerializedName("total_receive_order")
    private String totalReceiveorder;
    @SerializedName("total_sale")
    private String mTotalSale;

    @SerializedName("rider_status")
    private String riderStatus;

    @SerializedName("total_tips")
    private String totalTips;

    @SerializedName("rider_cash_hand")
    private String riderCashHand;

    @SerializedName("rider_rate")
    private String riderRate;

    public String getTotalReceiveorder() {
        return totalReceiveorder;
    }

    public void setTotalReceiveorder(String totalReceiveorder) {
        this.totalReceiveorder = totalReceiveorder;
    }

    public String getTotalAcceptOrder() {
        return mTotalAcceptOrder;
    }

    public void setTotalAcceptOrder(String totalAcceptOrder) {
        mTotalAcceptOrder = totalAcceptOrder;
    }

    public String getTotalCompleteOrder() {
        return mTotalCompleteOrder;
    }

    public void setTotalCompleteOrder(String totalCompleteOrder) {
        mTotalCompleteOrder = totalCompleteOrder;
    }

    public String getTotalRejectOrder() {
        return mTotalRejectOrder;
    }

    public void setTotalRejectOrder(String totalRejectOrder) {
        mTotalRejectOrder = totalRejectOrder;
    }

    public String getTotalSale() {
        return mTotalSale;
    }

    public void setTotalSale(String totalSale) {
        mTotalSale = totalSale;
    }

    public String getRiderStatus() {
        return riderStatus;
    }

    public void setRiderStatus(String riderStatus) {
        this.riderStatus = riderStatus;
    }

    public String getTotalTips() {
        return totalTips;
    }

    public void setTotalTips(String totalTips) {
        this.totalTips = totalTips;
    }

    public String getRiderCashHand() {
        return riderCashHand;
    }

    public void setRiderCashHand(String riderCashHand) {
        this.riderCashHand = riderCashHand;
    }

    public String getRiderRate() {
        return riderRate;
    }

    public void setRiderRate(String riderRate) {
        this.riderRate = riderRate;
    }
}
