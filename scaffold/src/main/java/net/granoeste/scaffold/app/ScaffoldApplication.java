/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.scaffold.app;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

import net.granoeste.scaffold.BuildConfig;
import net.granoeste.scaffold.di.config.DummyDevelModule;
import net.granoeste.scaffold.di.config.DummyProductionModule;
import net.granoeste.scaffold.di.inject.InjectionApplication;
import net.granoeste.scaffold.util.Tracker;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

@ReportsCrashes(applicationLogFile = "crashReport.log", formKey = "")
public abstract class ScaffoldApplication extends InjectionApplication {
    private static final String TAG = makeLogTag(ScaffoldApplication.class);

    private Tracker mTracker;

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();

        // Injectorを生成して設定する
        setInjector(newInjector());

        // Trackerを生成
        mTracker = newTracker();
    }

    /**
     * Injector
     * アプリでこのメソッドをオーバーライドして、環境別にモジュールの切り替えを行います。
     */
    protected Injector newInjector() {

        Module module = null;
        if (BuildConfig.DEBUG) {
            // デバッグ環境では開発用のモジュールを使う
            module = new DummyDevelModule();
        } else {
            // 本番環境では本番用のモジュールを使う
            module = new DummyProductionModule();
        }

        // Injectorを生成して返す
        return Guice.createInjector(module);
    }

    /**
     * Tracker
     */
    protected Tracker newTracker() {
        return new Tracker() {

            @Override
            public void trackOperation(Fragment fragment, View view) {
                // NOP
            }

            @Override
            public void trackOperation(Activity activity, View view) {
                // NOP
            }

            @Override
            public void trackOperation(Fragment fragment, MenuItem item) {
                // NOP
            }

            @Override
            public void trackOperation(Activity activity, MenuItem item) {
                // NOP
            }

            @Override
            public void trackOperation(Fragment fragment, String operation) {
                // NOP
            }

            @Override
            public void trackOperation(Activity activity, String operation) {
                // NOP
            }

            @Override
            public void trackFragmentStop(Fragment fragment) {
                // NOP
            }

            @Override
            public void trackFragmentStart(Fragment fragment) {
                // NOP
            }

            @Override
            public void trackActivityStop(Activity activity) {
                // NOP
            }

            @Override
            public void trackActivityStart(Activity activity) {
                // NOP
            }
        };
    }

    public Tracker getTracker() {
        return mTracker;
    }

}
