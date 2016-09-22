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

package cd.go.contrib.scms.bitkeeper.cmd;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;

public class Console {

    public static CommandLine createCommand(String... args) {
        CommandLine gitCmd = new CommandLine("bk");
        gitCmd.addArguments(args, false);
        return gitCmd;
    }

    public static ConsoleResult runOrBomb(CommandLine commandLine, File workingDir, ProcessOutputStreamConsumer stdOut, ProcessOutputStreamConsumer stdErr) {
        Executor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(stdOut, stdErr));
        if (workingDir != null) {
            executor.setWorkingDirectory(workingDir);
        }

        try {
            int exitCode = executor.execute(commandLine);

            if (exitCode != 0) {
                throw new RuntimeException(getMessage("Error", commandLine, workingDir));
            }

            return new ConsoleResult(exitCode, stdOut.output(), stdErr.output());
        } catch (Exception e) {
            throw new RuntimeException(getMessage("Exception", commandLine, workingDir), e);
        }
    }

    private static String getMessage(String type, CommandLine commandLine, File workingDir) {
        return String.format("%s Occurred: %s - %s", type, commandLine.toString(), workingDir);
    }
}
