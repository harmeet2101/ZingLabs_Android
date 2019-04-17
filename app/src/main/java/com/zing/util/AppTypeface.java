package com.zing.util;

import android.content.Context;
import android.graphics.Typeface;



public final class AppTypeface {

    public static Typeface avenieNext_demibold, avenieNext_light, avenieNext_regular,
            avenieNext_medium, avenieNext_bold;

    public static void getTypeFace(Context context) {
        avenieNext_demibold = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-DemiBold.ttf");
        avenieNext_light = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Light.ttf");
        avenieNext_regular = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Regular.ttf");
        avenieNext_medium = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.ttf");
        avenieNext_bold = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Bold.ttf");
    }
}
