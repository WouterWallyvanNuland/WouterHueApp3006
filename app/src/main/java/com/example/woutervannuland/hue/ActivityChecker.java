package com.example.woutervannuland.hue;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.model.PHBridge;

import java.io.IOException;
import java.util.List;

public interface ActivityChecker {

    void ikHebAccessPointsGevonden(List<PHAccessPoint> dezeVondIk);
    void showHueOnConnectedBridge(PHBridge verbondenBridge);
}
