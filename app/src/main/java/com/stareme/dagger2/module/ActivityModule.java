package com.stareme.dagger2.module;

import android.app.Activity;
import android.content.Context;

import com.stareme.dagger2.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wangbin on 21/7/21.
 */
@Module
public class ActivityModule {
    private Activity mActivity;
    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}
