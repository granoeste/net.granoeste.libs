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

package net.granoeste.commons.util;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * SystemResources Util
 */
public class SystemResourcesUtils {

    public static void initDrawableResources() {
        for (final Drawable d : Drawable.values()) {
            final int id = Resources.getSystem()
                    .getIdentifier(d.name(), DEF_TYPE_DRAWABLE, DEF_PACKAGE);
            mDrawableResources.put(d.name(), id);
        }
    }

    public static int getDrawableResId(final Drawable d) {
        if (mDrawableResources.isEmpty()) {
            initDrawableResources();
        }
        return mDrawableResources.get(d.name());
    }

    private static Map<String, Integer> mDrawableResources = new HashMap<String, Integer>();
    private static final String DEF_TYPE_DRAWABLE = "drawable";
    private static final String DEF_PACKAGE = "android";

    public static enum Drawable {
        ic_menu_account_list,
        ic_menu_add,
        ic_menu_agenda,
        ic_menu_allfriends,
        ic_menu_always_landscape_portrait,
        ic_menu_archive,
        ic_menu_attachment,
        ic_menu_back,
        ic_menu_block,
        ic_menu_blocked_user,
        ic_menu_call,
        ic_menu_camera,
        ic_menu_cc,
        ic_menu_chat_dashboard,
        ic_menu_clear_playlist,
        ic_menu_close_clear_cancel,
        ic_menu_compass,
        ic_menu_compose,
        ic_menu_crop,
        ic_menu_day,
        ic_menu_delete,
        ic_menu_directions,
        ic_menu_edit,
        ic_menu_emoticons,
        ic_menu_end_conversation,
        ic_menu_forward,
        ic_menu_friendslist,
        ic_menu_gallery,
        ic_menu_goto,
        ic_menu_help,
        ic_menu_home,
        ic_menu_info_details,
        ic_menu_invite,
        ic_menu_login,
        ic_menu_manage,
        ic_menu_mapmode,
        ic_menu_mark,
        ic_menu_month,
        ic_menu_more,
        ic_menu_my_calendar,
        ic_menu_mylocation,
        ic_menu_myplaces,
        ic_menu_notifications,
        ic_menu_play_clip,
        ic_menu_preferences,
        ic_menu_recent_history,
        ic_menu_refresh,
        ic_menu_report_image,
        ic_menu_revert,
        ic_menu_rotate,
        ic_menu_save,
        ic_menu_search,
        ic_menu_send,
        ic_menu_set_as,
        ic_menu_share,
        ic_menu_slideshow,
        ic_menu_sort_alphabetically,
        ic_menu_sort_by_size,
        ic_menu_star,
        ic_menu_start_conversation,
        ic_menu_stop,
        ic_menu_today,
        ic_menu_upload,
        ic_menu_upload_you_tube,
        ic_menu_view,
        ic_menu_week,
        ic_menu_zoom,
        btn_check_buttonless_off,
        btn_check_buttonless_on,
        btn_check_label_background,
        btn_check_off,
        btn_check_off_disable,
        btn_check_off_disable_focused,
        btn_check_off_pressed,
        btn_check_off_selected,
        btn_check_on,
        btn_check_on_disable,
        btn_check_on_disable_focused,
        btn_check_on_pressed,
        btn_check_on_selected,
        btn_circle_disable,
        btn_circle_disable_focused,
        btn_circle_normal,
        btn_circle_pressed,
        btn_circle_selected,
        btn_close_normal,
        btn_close_pressed,
        btn_code_lock_default,
        btn_code_lock_touched,

    }

}
