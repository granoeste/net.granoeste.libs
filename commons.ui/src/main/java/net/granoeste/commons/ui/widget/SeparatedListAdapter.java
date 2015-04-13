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

import java.util.LinkedHashMap;
import java.util.Map;

import net.granoeste.commons.ui.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter {

	public final Map<String, Adapter> mSections = new LinkedHashMap<String, Adapter>();
	public final ArrayAdapter<String> mHeaders;
	public final static int TYPE_SECTION_HEADER = 0;

	public SeparatedListAdapter(Context context) {
		this(context, R.layout.list_category);
	}

	public SeparatedListAdapter(Context context, int textViewResourceId) {
		mHeaders = new ArrayAdapter<String>(context, textViewResourceId);
	}

	public void addSection(String section, Adapter adapter) {
		this.mHeaders.add(section);
		this.mSections.put(section, adapter);
	}

	@Override
	public Object getItem(int position) {
		for (Object section : this.mSections.keySet()) {
			Adapter adapter = mSections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return section;
			}
			if (position < size) {
				return adapter.getItem(position - 1);
			}

			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	@Override
	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for (Adapter adapter : this.mSections.values()) {
			total += adapter.getCount() + 1;
		}
		return total;
	}

	@Override
	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for (Adapter adapter : this.mSections.values()) {
			total += adapter.getViewTypeCount();
		}
		return total;
	}

	@Override
	public int getItemViewType(int position) {
		int type = 1;
		for (Object section : this.mSections.keySet()) {
			Adapter adapter = mSections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return TYPE_SECTION_HEADER;
			}
			if (position < size) {
				return type + adapter.getItemViewType(position - 1);
			}

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for (Object section : this.mSections.keySet()) {
			Adapter adapter = mSections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return mHeaders.getView(sectionnum, convertView, parent);
			}
			if (position < size) {
				return adapter.getView(position - 1, convertView, parent);
			}

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
