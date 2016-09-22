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

import cd.go.contrib.scms.bitkeeper.executors.SCMConfigurationExecutor;
import cd.go.contrib.scms.bitkeeper.executors.SCMConfigurationValidator;
import cd.go.contrib.scms.bitkeeper.executors.SCMViewExecutor;
import cd.go.contrib.scms.bitkeeper.util.JSONUtils;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by bhupendrakumar on 9/21/16.
 */
public class BitkeeperPluginTest {

    public static final String TEST_DIR = "/tmp/SCM_" + UUID.randomUUID();

    @Test
    public void shouldAbleToServeConfigurationRequest() {

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        GoPluginApiResponse response = new SCMConfigurationExecutor().execute(request);

        Map<String, Object> map = JSONUtils.fromJSON(response.responseBody(), HashMap.class);

        Assert.assertTrue(map.containsKey("url"));
        Assert.assertTrue(map.containsKey("username"));
        Assert.assertTrue(map.containsKey("password"));
    }

    @Test
    public void shouldAbleToServeViewRequest() {

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        GoPluginApiResponse response = new SCMViewExecutor().execute(request);

        Map<String, Object> map = JSONUtils.fromJSON(response.responseBody(), HashMap.class);

        Assert.assertTrue(map.containsKey("displayValue"));
        Assert.assertTrue(map.containsKey("template"));
    }

    @Test
    public void shouldAbleToServeValidateRequest() {

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestBody()).thenReturn("{scm-configuration: {url: {value: \"ssh://bhupendra@bkbits.net/R1\" }}}");

        GoPluginApiResponse response = new SCMConfigurationValidator().validate(request);

    }
}