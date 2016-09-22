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

import java.util.Date;
import java.util.List;

public class Revision {

    private String revision;
    private Date timestamp;
    private String revisionComment;
    private String user;
    private String emailId;
    private List<ModifiedFile> modifiedFiles;

    public Revision(String revision) {
        this.revision = revision;
    }

    public Revision(String revision, Date timestamp, String user) {
        this.revision = revision;
        this.timestamp = timestamp;
        this.user = user;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRevisionComment() {
        return revisionComment;
    }

    public void setRevisionComment(String revisionComment) {
        this.revisionComment = revisionComment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<ModifiedFile> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(List<ModifiedFile> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

}
