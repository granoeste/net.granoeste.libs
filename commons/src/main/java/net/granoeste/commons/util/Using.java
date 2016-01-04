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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Using class is auto close utility.
 * <p/>
 * <pre>
 * Using.execute(new Using.Runnable() {
 *     @Override
 *    public void run() throws IOException {
 *      FileInputStream is = new FileInputStream();
 *      FileOutputStream os = new FileOutputStream();
 *
 *      register(is);
 *      register(os);
 *
 *      // do your processes.
 *   }
 * });
 * </pre>
 */
public class Using {

    static public abstract class Runnable {

        List<Closeable> closeables = new ArrayList<Closeable>();

        public abstract void run() throws IOException;

        public void register(Closeable closeable) {
            if (closeable == null) {
                throw new IllegalArgumentException("Closeable is null.");
            }
            closeables.add(closeable);
        }

    }

    static public void execute(Runnable runnable) throws Exception {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable is null.");
        }

        try {
            runnable.run();

        } catch (Exception e) {
            throw e;
        } finally {
            for (Closeable closeable : runnable.closeables) {
                CloseUtils.close(closeable);
            }
        }
    }
}
