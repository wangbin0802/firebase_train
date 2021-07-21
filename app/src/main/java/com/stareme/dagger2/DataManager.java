package com.stareme.dagger2;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.stareme.dagger2.SharedPrefsHelper.PREF_KEY_ACCESS_TOKEN;

/**
 * Created by wangbin on 21/7/21.
 */
@Singleton
public class DataManager {
    private Context mContext;
    private SharedPrefsHelper mSharePref;

    @Inject
    public DataManager(@ApplicationContext Context context, SharedPrefsHelper sharedPrefsHelper) {
        mContext = context;
        mSharePref = sharedPrefsHelper;
    }

    public void saveAccessToken(String accessToken) {
        mSharePref.put(PREF_KEY_ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken() {
        return mSharePref.get(PREF_KEY_ACCESS_TOKEN, null);
    }
}
