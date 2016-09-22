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

package cd.go.contrib.scms.bitkeeper;

import cd.go.contrib.scms.bitkeeper.executors.*;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Extension
public class BitkeeperPlugin implements GoPlugin {

    public static final Logger LOGGER = Logger.getLoggerFor(BitkeeperPlugin.class);

    public static final String REQUEST_SCM_CONFIGURATION = "scm-configuration";
    public static final String REQUEST_SCM_VIEW = "scm-view";
    public static final String REQUEST_VALIDATE_SCM_CONFIGURATION = "validate-scm-configuration";
    public static final String REQUEST_CHECK_SCM_CONNECTION = "check-scm-connection";
    public static final String REQUEST_LATEST_REVISION = "latest-revision";
    public static final String REQUEST_LATEST_REVISIONS_SINCE = "latest-revisions-since";
    public static final String REQUEST_CHECKOUT = "checkout";


    public static final int SUCCESS_RESPONSE_CODE = 200;
    public static final int INTERNAL_ERROR_RESPONSE_CODE = 500;
    public static final int NOT_FOUND_RESPONSE_CODE = 404;

    private GoApplicationAccessor goApplicationAccessor;

    public static Map<String, String> keyValuePairs(Map<String, Object> requestBodyMap, String mainKey) {
        Map<String, String> keyValuePairs = new HashMap<String, String>();
        Map<String, Object> fieldsMap = (Map<String, Object>) requestBodyMap.get(mainKey);
        for (String field : fieldsMap.keySet()) {
            Map<String, Object> fieldProperties = (Map<String, Object>) fieldsMap.get(field);
            String value = (String) fieldProperties.get("value");
            keyValuePairs.put(field, value);
        }
        return keyValuePairs;
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("scm", Arrays.asList("1.0"));
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest requestMessage) throws UnhandledRequestTypeException {

        if (requestMessage.requestName().equals(REQUEST_SCM_CONFIGURATION)) {
            return new SCMConfigurationExecutor().execute(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_SCM_VIEW)) {
            return new SCMViewExecutor().execute(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_VALIDATE_SCM_CONFIGURATION)) {
            return new SCMConfigurationValidator().validate(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_CHECK_SCM_CONNECTION)) {
            return new SCMConnectionCheckExecutor().execute(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_LATEST_REVISION)) {
            return new SCMLatestRevisionExecutor().execute(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_LATEST_REVISIONS_SINCE)) {
            return new SCMRevisionsSinceExecutor().execute(requestMessage);

        } else if (requestMessage.requestName().equals(REQUEST_CHECKOUT)) {
            return new SCMCheckoutExecutor().execute(requestMessage);
        } else {
            DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(BitkeeperPlugin.NOT_FOUND_RESPONSE_CODE);
            response.setResponseBody(null);
            return response;
        }
    }
}
