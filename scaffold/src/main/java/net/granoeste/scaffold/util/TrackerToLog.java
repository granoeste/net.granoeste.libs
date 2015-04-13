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
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.granoeste.commons.util.LogUtils;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

/**
 * オペレーションログを出力するクラス。(トラッカー)
 */
public class TrackerToLog implements Tracker {

    /**
     * オペレーションログを出力
     *
     * @param tag
     * @param operation
     */
    private static void track(final Context context, final String tag, final String operation) {
        LogUtils.LOGD(tag, operation);
    }

    // ---

    @Override
    public void trackActivityStart(final Activity activity) {
        if (activity == null) {
            return;
        }
        track(activity, makeLogTag(((Object) activity).getClass()), "start");
    }

    @Override
    public void trackActivityStop(final Activity activity) {
        if (activity == null) {
            return;
        }
        track(activity, makeLogTag(((Object) activity).getClass()), "stop");
    }

    @Override
    public void trackFragmentStart(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (fragment.getActivity() == null) {
            return;
        }
        track(fragment.getActivity(), makeLogTag(((Object) fragment).getClass()), "start");
    }

    @Override
    public void trackFragmentStop(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (fragment.getActivity() == null) {
            return;
        }
        track(fragment.getActivity(), makeLogTag(((Object) fragment).getClass()), "stop");
    }

    @Override
    public void trackOperation(final Activity activity, final String operation) {
        if (activity == null) {
            return;
        }
        track(activity, makeLogTag(((Object) activity).getClass()), operation);
    }

    @Override
    public void trackOperation(final Fragment fragment, final String operation) {
        if (fragment == null) {
            return;
        }
        if (fragment.getActivity() == null) {
            return;
        }
        track(fragment.getActivity(), makeLogTag(((Object) fragment).getClass()), operation);
    }

    @Override
    public void trackOperation(final Activity activity, final MenuItem item) {
        if (activity == null) {
            return;
        }
        if (item.getTitle() == null) {
            return;
        }
        track(activity, makeLogTag(((Object) activity).getClass()), item.getTitle().toString());
    }

    @Override
    public void trackOperation(final Fragment fragment, final MenuItem item) {
        if (fragment == null) {
            return;
        }
        if (fragment.getActivity() == null) {
            return;
        }
        if (item.getTitle() == null) {
            return;
        }
        track(fragment.getActivity(), makeLogTag(((Object) fragment).getClass()), item.getTitle()
                .toString());
    }

    @Override
    public void trackOperation(final Activity activity, final View view) {
        if (activity == null) {
            return;
        }
        CharSequence operation = null;
        if (view == null) {
            return;
        } else if (view instanceof Button) {
            operation = ((Button) view).getText();
        } else if (view instanceof TextView) {
            operation = ((TextView) view).getText();
        } else if (view instanceof ImageView) {
            operation = String.valueOf(((ImageView) view).getId());
        } else {
            return;
        }
        track(activity, makeLogTag(((Object) activity).getClass()),
                operation != null ? operation.toString() : null);
    }

    @Override
    public void trackOperation(final Fragment fragment, final View view) {
        if (fragment == null) {
            return;
        }
        if (fragment.getActivity() == null) {
            return;
        }
        CharSequence operation = null;
        if (view == null) {
            return;
        } else if (view instanceof Button) {
            operation = ((Button) view).getText();
        } else if (view instanceof TextView) {
            operation = ((TextView) view).getText();
        } else if (view instanceof ImageView) {
            operation = String.valueOf(((ImageView) view).getId());
        } else {
            return;
        }
        track(fragment.getActivity(), makeLogTag(((Object) fragment).getClass()),
                operation != null ? operation.toString() : null);
    }

}
