package com.example.woutervannuland.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.List;

public class FindingBridgeActivity extends AppCompatActivity implements ActivityChecker {
    private static final String TAG = "FindingBridgeActivity";
    private String ip = "";
    TextView textView3;
    TextView textView1;

    private PHHueSDK phHueSDK;
    private WouterHueListener myListener;
    private PHBridge verbondenBridge;
    private List<PHAccessPoint> connectedHueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_bridge);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
    }



    @Override
    protected void onResume() {
        super.onResume();

        phHueSDK = PHHueSDK.getInstance();
        myListener = new WouterHueListener(phHueSDK, this);
        phHueSDK.getNotificationManager().registerSDKListener(myListener);

        PHBridgeSearchManager searchManager = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        searchManager.search(true, true);







    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> list) {
        Log.d(TAG, "onAccessPointsFound: ");
        for(PHAccessPoint ap : list) {
            System.out.println("Komt ie hier uberHAUPT wel voorbij?");
            phHueSDK.connect(ap);
            Log.d(TAG, "onAccessPointsFound: " + ap.getIpAddress());
            Log.d(TAG, "onAccessPointsFound: " + ap.getMacAddress());
            Log.d(TAG, "onAccessPointsFound: " + ap.getBridgeId());

            // to set the last known ipAddress and Username.
            //      ap.setIpAddress("172.16.10.81");
            //     ap.setUsername("null");

            if (ap.getIpAddress().equals(ip)) {
                phHueSDK.connect(ap);
                Log.d(TAG, "onAccessPointsFound: " + " connected" + ip);
            }

            //store every bridge in listview



        }
    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
        phHueSDK.startPushlinkAuthentication(phAccessPoint);

        // TODO show pushlink image and timer.

        Log.d(TAG, "onAuthenticationRequired: WOUTER TAKE IT TO THE BRIDGE!");

        // play sample
        Log.d(TAG, "onAuthenticationRequired: " + phAccessPoint.getIpAddress());
    }


    public void showHueOnConnectedBridge(PHBridge verbondenBridge) {
        Log.d(TAG, "watIkWildeDoen:");

        //Receive all lights from bridge
        List<PHLight> gevondenLampen = verbondenBridge.getResourceCache().getAllLights();

        // lijst nog tonen aan user.

        for (PHLight lamp : gevondenLampen) {
            Log.d(TAG, "watIkWildeDoen: " + lamp.getName());
            Log.d(TAG, "showHueOnConnectedBridge: " + lamp.getUniqueId());
            Log.d(TAG, "showHueOnConnectedBridge: " + lamp.getIdentifier());
        }




    }

    @Override
    public void changingLightColors(PHBridge receivedBridge) {

    }

    public void toConnectedLampActivity() {
        // doe ik dat hier of doe ik dat in de main?
    }

}

