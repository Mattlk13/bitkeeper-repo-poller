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

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryConsumer implements StreamConsumer {
    private Queue<String> lines = new ConcurrentLinkedQueue<String>();

    public void consumeLine(String line) {
        try {
            lines.add(line);
        } catch (RuntimeException e) {
            // LOG.error("Problem consuming line [" + line + "]", e);
        }
    }

    public List<String> asList() {
        return new ArrayList<String>(lines);
    }

    public String toString() {
        return join(asList(), "\n");
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
