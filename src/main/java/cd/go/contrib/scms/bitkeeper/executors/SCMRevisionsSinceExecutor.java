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
import cd.go.contrib.scms.bitkeeper.helper.BitKeeperRepositoryHelper;
import cd.go.contrib.scms.bitkeeper.model.BitKeeperConfig;
import cd.go.contrib.scms.bitkeeper.model.Revision;
import cd.go.contrib.scms.bitkeeper.util.JSONUtils;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cd.go.contrib.scms.bitkeeper.BitkeeperPlugin.LOGGER;

/**
 * Created by bhupendrakumar on 9/21/16.
 */
public class SCMRevisionsSinceExecutor implements RequestExecutor {

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {

        Map<String, Object> requestBodyMap = JSONUtils.fromJSON(request.requestBody(), Map.class);
        Map<String, String> scmConfiguration = BitkeeperPlugin.keyValuePairs(requestBodyMap, "scm-configuration");
        final BitKeeperConfig bitKeeperConfig = new BitKeeperConfig(scmConfiguration.get("url"), scmConfiguration.get("username"), scmConfiguration.get("password"));

        String flyweightFolder = (String) requestBodyMap.get("flyweight-folder");
        LOGGER.info("Flyweight: %s" + flyweightFolder);

        BitKeeperRepositoryHelper bitKeeperRepositoryHelper = new BitKeeperRepositoryHelper(bitKeeperConfig, flyweightFolder);
        bitKeeperRepositoryHelper.cloneOrFetch();

        DefaultGoPluginApiResponse response;

        try {
            List<Revision> revisions = bitKeeperRepositoryHelper.getRevisions();
            revisions.remove(0);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("revision", revisions);
            responseBody.put("scm-data", new HashMap());
            response = new DefaultGoPluginApiResponse(BitkeeperPlugin.SUCCESS_RESPONSE_CODE);
            response.setResponseBody(JSONUtils.toJSON(responseBody));
        } catch (Exception e) {

            response = new DefaultGoPluginApiResponse(BitkeeperPlugin.INTERNAL_ERROR_RESPONSE_CODE);
            response.setResponseBody("Failed to get revisions since " + e.getMessage());
        }
        return response;
    }
}
