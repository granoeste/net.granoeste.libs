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

import static net.granoeste.commons.util.LogUtils.makeLogTag;
import android.os.Bundle;

public abstract class ScaffoldListActivity extends ScaffoldActivity {
    private static final String TAG = makeLogTag(ScaffoldListActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the list fragment and add it as our sole content.
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create an instance of Fragment
            ScaffoldListFragment frag = newListFragment();

            if (frag == null) {
                return;
            }

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            frag.setArguments(intentToFragmentArguments(getIntent()));

            // Add the fragment to the 'list_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
        }

    }

    public ScaffoldListFragment newListFragment() {
        return null;
    }

//  private void setListAdapter(ListAdapter adapter) {
//      BaseListFragment frag = (BaseListFragment) getSupportFragmentManager()
//              .findFragmentById(R.id.list_container);
//      if (frag != null) {
//          frag.setListAdapter(adapter);
//      }
//  }

}
