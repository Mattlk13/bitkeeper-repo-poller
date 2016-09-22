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
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhupendrakumar on 9/20/16.
 */
public class SCMViewExecutor implements RequestExecutor {

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        return handleSCMView();
    }

    private GoPluginApiResponse handleSCMView() {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        int responseCode = BitkeeperPlugin.SUCCESS_RESPONSE_CODE;
        try {
            responseBody.put("displayValue", "BitKeeper");
            responseBody.put("template", getFileContents("/views/scm.template.html"));
        } catch (IOException e) {
            responseBody.put("errorMessage", "Failed to find template: " + e.getMessage());
            responseCode = BitkeeperPlugin.INTERNAL_ERROR_RESPONSE_CODE;
        }

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        final String json = responseBody == null ? null : JSONUtils.toJSON(responseBody);
        response.setResponseBody(json);
        return response;
    }

    private String getFileContents(String filePath) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(filePath), "UTF-8");
    }
}
