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

package net.granoeste.commons.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * menu-like list item with icon
 */
public class IconContextMenuItem implements Parcelable {

	/** Title */
	private final CharSequence mText;
	/** Image */
	private final Drawable mImage;
	/** ActionId **/
	private final int mActionId;
	/** Tag **/
	private final Object mTag;

	/**
	 * @return mText
	 */
	public CharSequence getText() {
		return mText;
	}

	/**
	 * @return mImage
	 */
	public Drawable getImage() {
		return mImage;
	}

	/**
	 * @return mActionId
	 */
	public int getActionId() {
		return mActionId;
	}

	/**
	 * @return mTag
	 */
	public Object getTag() {
		return mTag;
	}

	/**
	 * public constructor
	 * 
	 * @param res resource handler
	 * @param textResourceId id of title in resource
	 * @param imageResourceId id of icon in resource
	 * @param actionId indicate action of menu item
	 */
	public IconContextMenuItem(final Resources res,
			final int textResourceId, final int imageResourceId,
			final int actionId) {
		mText = res.getString(textResourceId);
		if (imageResourceId != -1) {
			mImage = res.getDrawable(imageResourceId);
		} else {
			mImage = null;
		}
		mActionId = actionId;
		mTag = null;
	}

	/**
	 * public constructor
	 * 
	 * @param res resource handler
	 * @param textResourceId id of title in resource
	 * @param imageResourceId id of icon in resource
	 * @param actionId indicate action of menu item
	 * @param tag tag of menu item
	 */
	public IconContextMenuItem(final Resources res,
			final int textResourceId, final int imageResourceId,
			final int actionId, final Object tag) {
		mText = res.getString(textResourceId);
		if (imageResourceId != -1) {
			mImage = res.getDrawable(imageResourceId);
		} else {
			mImage = null;
		}
		mActionId = actionId;
		mTag = tag;
	}

	/**
	 * public constructor
	 * 
	 * @param res resource handler
	 * @param title menu item title
	 * @param imageResourceId id of icon in resource
	 * @param actionId indicate action of menu item
	 */
	public IconContextMenuItem(final Resources res,
			final CharSequence title, final int imageResourceId,
			final int actionId) {
		mText = title;
		if (imageResourceId != -1) {
			mImage = res.getDrawable(imageResourceId);
		} else {
			mImage = null;
		}
		mActionId = actionId;
		mTag = null;
	}

	/**
	 * public constructor
	 * 
	 * @param res resource handler
	 * @param title menu item title
	 * @param imageResourceId id of icon in resource
	 * @param actionId indicate action of menu item
	 * @param tag tag of menu item
	 */
	public IconContextMenuItem(final Resources res,
			final CharSequence title, final int imageResourceId,
			final int actionId, final Object tag) {
		mText = title;
		if (imageResourceId != -1) {
			mImage = res.getDrawable(imageResourceId);
		} else {
			mImage = null;
		}
		mActionId = actionId;
		mTag = tag;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeString(mText.toString());
		if (mImage != null) {
			try {
				final Bitmap bitmap = ((BitmapDrawable) mImage).getBitmap();
				final ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
				dest.writeSerializable(stream.toByteArray());
				stream.close();
			} catch (final IOException e) {
				// NOP
			}
		} else {
			dest.writeSerializable(null);
		}
		dest.writeInt(mActionId);
		dest.writeValue(mTag);
	}

	/**
	 * IconContextMenuItem
	 * 
	 * @param in Parcel
	 */
	@SuppressWarnings("deprecation")
	public IconContextMenuItem(final Parcel in) {
		super();
		mText = in.readString();
		final byte[] data = (byte[]) in.readSerializable();
		if (data != null && data.length != 0) {
			final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			mImage = new BitmapDrawable(bitmap);
		} else {
			mImage = null;
		}
		mActionId = in.readInt();
		mTag = in.readValue(null);
	}

	/** CREATOR */
	public static final Creator<IconContextMenuItem> CREATOR = new Creator<IconContextMenuItem>() {

		@Override
		public IconContextMenuItem createFromParcel(final Parcel source) {
			return new IconContextMenuItem(source);
		}

		@Override
		public IconContextMenuItem[] newArray(final int size) {
			return new IconContextMenuItem[size];
		}

	};

}
