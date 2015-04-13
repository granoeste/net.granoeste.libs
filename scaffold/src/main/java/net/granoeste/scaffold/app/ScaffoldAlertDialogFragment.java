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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import net.granoeste.scaffold.R;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

/**
 * AlertDialogFragment
 */
public class ScaffoldAlertDialogFragment extends ScaffoldDialogFragment {
    private static final String TAG = makeLogTag(ScaffoldAlertDialogFragment.class);

    private static final int STYLE = DialogFragment.STYLE_NORMAL;
    private static final int THEME = 0;

    private static final String ICON_ID = "iconId";
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String HAS_POSITIVE = "hasPositive";
    private static final String HAS_NEUTRAL = "hasNeutral";
    private static final String HAS_NEGATIVE = "hasNegative";
    private static final String POSITIVE_TEXT = "positiveText";
    private static final String NEUTRAL_TEXT = "neutralText";
    private static final String NEGATIVE_TEXT = "negativeText";
    private static final String CANCELABLE = "cancelable";
    private static final String CANCELED_ON_TOUCH_OUTSIDE = "canceledOnTouchOutside";

    private final Set<OnAlertDialogEventListener> mOnAlertDialogEventListeners
            = Collections.synchronizedSet(new HashSet<OnAlertDialogEventListener>());

    /**
     * Callbacks
     */
    public interface OnAlertDialogEventListener {
        public void onDialogClick(final DialogInterface dialog, final int whichButton,
                                  final String tag);

        public void onDialogCancel(final DialogInterface dialog, final String tag);
    }

    /**
     * AlertDialogFragment Builder
     *
     * @return builder
     */
    public static ScaffoldAlertDialogFragment.FragmentBuilder builder(Context context, FragmentManager fm) {
        return new ScaffoldAlertDialogFragment.FragmentBuilder(context, fm);
    }

    public static class FragmentBuilder {

        private Context context;
        private FragmentManager fm;
        private Bundle args;
        private String tag;

        private FragmentBuilder(Context context, FragmentManager fm) {
            this.context = context.getApplicationContext();
            this.fm = fm;
            args = new Bundle();
        }

        public ScaffoldAlertDialogFragment build() {
            ScaffoldAlertDialogFragment frag = new ScaffoldAlertDialogFragment();
            frag.setArguments(args);
            return frag;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder iconId(int iconId) {
            args.putInt(ICON_ID, iconId);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder title(int resId) {
            args.putString(TITLE, context.getString(resId));
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder title(String title) {
            args.putString(TITLE, title);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder message(int resId) {
            args.putString(MESSAGE, context.getString(resId));
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder message(String message) {
            args.putString(MESSAGE, message);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder hasPositive(boolean enable) {
            args.putBoolean(HAS_POSITIVE, enable);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder hasNeutral(boolean enable) {
            args.putBoolean(HAS_NEUTRAL, enable);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder hasNegative(boolean enable) {
            args.putBoolean(HAS_NEGATIVE, enable);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder positiveText(int resId) {
            args.putString(POSITIVE_TEXT, context.getString(resId));
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder positiveText(String text) {
            args.putString(POSITIVE_TEXT, text);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder neutralText(int resId) {
            args.putString(NEUTRAL_TEXT, context.getString(resId));
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder neutralText(String text) {
            args.putString(NEUTRAL_TEXT, text);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder negativeText(int resId) {
            args.putString(NEGATIVE_TEXT, context.getString(resId));
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder negativeText(String text) {
            args.putString(NEGATIVE_TEXT, text);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder cancelable(boolean enable) {
            args.putBoolean(CANCELABLE, enable);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder canceledOnTouchOutside(boolean enable) {
            args.putBoolean(CANCELED_ON_TOUCH_OUTSIDE, enable);
            return this;
        }

        public ScaffoldAlertDialogFragment.FragmentBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public void show() {
            this.build().show(fm, tag != null ? tag : "dialog");
        }
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        synchronized (mOnAlertDialogEventListeners) {
            if (activity instanceof OnAlertDialogEventListener) {
                mOnAlertDialogEventListeners.add((OnAlertDialogEventListener) activity);
            }
            Fragment frag = getTargetFragment();
            if (frag != null && frag instanceof OnAlertDialogEventListener) {
                mOnAlertDialogEventListeners.add((OnAlertDialogEventListener) frag);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnAlertDialogEventListeners.clear();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE, THEME);

        // TIPS: AlertDialog.Builder.setCancelable() では有効にならない..?
        final boolean cancelable = getArguments().getBoolean(CANCELABLE, true);
        setCancelable(cancelable);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        int iconId = getArguments().getInt(ICON_ID, 0);
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        boolean hasPositive = getArguments().getBoolean(HAS_POSITIVE, false);
        boolean hasNeutral = getArguments().getBoolean(HAS_NEUTRAL, false);
        boolean hasNegative = getArguments().getBoolean(HAS_NEGATIVE, false);
        String positiveText = getArguments().getString(POSITIVE_TEXT);
        String neutralText = getArguments().getString(NEUTRAL_TEXT);
        String negativeText = getArguments().getString(NEGATIVE_TEXT);
        boolean cancelable = getArguments().getBoolean(CANCELABLE, true);
        boolean canceledOnTouchOutside =
                getArguments().getBoolean(CANCELED_ON_TOUCH_OUTSIDE, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (iconId > 0) {
            builder.setIcon(iconId);
        }
        if (StringUtils.isNoneEmpty(title)) {
            builder.setTitle(title);
        }
        if (StringUtils.isNoneEmpty(message)) {
            builder.setMessage(message);
        }
        if (hasPositive) {
            if (StringUtils.isEmpty(positiveText)) {
                positiveText = getResources().getString(R.string.yes);
            }
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int whichButton) {
                            synchronized (mOnAlertDialogEventListeners) {
                                for (OnAlertDialogEventListener listener : mOnAlertDialogEventListeners) {
                                    listener.onDialogClick(dialog, whichButton, getTag());
                                }
                            }
                        }
                    });
        }
        if (hasNeutral) {
            if (StringUtils.isEmpty(neutralText)) {
                neutralText = getResources().getString(R.string.no);
            }
            builder.setNeutralButton(neutralText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int whichButton) {
                            synchronized (mOnAlertDialogEventListeners) {
                                for (OnAlertDialogEventListener listener : mOnAlertDialogEventListeners) {
                                    listener.onDialogClick(dialog, whichButton, getTag());
                                }
                            }
                        }
                    });
        }
        if (hasNegative) {
            if (StringUtils.isEmpty(negativeText)) {
                negativeText = getResources().getString(hasNeutral ? R.string.cancel : R.string.no);
            }
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int whichButton) {
                            synchronized (mOnAlertDialogEventListeners) {
                                for (OnAlertDialogEventListener listener : mOnAlertDialogEventListeners) {
                                    listener.onDialogClick(dialog, whichButton, getTag());
                                }
                            }
                        }
                    });
        }
        builder.setCancelable(cancelable);
        if (cancelable) {
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(final DialogInterface dialog) {
                    synchronized (mOnAlertDialogEventListeners) {
                        for (OnAlertDialogEventListener listener : mOnAlertDialogEventListeners) {
                            listener.onDialogCancel(dialog, getTag());
                        }
                    }
                }
            });
        }
        View customView = getCustomView();
        if (customView != null) {
            builder.setView(customView);
        }

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

        return dialog;
    }

    /**
     * If you want to include custom view in AlertDialog, You Should be override getCustomView and return custom view.
     *
     * @return custom view
     */
    public View getCustomView() {
        return null;
    }
}
