/*
 * Copyright 2016 Alex Zhang aka. ztc1997
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.evilbinary.highliter;

import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;

public class SelectableScrollingMovementMethod extends ScrollingMovementMethod {
    private static MovementMethod sInstance;

    @Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new SelectableScrollingMovementMethod();
        }

        return sInstance;
    }
}
