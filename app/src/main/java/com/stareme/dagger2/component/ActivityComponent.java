package com.stareme.dagger2.component;

import com.stareme.dagger2.MainActivity;
import com.stareme.dagger2.PerActivity;
import com.stareme.dagger2.module.ActivityModule;

import dagger.Component;

/**
 * Created by wangbin on 21/7/21.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
