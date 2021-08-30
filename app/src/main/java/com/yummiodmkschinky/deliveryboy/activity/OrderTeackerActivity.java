package com.yummiodmkschinky.deliveryboy.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yummiodmkschinky.deliveryboy.R;
import com.yummiodmkschinky.deliveryboy.map.FetchURL;
import com.yummiodmkschinky.deliveryboy.map.TaskLoadedCallback;
import com.yummiodmkschinky.deliveryboy.model.PendingOrderItem;
import com.yummiodmkschinky.deliveryboy.model.Productinfo;
import com.yummiodmkschinky.deliveryboy.model.RestResponse;
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

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class OrderTeackerActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GetResult.MyListener {


    @BindView(R.id.crd_accept)
    androidx.cardview.widget.CardView crdAccept;
    @BindView(R.id.txt_pname)
    TextView txtPname;
    @BindView(R.id.txt_paddress)
    TextView txtPaddress;
    @BindView(R.id.txt_cname)
    TextView txtCname;
    @BindView(R.id.txt_caddress)
    TextView txtCaddress;
    @BindView(R.id.txt_km)
    TextView txtKm;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.crd_storeanddeliveryboy)
    CardView crdStoreanddeliveryboy;
    @BindView(R.id.cmd_codamount)
    CardView cmdCodamount;
    @BindView(R.id.txt_point)
    TextView txtPoint;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_icon)
    TextView txtIcon;
    @BindView(R.id.txt_amount)
    TextView txtAmount;
    @BindView(R.id.btn_pickup)
    Button btnPickup;
    @BindView(R.id.btn_delivery)
    Button btnDelivery;


    ArrayList<Productinfo> productinfoArrayList;
    PendingOrderItem order;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    FusedLocationProviderClient fusedLocationProviderClient;

    GoogleMap mMap;
    Marker marker;
    MarkerOptions place1;
    MarkerOptions place2;
    MarkerOptions placePath;
    private Polyline currentPolyline;
    double mLatitude;
    double mLongitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_teacker);
        ButterKnife.bind(this);
        fusedLocationProviderClient = getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        order = (PendingOrderItem) getIntent().getParcelableExtra("MyClass");
        productinfoArrayList = getIntent().getParcelableArrayListExtra("MyList");
        showCurrentLocationOnMap();

    }


    @OnClick({R.id.img_back, R.id.btn_accept, R.id.btn_pickup, R.id.img_reject, R.id.btn_delivery, R.id.img_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                orderUpdate("accept");
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_pickup:
                orderUpdate("pickup");
                break;
            case R.id.btn_delivery:
                orderUpdate("complete");
                break;
            case R.id.img_reject:
                orderUpdate("reject");
                break;
            case R.id.img_call:
                if (order.getOrderStatus().equalsIgnoreCase("Processing")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + order.getPickupMobile()));
                    startActivity(intent);
                } else if (order.getOrderStatus().equalsIgnoreCase("On Route")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + order.getCustomerMobile()));
                    startActivity(intent);

                }

                break;

            default:
                break;
        }
    }

    private void orderUpdate(String status) {
        custPrograssbar.prograsscreate(OrderTeackerActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            jsonObject.put("oid", order.getOrderId());
            jsonObject.put("status", status);
            jsonObject.put("lats", mLatitude);
            jsonObject.put("longs", mLongitude);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getOstatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static PenddingFragment listener;

    public interface PenddingFragment {
        public void onClickItem(String s, PendingOrderItem orderItem);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closeprograssbar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {

                    if (response.getNextStep().equalsIgnoreCase("pickup")) {
                        crdAccept.setVisibility(View.GONE);
                        crdStoreanddeliveryboy.setVisibility(View.VISIBLE);
                        listener.onClickItem("Processing", order);
                        createroot();

                    } else if (response.getNextStep().equalsIgnoreCase("Deliverey")) {
                        crdAccept.setVisibility(View.GONE);
                        crdStoreanddeliveryboy.setVisibility(View.VISIBLE);
                        listener.onClickItem("On Route", order);
                        createroot();
                    } else if (response.getNextStep().equalsIgnoreCase("Done")) {
                        finish();
                    }

                } else {
                    listener.onClickItem("reject", order);

                    finish();
                }
            }
        } catch (Exception e) {
                e.toString();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
        }


    }

    public void createroot() {
        Log.e("lat", mLatitude + " long " + mLongitude);
        switch (order.getOrderStatus()) {
            case "Pending":
                txtPoint.setText("Pickup Point");
                crdAccept.setVisibility(View.VISIBLE);
                crdStoreanddeliveryboy.setVisibility(View.GONE);
                txtPaddress.setText(order.getPickupAddress());
                txtPname.setText("" + order.getPickupName());
                txtCaddress.setText(order.getCustomerAddress());
                txtCname.setText("" + order.getCustomerName());
                txtKm.setText(" " + order.getTotalDistance() + " ");
                txtTime.setText(" " + order.getDeliveryTime() + " ");

                if (mMap != null && marker != null) {

                    animateMarkerToGB(marker, new LatLng(order.getPickupLat(), order.getPickupLong()), new LatLngInterpolator.Spherical());
                } else {
                    place1 = new MarkerOptions().position(new LatLng(order.getPickupLat(), order.getPickupLong())).title(order.getPickupName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
                    LatLng coordinate = new LatLng(order.getPickupLat(), order.getPickupLong());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
                    mMap.animateCamera(yourLocation);
                    marker = mMap.addMarker(place1);

                    marker.showInfoWindow();
                }
                break;
            case "Processing":
                txtPoint.setText("Pickup Point");

                LatLng latLng = new LatLng(mLatitude, mLongitude);
                if (mMap != null && marker != null) {

                    animateMarkerToGB(marker, latLng, new LatLngInterpolator.Spherical());

                } else {
                    placePath = new MarkerOptions().position(latLng).title("Path");
                    place1 = new MarkerOptions().position(new LatLng(order.getPickupLat(), order.getPickupLong())).title(order.getPickupName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
                    place2 = new MarkerOptions().position(latLng).title("Me").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_loc));

                    new FetchURL(OrderTeackerActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                    mMap.animateCamera(yourLocation);
                    marker = mMap.addMarker(place1);
                    mMap.addMarker(place2);
                    marker.showInfoWindow();


                }
                btnDelivery.setVisibility(View.GONE);
                btnPickup.setVisibility(View.VISIBLE);
                crdAccept.setVisibility(View.GONE);
                crdStoreanddeliveryboy.setVisibility(View.VISIBLE);
                txtName.setText("" + order.getPickupName());
                txtAddress.setText("" + order.getPickupAddress());

                break;
            case "On Route":
                txtPoint.setText("Drop Point");
                btnPickup.setVisibility(View.GONE);
                crdAccept.setVisibility(View.GONE);

                crdStoreanddeliveryboy.setVisibility(View.VISIBLE);
                btnDelivery.setVisibility(View.VISIBLE);
                txtName.setText("" + order.getCustomerName());
                txtAddress.setText("" + order.getCustomerAddress());
                if (order.getpMethodName().equalsIgnoreCase("Cash On Delivery")) {
                    cmdCodamount.setVisibility(View.VISIBLE);
                    txtIcon.setText("" + sessionManager.getStringData(SessionManager.currncy));
                    txtAmount.setText("" + sessionManager.getStringData(SessionManager.currncy) + order.getOrderTotal());
                }

                latLng = new LatLng(mLatitude, mLongitude);

                placePath = new MarkerOptions().position(latLng).title("Path");
                place1 = new MarkerOptions().position(new LatLng(order.getDeliveryLat(), order.getDeliveryLong())).title(order.getCustomerName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
                place2 = new MarkerOptions().position(latLng).title("Me").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_loc));

                new FetchURL(OrderTeackerActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mMap.animateCamera(yourLocation);
                marker = mMap.addMarker(place1);
                mMap.addMarker(place2);
                marker.showInfoWindow();

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + order.getOrderStatus());
        }

    }


    public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {

        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);

    }


    public interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class Spherical implements LatLngInterpolator {
            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to) {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);
                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6) {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;
                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);
                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }

            private double computeAngleBetween(double format, double fromLng, double toLat, double toLng) {
                // Haversine's formula
                double dLat = format - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(format) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.api_key);
        return url;
    }

    private void showCurrentLocationOnMap() {

        if (checkAndRequestPermissions()) {

            @SuppressLint("MissingPermission")
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, location -> {
                if (location != null) {
                    mMap.clear();

                    //Go to Current Location
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();

                    if (order != null) {
                        createroot();
                    }
                } else {
                    //Gps not enabled if loc is null
                    Toast.makeText(OrderTeackerActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();

                }
            });
            lastLocation.addOnFailureListener(e -> {
                //If perm provided then gps not enabled
                Toast.makeText(OrderTeackerActivity.this, "Location Not Availabe", Toast.LENGTH_SHORT).show();

            });
        }

    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 2);
            return false;
        }

        return true;

    }


}