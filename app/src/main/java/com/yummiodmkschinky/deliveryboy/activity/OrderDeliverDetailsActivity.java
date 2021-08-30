package com.yummiodmkschinky.deliveryboy.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yummiodmkschinky.deliveryboy.R;
import com.yummiodmkschinky.deliveryboy.model.PendingOrderItem;
import com.yummiodmkschinky.deliveryboy.model.Productinfo;
import com.yummiodmkschinky.deliveryboy.model.User;
import com.yummiodmkschinky.deliveryboy.utils.CustPrograssbar;
import com.yummiodmkschinky.deliveryboy.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yummiodmkschinky.deliveryboy.retrofit.APIClient.baseUrl;
import static com.yummiodmkschinky.deliveryboy.utils.SessionManager.currncy;

public class OrderDeliverDetailsActivity extends AppCompatActivity {

    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_address_store)
    TextView txtAddressStore;
    @BindView(R.id.txt_email_store)
    TextView txtEmailStore;
    @BindView(R.id.txt_name_store)
    TextView txtNameStore;

    @BindView(R.id.txt_pmethod)
    TextView txtPmethod;
    @BindView(R.id.txt_qty)
    TextView txtQty;
    @BindView(R.id.txt_total)
    TextView txtTotal;

    @BindView(R.id.img_sing)
    ImageView imgSing;


    ArrayList<Productinfo> productinfoArrayList;
    PendingOrderItem order;
    SessionManager sessionManager;

    CustPrograssbar custPrograssbar;
    User user;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.navigation_product) {
            bottonOrderMakeDecision();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deliver_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Delivered Details");
        getSupportActionBar().setElevation(0f);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        order = (PendingOrderItem) getIntent().getParcelableExtra("MyClass");
        productinfoArrayList = getIntent().getParcelableArrayListExtra("MyList");

        txtQty.setText("QTY : " + productinfoArrayList.size());
        byte[] decodedString;

        if (order.getSign() != null) {
            decodedString = Base64.decode(order.getSign(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgSing.setImageBitmap(decodedByte);
        }
    }

    public void bottonOrderMakeDecision() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.orderlist_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        LinearLayout linearLayout = sheetView.findViewById(R.id.lvl_data);

        for (int i = 0; i < productinfoArrayList.size(); i++) {
            Productinfo listdatum = productinfoArrayList.get(i);
            LayoutInflater inflater = LayoutInflater.from(OrderDeliverDetailsActivity.this);

            View view = inflater.inflate(R.layout.pending_order_item, null);
            ImageView imgView = view.findViewById(R.id.imageView);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtItems = view.findViewById(R.id.txt_items);
            TextView txtWight = view.findViewById(R.id.txt_wight);
            TextView txtPrice = view.findViewById(R.id.txt_price);
            TextView txtPricedis = view.findViewById(R.id.txt_pricedis);
            Glide.with(OrderDeliverDetailsActivity.this).load(baseUrl + listdatum.getProductImage()).placeholder(R.drawable.slider).into(imgView);
            Log.e("url", baseUrl + listdatum.getProductImage());
            txtTitle.setText("" + listdatum.getProductName());
            txtItems.setText(" X " + listdatum.getProductQty() + " Items");
            txtWight.setText(" " + listdatum.getProductWeight() + "");
            if (Double.parseDouble(listdatum.getDiscount()) == 0) {
                txtPricedis.setVisibility(View.GONE);
                txtPrice.setText(sessionManager.getStringData(currncy) + listdatum.getProductPrice() + "");

            } else {
                double ress = (Double.parseDouble(listdatum.getProductPrice()) * Double.parseDouble(listdatum.getDiscount())) / 100;
                ress = Double.parseDouble(listdatum.getProductPrice()) - ress;
                txtPrice.setText(sessionManager.getStringData(currncy) + ress + "");

                txtPricedis.setPaintFlags(txtPricedis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtPricedis.setText(sessionManager.getStringData(currncy) + "" + listdatum.getProductPrice());
            }
            linearLayout.addView(view);
        }

        mBottomSheetDialog.show();


    }




}
