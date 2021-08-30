
package com.yummiodmkschinky.deliveryboy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class PendingOrderItem implements Parcelable {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("p_method_name")
    @Expose
    private String pMethodName;
    @SerializedName("customer_address")
    @Expose
    private String customerAddress;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_mobile")
    @Expose
    private String customerMobile;
    @SerializedName("Delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("Coupon_Amount")
    @Expose
    private String couponAmount;
    @SerializedName("Wallet_Amount")
    @Expose
    private String walletAmount;
    @SerializedName("Order_Total")
    @Expose
    private String orderTotal;
    @SerializedName("sign")
    @Expose
    private String sign;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("pickup_name")
    @Expose
    private String pickupName;
    @SerializedName("pickup_mobile")
    @Expose
    private String pickupMobile;
    @SerializedName("pickup_email")
    @Expose
    private String pickupEmail;
    @SerializedName("Order_SubTotal")
    @Expose
    private String orderSubTotal;
    @SerializedName("Order_Transaction_id")
    @Expose
    private String orderTransactionId;
    @SerializedName("Additional_Note")
    @Expose
    private String additionalNote;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("rest_charge")
    @Expose
    private String restCharge;
    @SerializedName("Order_Status")
    @Expose
    private String orderStatus;
    @SerializedName("pickup_lat")
    @Expose
    private double pickupLat;
    @SerializedName("pickup_long")
    @Expose
    private double pickupLong;
    @SerializedName("delivery_lat")
    @Expose
    private double deliveryLat;
    @SerializedName("delivery_long")
    @Expose
    private double deliveryLong;
    @SerializedName("total_distance")
    @Expose
    private String totalDistance;
    @SerializedName("Delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("Order_Product_Data")
    @Expose
    private ArrayList<Productinfo> orderProductData = null;

    protected PendingOrderItem(Parcel in) {
        orderId = in.readString();
        orderDate = in.readString();
        pMethodName = in.readString();
        customerAddress = in.readString();
        customerName = in.readString();
        customerMobile = in.readString();
        deliveryCharge = in.readString();
        couponAmount = in.readString();
        walletAmount = in.readString();
        orderTotal = in.readString();
        pickupAddress = in.readString();
        pickupName = in.readString();
        pickupMobile = in.readString();
        pickupEmail = in.readString();
        orderSubTotal = in.readString();
        orderTransactionId = in.readString();
        additionalNote = in.readString();
        tax = in.readString();
        tip = in.readString();
        restCharge = in.readString();
        orderStatus = in.readString();
        pickupLat = in.readDouble();
        pickupLong = in.readDouble();
        deliveryLat = in.readDouble();
        deliveryLong = in.readDouble();
        totalDistance = in.readString();
        deliveryTime = in.readString();
        orderProductData = in.createTypedArrayList(Productinfo.CREATOR);
    }

    public static final Creator<PendingOrderItem> CREATOR = new Creator<PendingOrderItem>() {
        @Override
        public PendingOrderItem createFromParcel(Parcel in) {
            return new PendingOrderItem(in);
        }

        @Override
        public PendingOrderItem[] newArray(int size) {
            return new PendingOrderItem[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getpMethodName() {
        return pMethodName;
    }

    public void setpMethodName(String pMethodName) {
        this.pMethodName = pMethodName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public String getPickupMobile() {
        return pickupMobile;
    }

    public void setPickupMobile(String pickupMobile) {
        this.pickupMobile = pickupMobile;
    }

    public String getPickupEmail() {
        return pickupEmail;
    }

    public void setPickupEmail(String pickupEmail) {
        this.pickupEmail = pickupEmail;
    }

    public String getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(String orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public String getOrderTransactionId() {
        return orderTransactionId;
    }

    public void setOrderTransactionId(String orderTransactionId) {
        this.orderTransactionId = orderTransactionId;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getRestCharge() {
        return restCharge;
    }

    public void setRestCharge(String restCharge) {
        this.restCharge = restCharge;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public double getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(double pickupLong) {
        this.pickupLong = pickupLong;
    }

    public double getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(double deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public double getDeliveryLong() {
        return deliveryLong;
    }

    public void setDeliveryLong(double deliveryLong) {
        this.deliveryLong = deliveryLong;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public ArrayList<Productinfo> getOrderProductData() {
        return orderProductData;
    }

    public void setOrderProductData(ArrayList<Productinfo> orderProductData) {
        this.orderProductData = orderProductData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(orderDate);
        parcel.writeString(pMethodName);
        parcel.writeString(customerAddress);
        parcel.writeString(customerName);
        parcel.writeString(customerMobile);
        parcel.writeString(deliveryCharge);
        parcel.writeString(couponAmount);
        parcel.writeString(walletAmount);
        parcel.writeString(orderTotal);
        parcel.writeString(pickupAddress);
        parcel.writeString(pickupName);
        parcel.writeString(pickupMobile);
        parcel.writeString(pickupEmail);
        parcel.writeString(orderSubTotal);
        parcel.writeString(orderTransactionId);
        parcel.writeString(additionalNote);
        parcel.writeString(tax);
        parcel.writeString(tip);
        parcel.writeString(restCharge);
        parcel.writeString(orderStatus);
        parcel.writeDouble(pickupLat);
        parcel.writeDouble(pickupLong);
        parcel.writeDouble(deliveryLat);
        parcel.writeDouble(deliveryLong);
        parcel.writeString(totalDistance);
        parcel.writeString(deliveryTime);
        parcel.writeTypedList(orderProductData);
    }
}
