/*
 * Copyright (C) 2010 Tani Group
 * http://android-demo.blogspot.com/
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

package net.granoeste.commons.ui;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * アイコン付きコンテキストメニュー<br />
 * Fragmentのコールバックの登録は、onAttach()でActivityで実装します。
 * 
 * @author nguyendt
 */
public class IconContextMenu {

	private String mTitle;
	private final ArrayList<IconContextMenuItem> mIconContextMenuItems;

	private static boolean sIsShow;

	/**
	 * constructor
	 */
	public IconContextMenu() {
		mIconContextMenuItems = new ArrayList<IconContextMenuItem>();
	}

	/**
	 * constructor
	 */
	public IconContextMenu(ArrayList<IconContextMenuItem> iconContextMenuItems) {
		mIconContextMenuItems = iconContextMenuItems;
	}

	/**
	 * set title
	 * 
	 * @param menuTitle MenuTitle
	 */
	public void setMenuTitle(final String menuTitle) {
		mTitle = menuTitle;
	}

	/**
	 * Add menu item
	 * 
	 * @param res Resources
	 * @param title Title
	 * @param imageResourceId ImageResourceId
	 * @param actionId actionId
	 */
	public void addItem(final Resources res, final CharSequence title,
			final int imageResourceId, final int actionId) {
		mIconContextMenuItems.add(new IconContextMenuItem(res, title,
				imageResourceId, actionId));
	}

	/**
	 * Add menu item
	 * 
	 * @param res Resources
	 * @param title Title
	 * @param imageResourceId ImageResourceId
	 * @param actionId ActionId
	 * @param tag Tag
	 */
	public void addItem(final Resources res, final CharSequence title,
			final int imageResourceId, final int actionId, final int tag) {
		mIconContextMenuItems.add(new IconContextMenuItem(res, title,
				imageResourceId, actionId, tag));
	}

	/**
	 * @param res Resources
	 * @param textResourceId TextResourceId
	 * @param imageResourceId ImageResourceId
	 * @param actionId ActionId
	 */
	public void addItem(final Resources res, final int textResourceId,
			final int imageResourceId, final int actionId) {
		mIconContextMenuItems.add(new IconContextMenuItem(res, textResourceId,
				imageResourceId, actionId));
	}

	/**
	 * @param res Resources
	 * @param title Title
	 * @param imageResourceId ImageResourceId
	 * @param actionId ActionId
	 * @param tag Tag
	 */
	public void addItem(final Resources res, final CharSequence title,
			final int imageResourceId, final int actionId, final Object tag) {
		mIconContextMenuItems.add(new IconContextMenuItem(res, title,
				imageResourceId, actionId, tag));
	}

	/**
	 * Show dialog
	 * 
	 * @param manager FragmentManager
	 * @param tag Tag
	 */
	public void show(final FragmentManager manager, final String tag) {
		if (sIsShow) {
			return;
		}
		sIsShow = true;
		final IconContextMenuDialogFragment fragment = IconContextMenuDialogFragment.newInstance();
		final Bundle args = new Bundle();
		args.putString(IconContextMenuDialogFragment.ICON_CONTEXT_MENU_TITLE, mTitle);
		args.putSerializable(IconContextMenuDialogFragment.ICON_CONTEXT_MENU_ITEMS,
				mIconContextMenuItems);
		fragment.setArguments(args);
		fragment.show(manager, tag);
		sIsShow = false;
	}

}
