/*
 * Copyright 2016 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.contrib.scms.bitkeeper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by bhupendrakumar on 9/20/16.
 */
public class JSONUtils {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    public static <T> T fromJSON(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static String toJSON(Object object) {
        return GSON.toJson(object);
    }
}
