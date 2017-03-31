package com.example.woutervannuland.hue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;


import java.util.List;
import java.util.Timer;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    // create all variables that i need in my Main.
    private PHHueSDK phHueSDK;
    private WouterHueListener myListener;
    private PHBridge verbondenBridge;
    private List<PHLight> connectedHueList;

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // first create all buttons and catch them by ID.
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);

        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener (this);

        phHueSDK = PHHueSDK.getInstance();
        phHueSDK.setAppName("WouterHue");
        phHueSDK.setDeviceName(android.os.Build.MODEL);

    }

    @Override
    protected void onResume() {
        super.onResume();

        myListener = new WouterHueListener(phHueSDK, this);
        phHueSDK.getNotificationManager().registerSDKListener(myListener);

        PHBridgeSearchManager searchManager = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        searchManager.search(true, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        phHueSDK.getNotificationManager().unregisterSDKListener(myListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                // both lights to BLUE
                if (verbondenBridge != null) {
                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, changeHue0ToColorBlue());
                    }
                    System.out.println("Changed both lights to color Blue via button!");

                    break;
                }
            case R.id.button2:
                // both lights to RED
                if (verbondenBridge != null) {
                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, changeHue0ToColorRed());
                    }
                    System.out.println("Changed both lights to color Red via button!");

                    break;
                }

            case R.id.button3:
                // both lights to GREEN
                if (verbondenBridge != null) {
                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, changeHue0ToColorGreen());
                    }
                    System.out.println("Changed both lights to color Green via button!");

                    break;
                }


            case R.id.button4:
                // both lights need to stop with their colorLoop. Change back to last known color.
                if (verbondenBridge != null) {
                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, loopStop0());
                    }
                    System.out.println("Quit loop and go back to last known color before loop started ");

                    break;
                }

            case R.id.button5:
                // both lights to ColorLoop mode!
                if (verbondenBridge != null) {
                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, loopEffectHue0());
                    }
                    System.out.println("HoopdiLoop! Demonstrating all the colors in a loop on both lights ");

                    break;
                }

            case R.id.button6:
                // both ligths to YELLOW!
                if (verbondenBridge != null) {
//                    for (int i=0; i < connectedHueList.size(); i++ ) {
//                        verbondenBridge.updateLightState(connectedHueList.get(i), changeHue0ToColorYellow());
//                    }

                    for (PHLight thisConnectedHueList : connectedHueList){
                        verbondenBridge.updateLightState(thisConnectedHueList, changeHue0ToColorYellow());
                    }


                }

        }
    }

    public void showHueOnConnectedBridge(PHBridge verbondenBridge) {
        Log.d(TAG, "watIkWildeDoen:");

        //Receive all lights from bridge
        List<PHLight> gevondenLampen = verbondenBridge.getResourceCache().getAllLights();

        for (PHLight lamp : gevondenLampen) {
            Log.d(TAG, "watIkWildeDoen: " + lamp.getName());
            Log.d(TAG, "showHueOnConnectedBridge: " + lamp.getUniqueId());
            Log.d(TAG, "showHueOnConnectedBridge: " + lamp.getIdentifier());
            }
    }

    public void changingLightColors(PHBridge receivedBridge) {

        // DE BRIDGE DIE GEVONDEN IS, LOKAAL MAKEN IN DE MAIN CLASS
        this.verbondenBridge = receivedBridge;


        // om alle HUE lampen uit de bridge te lezen.
        connectedHueList = receivedBridge.getResourceCache().getAllLights();

    }


    public PHLightState loopEffectHue0() {
        final PHLightState lightState0 = new PHLightState();
        lightState0.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);

        return lightState0;
    }

    public PHLightState loopEffectHue1() {
        final PHLightState lightState1 = new PHLightState();
        lightState1.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);

        return lightState1;
    }

    public PHLightState loopStop0() {
        PHLightState lightState0 = new PHLightState();
        lightState0.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);

        return lightState0;
    }

    public PHLightState loopStop1() {
        PHLightState lightState1 = new PHLightState();
        lightState1.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);

        return lightState1;
    }


    public PHLightState changeHue0ToColorYellow() {
        final PHLightState yellowState0 = new PHLightState();
        yellowState0.setHue(12750, true);
//        yellowState.setX(0.5425f);
//        yellowState.setY(0.4196f);

        return yellowState0;
    }

    public PHLightState changeHue0ToColorGreen() {
        final PHLightState greenState0 = new PHLightState();
        greenState0.setX(0.4100f);
        greenState0.setY(0.51721f);

        return greenState0;
    }

    public PHLightState changeHue0ToColorBlue() {
        final PHLightState blueState0 = new PHLightState();
        blueState0.setHue(46920, true);

        //   blueState.setX(0.1691f);
        //   blueState.setY(0.0441f);

        return blueState0;
    }

    public PHLightState changeHue0ToColorRed() {
        final PHLightState redState0 = new PHLightState();
        redState0.setHue(0, true);

//        redState.setX(0.6750f);
//        redState.setY(0.3223f);

        return redState0;
    }

    public PHLightState changeHue1ToColorYellow() {
        final PHLightState yellowState1 = new PHLightState();
        yellowState1.setHue(12750, true);
//        yellowState.setX(0.5425f);
//        yellowState.setY(0.4196f);

        return yellowState1;
    }

    public PHLightState changeHue1ToColorBlue() {
        final PHLightState blueState1 = new PHLightState();
        blueState1.setHue(46920, true);
        //  blueState.setX(0.1691f);
        //  blueState.setY(0.0441f);

        return blueState1;
    }

    public PHLightState changeHue1ToColorRed() {
        final PHLightState redState1 = new PHLightState();
        redState1.setHue(0, true);
        // redState.setX(0.6750f);
        // redState.setY(0.3223f);

        return redState1;
    }

}
