package com.stareme.dagger2.component;

import android.app.Application;
import android.content.Context;

import com.stareme.dagger2.ApplicationContext;
import com.stareme.dagger2.DataManager;
import com.stareme.dagger2.SharedPrefsHelper;
import com.stareme.dagger2.module.ApplicationModule;
import com.stareme.firebase.MyApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wangbin on 21/7/21.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MyApplication myApplication);

    @ApplicationContext
    Context getContext();

    SharedPrefsHelper getSharedPrefsHelper();

    Application getApplication();

    DataManager getDataManager();
}
