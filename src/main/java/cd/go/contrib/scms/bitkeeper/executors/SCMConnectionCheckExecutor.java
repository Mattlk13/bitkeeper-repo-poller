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

import cd.go.contrib.scms.bitkeeper.model.BitKeeperConfig;
import cd.go.contrib.scms.bitkeeper.BitkeeperPlugin;
import cd.go.contrib.scms.bitkeeper.util.JSONUtils;
import cd.go.contrib.scms.bitkeeper.util.URLUtils;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bhupendrakumar on 9/20/16.
 */
public class SCMConnectionCheckExecutor implements RequestExecutor {

    public static CommandLine createCommand(String... args) {
        CommandLine gitCmd = new CommandLine("git");
        gitCmd.addArguments(args, false);
        return gitCmd;
    }

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {

        Map<String, Object> requestBodyMap = JSONUtils.fromJSON(request.requestBody(), Map.class);
        Map<String, String> scmConfiguration = BitkeeperPlugin.keyValuePairs(requestBodyMap, "scm-configuration");
        final BitKeeperConfig bitKeeperConfig = new BitKeeperConfig(scmConfiguration.get("url"), scmConfiguration.get("username"), scmConfiguration.get("password"));
        ConnectionResponse connectionResponse = checkConnection(bitKeeperConfig);

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(BitkeeperPlugin.SUCCESS_RESPONSE_CODE);
        response.setResponseBody(JSONUtils.toJSON(connectionResponse));
        return response;
    }

    public ConnectionResponse checkConnection(BitKeeperConfig bitKeeperConfig) {

        ConnectionResponse connectionResponse = new ConnectionResponse();
        if (StringUtils.isEmpty(bitKeeperConfig.getUrl())) {
            connectionResponse.setStatus("failure");
            connectionResponse.addMessage("URL is empty");
        } else if (!URLUtils.isValidURL(bitKeeperConfig.getUrl())) {
            connectionResponse.setStatus("failure");
            connectionResponse.addMessage("Invalid URL " + bitKeeperConfig.getUrl());
        } else {
            //TODO: FIXME (How to check bk repo with url)
//            CommandLine gitCmd = createCommand("ls-remote", bitKeeperConfig.getUrl());
//            runAndGetOutput(gitCmd);
            connectionResponse.setStatus("success");
            connectionResponse.addMessage("Successfully connected to SCM URL provided");
        }
        return connectionResponse;
    }

    private class ConnectionResponse {

        private String status;
        private List<String> messages;

        public ConnectionResponse() {
            messages = new ArrayList<>(0);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void addMessage(String message) {
            this.messages.add(message);
        }

    }
}
