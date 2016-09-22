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

package cd.go.contrib.scms.bitkeeper.executors;

import cd.go.contrib.scms.bitkeeper.BitkeeperPlugin;
import cd.go.contrib.scms.bitkeeper.util.JSONUtils;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

import static cd.go.contrib.scms.bitkeeper.util.FieldUtils.createField;

/**
 * Created by bhupendrakumar on 9/20/16.
 */
public class SCMConfigurationExecutor implements RequestExecutor {

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        return handleSCMConfiguration(request);
    }

    private GoPluginApiResponse handleSCMConfiguration(GoPluginApiRequest requestMessage) {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("url", createField("URL", null, true, true, false, "0"));
        responseBody.put("username", createField("Username", null, false, false, false, "1"));
        responseBody.put("password", createField("Password", null, false, false, true, "2"));

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(BitkeeperPlugin.SUCCESS_RESPONSE_CODE);
        response.setResponseBody(JSONUtils.toJSON(responseBody));

        return response;
    }
}
