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

import cd.go.contrib.scms.bitkeeper.*;
import cd.go.contrib.scms.bitkeeper.model.BitKeeperConfig;
import cd.go.contrib.scms.bitkeeper.util.JSONUtils;
import cd.go.contrib.scms.bitkeeper.util.URLUtils;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhupendrakumar on 9/20/16.
 */
public class SCMConfigurationValidator {

    public GoPluginApiResponse validate(GoPluginApiRequest request) {

        Map<String, Object> requestBodyMap = JSONUtils.fromJSON(request.requestBody(), Map.class);
        Map<String, String> scmConfiguration = BitkeeperPlugin.keyValuePairs(requestBodyMap, "scm-configuration");
        final BitKeeperConfig bitKeeperConfig = new BitKeeperConfig(scmConfiguration.get("url"), scmConfiguration.get("username"), scmConfiguration.get("password"));

        List<Map<String, Object>> responseBody = new ArrayList<Map<String, Object>>();
        validate(responseBody, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateUrl(bitKeeperConfig, fieldValidation);
            }
        });

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(BitkeeperPlugin.SUCCESS_RESPONSE_CODE);
        response.setResponseBody(JSONUtils.toJSON(responseBody));
        return response;
    }

    private void validate(List<Map<String, Object>> response, FieldValidator fieldValidator) {
        Map<String, Object> fieldValidation = new HashMap<String, Object>();
        fieldValidator.validate(fieldValidation);
        if (!fieldValidation.isEmpty()) {
            response.add(fieldValidation);
        }
    }

    private void validateUrl(BitKeeperConfig bitKeeperConfig, Map<String, Object> fieldMap) {
        if (StringUtils.isEmpty(bitKeeperConfig.getUrl())) {
            fieldMap.put("key", "url");
            fieldMap.put("message", "URL is a required field");
        } else if (!URLUtils.isValidURL(bitKeeperConfig.getUrl())) {
            fieldMap.put("key", "url");
            fieldMap.put("message", "Invalid URL");
        }
    }
}
