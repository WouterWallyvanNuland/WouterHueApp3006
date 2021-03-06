package com.example.woutervannuland.hue;

import junit.framework.Assert;

import org.apache.maven.model.Build;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

/**
 * Created by Wouter.van.Nuland on 09/05/2017.
 */

//om er voor te zorgen dat de flow van de app gedaan kan worden maak je de mcontroller aan.
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FindingBridgeActivityTest {

    private ActivityController<FindingBridgeActivity> mController = Robolectric.buildActivity(FindingBridgeActivity.class);
    private FindingBridgeActivity activity;

    @Before
    public void setUp() throws Exception {
        Robolectric.buildActivity(FindingBridgeActivity.class).create();
        this.mController.create().start().resume();
        activity = mController.get();
    }

    @After
    public void tearDown() throws Exception {
        this.mController = null;
        activity = null;
    }

    @Test
    public void created() throws Exception {
        Assert.assertNotNull(activity.mp);
        Assert.assertNotNull(activity.afteller);
        Assert.assertNotNull(activity.textViewAskUserToSelectBridge);

    }


}