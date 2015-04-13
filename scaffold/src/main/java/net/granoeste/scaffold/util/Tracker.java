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

package net.granoeste.scaffold.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

public interface Tracker {

	void trackActivityStart(Activity activity);

	void trackActivityStop(Activity activity);

	void trackFragmentStart(Fragment fragment);

	void trackFragmentStop(Fragment fragment);

	void trackOperation(Activity activity, String operation);

	void trackOperation(Fragment fragment, String operation);

	void trackOperation(Activity activity, MenuItem item);

	void trackOperation(Fragment fragment, MenuItem item);

	void trackOperation(Activity activity, View view);

	void trackOperation(Fragment fragment, View view);

}