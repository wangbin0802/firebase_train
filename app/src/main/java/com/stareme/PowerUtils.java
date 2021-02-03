package com.stareme;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;

import com.stareme.firebase.AppContext;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class PowerUtils {
    public static void listenPower() {
        Observable.interval(20, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d("PowerUtils", getCurrent(AppContext.Companion.getContext()));
            }
        });
    }
    /**
     * 获取当前电流
     */
    private static String getCurrent(Context context) {
        BatteryManager mBatteryManager = (BatteryManager)context.getSystemService(Context.BATTERY_SERVICE);
        long current = 0;
        long capacity = 0;
        long average = 0;
        long energy = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            current = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000;
//            capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//            average = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
//            energy = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
        }
        return "Battery:current:" + current + "\tcapacity-" + capacity + "\taverage-" + average + "\tenergy-" + energy;
    }

}
