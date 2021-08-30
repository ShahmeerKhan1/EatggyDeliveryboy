package com.yummiodmkschinky.deliveryboy.fregment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yummiodmkschinky.deliveryboy.R;
import com.yummiodmkschinky.deliveryboy.activity.LoginActivity;
import com.yummiodmkschinky.deliveryboy.activity.ProfileActivity;
import com.yummiodmkschinky.deliveryboy.model.Ostatus;
import com.yummiodmkschinky.deliveryboy.model.RestResponse;
import com.yummiodmkschinky.deliveryboy.model.User;
import com.yummiodmkschinky.deliveryboy.retrofit.APIClient;
import com.yummiodmkschinky.deliveryboy.retrofit.GetResult;
import com.yummiodmkschinky.deliveryboy.utils.CustPrograssbar;
import com.yummiodmkschinky.deliveryboy.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.yummiodmkschinky.deliveryboy.retrofit.APIClient.baseUrl;

public class ProfileFragment extends Fragment implements GetResult.MyListener {


    @BindView(R.id.ed_username)
    TextView edUsername;
    @BindView(R.id.ed_email)
    TextView edEmail;
    @BindView(R.id.ed_phone)
    TextView edPhone;
    @BindView(R.id.switch1)
    Switch switch1;
    @BindView(R.id.txt_complet)
    TextView txtComplet;
    @BindView(R.id.txt_sale)
    TextView txtSale;
    @BindView(R.id.txt_cencel)
    TextView txtCencel;
    @BindView(R.id.txt_recived)
    TextView txtRecived;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txt_case)
    TextView txtCase;
    @BindView(R.id.txt_tips)
    TextView txtTips;

    @BindView(R.id.txt_star)
    TextView txtStar;

    @BindView(R.id.img_profile)
    CircleImageView imgProfile;

    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String EMAIL_KEY = "email_key";

    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";

    String email;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;

    SharedPreferences sharedpreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        ProfileActivity.listener = this;
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        edUsername.setText("" + user.getName());
        edEmail.setText("" + user.getEmail());
        edPhone.setText("" + user.getMobile());

        // getting data from shared prefs and
        // storing it in our string variable.

        sharedpreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        email = sharedpreferences.getString(EMAIL_KEY, null);

        if (sessionManager.getBooleanData("status")) {
            txtStatus.setText("Avaliable");
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
            txtStatus.setText("Not Avaliable");
        }
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                status("1");
                txtStatus.setText("Avaliable");
            } else {
                status("0");
                txtStatus.setText("Not Avaliable");

            }
        });
        orderStatus();
        return view;
    }

    private void status(String key) {
        custPrograssbar.prograsscreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            jsonObject.put("status", key);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void orderStatus() {
        custPrograssbar.prograsscreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());

            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().orderStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closeprograssbar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setBooleanData("status", switch1.isChecked());
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Ostatus ostatus = gson.fromJson(result.toString(), Ostatus.class);
                txtComplet.setText("" + ostatus.getOrderData().getTotalCompleteOrder());
                txtSale.setText(sessionManager.getStringData(SessionManager.currncy) + " " + ostatus.getOrderData().getTotalSale());
                txtCencel.setText("" + ostatus.getOrderData().getTotalRejectOrder());
                txtRecived.setText("" + ostatus.getOrderData().getTotalReceiveorder());
                txtCase.setText(sessionManager.getStringData(SessionManager.currncy)+" "+ostatus.getOrderData().getRiderCashHand());
                txtTips.setText(""+ostatus.getOrderData().getTotalTips());
                txtStar.setText(""+ostatus.getOrderData().getRiderRate());
                Glide.with(getActivity()).load(baseUrl + user.getRimg()).placeholder(R.drawable.slider).into(imgProfile);

                if (ostatus.getOrderData().getRiderStatus().equalsIgnoreCase("1")) {
                    switch1.setChecked(true);
                    sessionManager.setBooleanData("status", true);
                } else {
                    switch1.setChecked(false);
                    sessionManager.setBooleanData("status", false);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onrefaress() {
        if (user != null && sessionManager != null) {
            user = sessionManager.getUserDetails("");
            edUsername.setText("" + user.getName());
            edEmail.setText("" + user.getEmail());
            edPhone.setText("" + user.getMobile());
        }

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @OnClick({R.id.lvl_edit, R.id.lvl_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_edit:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.lvl_logout:


                AlertDialog myDelete = new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout")
                        .setIcon(R.drawable.ic_logout)

                        .setPositiveButton("Logout", (dialog, whichButton) -> {
                            //your deleting code
                            dialog.dismiss();
                            sessionManager.logoutUser();
                            status("0");
                            txtStatus.setText("Not Avaliable");
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            // below line will clear
                            // the data in shared prefs.
                            editor.clear();

                            // below line will apply empty
                            // data to shared prefs.
                            editor.apply();

                            // starting mainactivity after
                            // clearing values in shared preferences.
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();

                        })
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .create();
                myDelete.show();


                break;
            default:
                break;
        }
    }
}
