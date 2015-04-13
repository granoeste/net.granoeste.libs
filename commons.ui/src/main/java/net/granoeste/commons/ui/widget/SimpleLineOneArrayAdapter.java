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

package net.granoeste.commons.ui.widget;

import net.granoeste.commons.util.UIUtils;
import android.content.Context;
import android.widget.ArrayAdapter;

public class SimpleLineOneArrayAdapter extends ArrayAdapter<String> {

	static final int RESOURCE = UIUtils.hasHoneycomb() ?
			android.R.layout.simple_list_item_activated_2 :
			android.R.layout.simple_list_item_2;

	public SimpleLineOneArrayAdapter(Context context, String[] data) {
		super(context, RESOURCE, data);
	}

}
