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

import android.app.Dialog;
import android.content.Context;

public class DialogUtils {

	public static final int PROGRESS = 0x1;

	public static Dialog create(final Context context, int id) {
		switch (id) {
			case PROGRESS:
				return createProgress(context);
			default:
				break;
		}
		return null;
	}

	public static Dialog createProgress(final Context context) {
		final Dialog dlg = new Dialog(context, R.style.Theme_ProgressDialog);
		dlg.setContentView(R.layout.dialog_progress);
		return dlg;
	}
}
