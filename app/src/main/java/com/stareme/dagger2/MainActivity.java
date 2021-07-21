package com.stareme.dagger2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stareme.dagger2.component.ActivityComponent;
import com.stareme.dagger2.component.DaggerActivityComponent;
import com.stareme.dagger2.module.ActivityModule;
import com.stareme.firebase.MyApplication;
import com.stareme.firebase.R;

import javax.inject.Inject;

/**
 * Created by wangbin on 21/7/21.
 */
public class MainActivity extends AppCompatActivity {

    private TextView accessTokenTv;

    @Inject
    DataManager mDataManager;

    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MyApplication.get(this).getComponent())
                    .build();
        }

        return activityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_layout);

        getActivityComponent().inject(this);

        accessTokenTv = findViewById(R.id.tv_access_token);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDataManager.saveAccessToken("1234567");

        String token = mDataManager.getAccessToken();
        accessTokenTv.setText(token);
    }
}
