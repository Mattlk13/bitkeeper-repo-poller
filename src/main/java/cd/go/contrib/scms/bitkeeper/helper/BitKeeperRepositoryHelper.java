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

package cd.go.contrib.scms.bitkeeper.helper;

import cd.go.contrib.scms.bitkeeper.cmd.Console;
import cd.go.contrib.scms.bitkeeper.cmd.InMemoryConsumer;
import cd.go.contrib.scms.bitkeeper.cmd.ProcessOutputStreamConsumer;
import cd.go.contrib.scms.bitkeeper.model.BitKeeperConfig;
import cd.go.contrib.scms.bitkeeper.model.ModifiedFile;
import cd.go.contrib.scms.bitkeeper.model.Revision;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bhupendrakumar on 9/21/16.
 */
public class BitKeeperRepositoryHelper {

    public static final String CHANGE_SET = "ChangeSet@";
    private BitKeeperConfig bitKeeperConfig;
    private File workingDir;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public BitKeeperRepositoryHelper(BitKeeperConfig bitKeeperConfig, String workingDir) {
        this.bitKeeperConfig = bitKeeperConfig;
        this.workingDir = new File(workingDir);
    }

    public void cloneOrFetch() {
        if (!isBitKeeperRepository() || !isSameRepository()) {
            setupWorkingDir();
            cloneRepository();
        }
        updateToMaster();
    }

    private void updateToMaster() {
        CommandLine bitKeeperClone = Console.createCommand("pull", bitKeeperConfig.getEffectiveUrl());
        Console.runOrBomb(bitKeeperClone, workingDir, new ProcessOutputStreamConsumer(new InMemoryConsumer()), new ProcessOutputStreamConsumer(new InMemoryConsumer()));
    }

    public void cloneRepository() {
        CommandLine bitKeeperClone = Console.createCommand("clone", bitKeeperConfig.getEffectiveUrl(), workingDir.getAbsolutePath());
        Console.runOrBomb(bitKeeperClone, workingDir, new ProcessOutputStreamConsumer(new InMemoryConsumer()), new ProcessOutputStreamConsumer(new InMemoryConsumer()));
    }

    private void setupWorkingDir() {
        FileUtils.deleteQuietly(workingDir);
        try {
            FileUtils.forceMkdir(workingDir);
        } catch (IOException e) {
            new RuntimeException("Could not create directory: " + workingDir.getAbsolutePath());
        }
    }

    private boolean isBitKeeperRepository() {
        File bitKeeper = new File(workingDir, ".bk");
        return workingDir.exists() && bitKeeper.exists() && bitKeeper.isDirectory();
    }

    public boolean isSameRepository() {
        try {
            return workingRepositoryUrl().contains(bitKeeperConfig.getEffectiveUrl());
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> workingRepositoryUrl() {
        CommandLine bitKeeperURL = Console.createCommand("parent", "-l");
        return Console.runOrBomb(bitKeeperURL, workingDir, new ProcessOutputStreamConsumer(new InMemoryConsumer()), new ProcessOutputStreamConsumer(new InMemoryConsumer())).stdOut();
    }

    public List<Revision> getRevisions() throws ParseException {
        CommandLine bitKeeperURL = Console.createCommand("changes", "-v");
        List<String> outputLines = Console.runOrBomb(bitKeeperURL, workingDir, new ProcessOutputStreamConsumer(new InMemoryConsumer()), new ProcessOutputStreamConsumer(new InMemoryConsumer())).stdOut();
        List<String> changeSets = getChangeSet(outputLines);
        List<Revision> revisions = new ArrayList<>();

        Revision revision = null;
        for (String line : changeSets)
            if (!line.isEmpty()) {
                String[] parts = line.split("\n", 2);
                revision = parseToRevision(parts[0]);
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    String[] otherData = parts[1].split("\n\n");
                    revision.setRevisionComment(otherData[0]);
                    revision.setModifiedFiles(getChangedFiles(otherData));
                }
                revisions.add(revision);
            }
        return revisions;
    }

    private List<ModifiedFile> getChangedFiles(String[] otherData) {

        Set<String> fileNames = new HashSet<>();
        List<ModifiedFile> files = new ArrayList<>();
        for (int i = 1; i < otherData.length; i++) {

            if (otherData[i].contains("@")) {
                String fileData = otherData[i].substring(0, otherData[i].indexOf("+")).trim();
                String[] parts = fileData.split("@");
                String filepath = parts[0].contains("~") ? parts[0].substring(0, parts[0].indexOf("~")) : parts[0];
                File file = new File(filepath);
                if (fileNames.contains(file.getName()))
                    continue;

                if (filepath.contains("deleted")) {
                    files.add(new ModifiedFile(file.getName(), "deleted"));
                } else {
                    files.add(new ModifiedFile(file.getName(), "1.1".equals(parts[1]) ? "added" : "updated"));
                }
            }
        }
        return files;
    }

    private Revision parseToRevision(String data) throws ParseException {
        String[] parts = data.split("\\s*,\\s*");
        return new Revision(parts[0], simpleDateFormat.parse(parts[1]), parts[2]);
    }

    public List<String> getChangeSet(List<String> outputLines) {
        String result = join(outputLines, "\n");
        List<String> changeSets = Arrays.asList(result.split(CHANGE_SET));
        return changeSets;
    }

    public Revision getLatestRevision() throws ParseException {
        return getRevisions().get(0);
    }

    public void resetHard(String revision) {
        setupWorkingDir();
        ProcessOutputStreamConsumer stdOut = new ProcessOutputStreamConsumer(new InMemoryConsumer());
        CommandLine bitKeeperResetHard = Console.createCommand("clone", "--checkout=get", "-r" + revision, bitKeeperConfig.getEffectiveUrl(), workingDir.getAbsolutePath());
        Console.runOrBomb(bitKeeperResetHard, workingDir, stdOut, new ProcessOutputStreamConsumer(new InMemoryConsumer()));
    }

    private String join(Collection c, String join) {
        StringBuffer sb = new StringBuffer();
        Iterator iter = c.iterator();

        while (iter.hasNext()) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(join);
            }
        }

        return sb.toString();
    }

}
