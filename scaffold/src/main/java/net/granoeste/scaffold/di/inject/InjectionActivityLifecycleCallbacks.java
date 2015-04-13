/*
 * Copyright (C) 2012 uPhyca Inc. http://www.uphyca.com/
 *
 * Android Advent Calendar 2012 http://androidadvent.blogspot.jp/
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

package net.granoeste.scaffold.di.inject;

import android.app.Activity;
import android.os.Bundle;

import com.google.inject.Injector;
import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportApplication;

/** ActivityにDIするためのコールバック. */
public class InjectionActivityLifecycleCallbacks extends
		LifecycleCallbacksSupportApplication.SimpleActivityLifecycleCallbacks {

	/** GuiceのInjectorオブジェクト */
	private final Injector mInjector;

	public InjectionActivityLifecycleCallbacks(Injector injector) {
		mInjector = injector;
	}

	/** Activityが生成されたらInjectionする */
	@Override
	public void onActivityCreated(Activity activity,
			Bundle savedInstanceState) {
        // FIXME: NoClassDefFoundError upon injecting instances of @JavascriptInterface
        // NoClassDefFoundError upon injecting instances of @JavascriptInterface annotated class
        // · Issue #221 · roboguice/roboguice https://github.com/roboguice/roboguice/issues/221
		mInjector.injectMembers(activity);
	}

}
