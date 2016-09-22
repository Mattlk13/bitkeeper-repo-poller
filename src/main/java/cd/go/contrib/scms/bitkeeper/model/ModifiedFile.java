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

package cd.go.contrib.scms.bitkeeper.model;

/**
 * Created by bhupendrakumar on 9/21/16.
 */
public class ModifiedFile {

    private String fileName;
    private String action;

    public ModifiedFile(String fileName, String action) {
        this.fileName = fileName;
        this.action = action;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAction() {
        return action;
    }
}
