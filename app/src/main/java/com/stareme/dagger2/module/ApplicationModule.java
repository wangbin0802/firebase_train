package com.stareme.dagger2.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.stareme.dagger2.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wangbin on 21/7/21.
 */
@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    SharedPreferences provideSharedPref() {
        return mApplication.getSharedPreferences("demo-prefs", Context.MODE_PRIVATE);
    }
}
