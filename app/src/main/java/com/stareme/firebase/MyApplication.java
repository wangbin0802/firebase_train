package com.stareme.firebase;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.stareme.dagger2.DataManager;
import com.stareme.dagger2.component.ApplicationComponent;
import com.stareme.dagger2.component.DaggerApplicationComponent;
import com.stareme.dagger2.module.ApplicationModule;

import javax.inject.Inject;

/**
 * Created by wangbin on 21/7/21.
 */
public class MyApplication extends MultiDexApplication {
    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager mDataManager;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }
}
