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

package net.granoeste.scaffold.di.inject;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.logging.Level;

import com.google.inject.Injector;
import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportApplication;

public class InjectionApplication extends LifecycleCallbacksSupportApplication {
	@SuppressWarnings("unused")
	private static final String TAG = InjectionApplication.class.getSimpleName();

	static {
		//Android環境でGuiceが出力するWARNログを抑制する
		java.util.logging.Logger.getLogger(
				com.google.inject.internal.util.$FinalizableReferenceQueue.class.getName())
				.setLevel(Level.SEVERE);
	}

	private ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
	private FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;

	/** 現在設定されているInjectorオブジェクト */
	private Reference<Injector> mInjector;

	/** このアプリケーションで使う{@link Injector}を設定する */
	public Injector setInjector(Injector newInjector) {
		synchronized (this) {
			final Injector previousInjector = (mInjector != null) ? mInjector.get() : null;
			mInjector = new WeakReference<Injector>(newInjector);
			unregisterPreviousCallbacks();
			registerCallbacks(newInjector);
			return previousInjector;
		}
	}

	/** 　Activity/FragmentにDIするためのコールバックオブジェクトを登録する */
	private void registerCallbacks(Injector injector) {

		registerSupportActivityLifecycleCallbacks(new InjectionActivityLifecycleCallbacks(injector));
		registerSupportFragmentLifecycleCallbacks(new InjectionFragmentLifecycleCallbacks(injector));
	}

	/** コールバックがすでに登録されている場合は除去する */
	private void unregisterPreviousCallbacks() {
		if (mActivityLifecycleCallbacks != null) {
			unregisterSupportActivityLifecycleCallbacksCompat(mActivityLifecycleCallbacks);
		}
		if (mFragmentLifecycleCallbacks != null) {
			unregisterSupportFragmentLifecycleCallbacksCompat(mFragmentLifecycleCallbacks);
		}
	}
}
