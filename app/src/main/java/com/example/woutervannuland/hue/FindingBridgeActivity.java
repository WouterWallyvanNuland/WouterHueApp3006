package com.example.woutervannuland.hue;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.woutervannuland.hue.bridgelist.AccessPointPresenter;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;

import nl.rwslinkman.presentable.PresentableAdapter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

public class FindingBridgeActivity extends AppCompatActivity implements ActivityChecker {
    private static final String TAG = "FindingBridgeActivity";
    TextView textView3;
    TextView textViewSetTimer;
    TextView textView1;
    public CountDownTimer afteller;


    private PHHueSDK phHueSDK;
    private WouterHueListener myListener;
    private List<PHAccessPoint> connectedHueList;
    private PresentableAdapter<PHAccessPoint> accessPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_bridge);

        textView1 = (TextView) findViewById(R.id.connectedIpTextView);
        textView3 = (TextView) findViewById(R.id.textViewAskUserToSetIP);
        textView3.setText("Zoeken naar bridges.. ");
        textViewSetTimer = (TextView) findViewById(R.id.textViewSetTimer);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_of_accesspoints);
        recyclerView.setLayoutManager(llm);

        // Create adapter
        List<PHAccessPoint> accessPoints = new ArrayList<>();
        accessPointAdapter = new PresentableAdapter<>(new AccessPointPresenter(), accessPoints);
        recyclerView.setAdapter(accessPointAdapter);


//      https://github.com/rwslinkman/presentablelibrary [RTFM]
    }

    @Override
    protected void onResume() {
        super.onResume();

            afteller = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    textViewSetTimer.setText("Seconds remaining to connect: " + millisUntilFinished / 1000);
                }

                public void onFinish() {

                    Log.e(TAG, "onFinish: Timer afgelopen maar geen bridge!");
//                    toAskIPActivity();
                }
            }.start();



        phHueSDK = PHHueSDK.getInstance();
        myListener = new WouterHueListener(phHueSDK, this);
        phHueSDK.getNotificationManager().registerSDKListener(myListener);



        PHBridgeSearchManager searchManager = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        searchManager.search(true, true);
        Log.d(TAG, "onResume: Search for Hue Bridge started");
    }

    @Override
    public void ikHebAccessPointsGevonden(final List<PHAccessPoint> dezeVondIk) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Met de lijst kun je nog meer doen!

                accessPointAdapter.setData(dezeVondIk);
                accessPointAdapter.notifyDataSetChanged();
                accessPointAdapter.setItemClickListener(new PresentableItemClickListener<PHAccessPoint>() {
                    @Override
                    public void onItemClicked(PHAccessPoint item) {
                        Log.d("Hoi", item.getIpAddress());
                        //eerste in de lijst in een variable zetten om deze mee te geven aan de listener
                        PHAccessPoint eersteInDeLijst = dezeVondIk.get(0);
                        myListener.onAuthenticationRequired(eersteInDeLijst);

                        textViewSetTimer.setVisibility(View.VISIBLE);
                        ImageView bridge = (ImageView) findViewById(R.id.bridgeImageView) ;
                        bridge.setVisibility(View.VISIBLE);
                        afteller.start();

                        // - teller starten en doorvendinden naar connectedlamp als er verbinding is.

                        //afteller.cancel();
                        //toConnectedLampActivity();
                    }
                });


                if(dezeVondIk.size() <= 1)
                textView3.setText("Er is " + dezeVondIk.size() + " bridge gevonden:");
                else {
                    textView3.setText("Er zijn " + dezeVondIk.size() + " bridges gevonden:");
                }



                // als er een item is ingekomen dan moet deze een onclick methode krijgen om door te gaan naar
                // de Lampactivity, via de main waarschijnlijk.

                //  afteller.cancel();
               // toConnectedLampActivity();
            }
        });
    }

    public void showHueOnConnectedBridge(final PHBridge verbondenBridge) {
        Log.d(TAG, "watIkWildeDoen:");

        String verbondenBridgeIP = verbondenBridge.getResourceCache().getBridgeConfiguration().getIpAddress().toString();
        Log.d(TAG, "DE HUE IS VERBONDEN MET HET VOLGENDE IP ADRES: " + verbondenBridgeIP);
        Log.d(TAG, "Stop de teller nu. En ga naar ConnectedLampActivity");

        //Receive all lights from bridge
        List<PHLight> gevondenLampen = verbondenBridge.getResourceCache().getAllLights();
        for (PHLight lamp : gevondenLampen) {
            Log.d(TAG, "watIkWildeDoen: " + lamp.getName());
        }
        afteller.cancel();
        toConnectedLampActivity();
    }

    public void toConnectedLampActivity() {
        // doe ik dat hier of doe ik dat in de main? (Antwoord = HIER!)
        Intent startAct = new Intent(this, LampActivity.class);

        startActivity(startAct);
    }

}


