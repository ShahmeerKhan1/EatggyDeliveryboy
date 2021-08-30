package com.yummiodmkschinky.deliveryboy.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yummiodmkschinky.deliveryboy.R;
import com.yummiodmkschinky.deliveryboy.activity.OrderTeackerActivity;
import com.yummiodmkschinky.deliveryboy.model.PendingOrder;
import com.yummiodmkschinky.deliveryboy.model.PendingOrderItem;
import com.yummiodmkschinky.deliveryboy.model.User;
import com.yummiodmkschinky.deliveryboy.retrofit.APIClient;
import com.yummiodmkschinky.deliveryboy.retrofit.GetResult;
import com.yummiodmkschinky.deliveryboy.utils.CustPrograssbar;
import com.yummiodmkschinky.deliveryboy.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class PendingFragment extends Fragment implements GetResult.MyListener, OrderTeackerActivity.PenddingFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_neworder)
    TextView txtNeworder;
    @BindView(R.id.txt_ongoing)
    TextView txtOngoing;
    @BindView(R.id.txt_complet)
    TextView txtComplet;
    @BindView(R.id.recycle_pending)
    RecyclerView recyclePending;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    List<PendingOrderItem> pendinglistMain = new ArrayList<>();
    PendingAdepter myOrderAdepter;

    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendding, container, false);
        ButterKnife.bind(this, view);
        OrderTeackerActivity.listener = this;
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclePending.setLayoutManager(recyclerLayoutManager);
        getPendingOrder("New");
        return view;
    }

    @OnClick({R.id.txt_neworder, R.id.txt_ongoing, R.id.txt_complet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_neworder:
                getPendingOrder("New");
                txtTitle.setText("New Order");
                txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

                txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.selectcoler));
                txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                break;
            case R.id.txt_ongoing:
                getPendingOrder("Ongoing");
                txtTitle.setText("Live Order");
                txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));

                txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.selectcoler));
                break;
            case R.id.txt_complet:
                getPendingOrder("Completed");
                txtTitle.setText("Past Order");

                txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

                txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                txtComplet.setTextColor(getActivity().getResources().getColor(R.color.selectcoler));
                txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorGrey2));
                break;
            default:
                break;
        }
    }

    private void getPendingOrder(String status) {
        custPrograssbar.prograsscreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getOlist((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

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
                PendingOrder pendingOrder = gson.fromJson(result.toString(), PendingOrder.class);
                if (pendingOrder.getResult().equalsIgnoreCase("true")) {
                    if (pendingOrder.getOrderData().isEmpty()) {
                        txtNodata.setVisibility(View.VISIBLE);
                        txtNodata.setText(""+pendingOrder.getResponseMsg());
                        recyclePending.setVisibility(View.GONE);
                    } else {
                        txtNodata.setVisibility(View.GONE);
                        recyclePending.setVisibility(View.VISIBLE);
                        pendinglistMain = pendingOrder.getOrderData();
                        myOrderAdepter = new PendingAdepter(pendinglistMain);
                        recyclePending.setAdapter(myOrderAdepter);
                    }
                } else {
                    recyclePending.setVisibility(View.GONE);
                    txtNodata.setVisibility(View.VISIBLE);
                    txtNodata.setText(""+pendingOrder.getResponseMsg());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClickItem(String s, PendingOrderItem orderItem) {

        for (int i = 0; i < pendinglistMain.size(); i++) {
            if (pendinglistMain.get(i).getOrderId().equalsIgnoreCase(orderItem.getOrderId())) {
                if (s.equalsIgnoreCase("reject")) {
                    pendinglistMain.remove(i);
                    myOrderAdepter.notifyDataSetChanged();
                } else {
                    orderItem.setOrderStatus(s);
                    pendinglistMain.set(i, orderItem);
                    myOrderAdepter.notifyDataSetChanged();
                }
                break;  // uncomment to get the first instance
            }
        }
    }

    public class PendingAdepter extends RecyclerView.Adapter<PendingAdepter.ViewHolder> {
        private List<PendingOrderItem> pendinglist;

        public PendingAdepter(List<PendingOrderItem> pendinglist) {
            this.pendinglist = pendinglist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pending_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            PendingOrderItem order = pendinglist.get(position);
            holder.txtOderid.setText("OrderID : " + order.getOrderId());

            holder.txtPname.setText("" + order.getPickupName());
            holder.txtCname.setText("" + order.getCustomerName());
            holder.txtStuts.setText(" " + order.getOrderStatus() + " ");
            holder.txtOdate.setText(" " + order.getOrderDate() + " ");
            holder.txtKm.setText(" " + order.getTotalDistance() + " ");
            holder.txtTime.setText(" " + order.getDeliveryTime() + " ");

            holder.lvlClick.setOnClickListener(v -> {
                if(!order.getOrderStatus().equalsIgnoreCase("Completed")){
                    startActivity(new Intent(getActivity(), OrderTeackerActivity.class).putExtra("MyClass", order).putParcelableArrayListExtra("MyList", order.getOrderProductData()));

                }
            });
        }

        @Override
        public int getItemCount() {
            return pendinglist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.lvl_click)
            LinearLayout lvlClick;
            @BindView(R.id.txt_oderid)
            TextView txtOderid;
            @BindView(R.id.txt_odate)
            TextView txtOdate;
            @BindView(R.id.txt_pname)
            TextView txtPname;
            @BindView(R.id.txt_paddress)
            TextView txtPaddress;
            @BindView(R.id.txt_cname)
            TextView txtCname;
            @BindView(R.id.txt_caddress)
            TextView txtCaddress;
            @BindView(R.id.txt_stuts)
            TextView txtStuts;
            @BindView(R.id.txt_km)
            TextView txtKm;
            @BindView(R.id.txt_time)
            TextView txtTime;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
