/**
 * Copyright 2018 BlazeMeter Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hudson.plugins.blazemeter.utils.report;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.plugins.blazemeter.PerformanceBuildAction;
import hudson.remoting.VirtualChannel;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportUrlTask extends TimerTask {

    protected Logger logger = Logger.getLogger(ReportUrlTask.class.getName());

    protected String jobName;

    private VirtualChannel channel;
    private Run run;

    protected boolean isDone;
    protected final long reportLinkId;

    public ReportUrlTask(Run run, String jobName, VirtualChannel channel, long reportLinkId) {
        this.run = run;
        this.jobName = jobName;
        this.channel = channel;
        this.reportLinkId = reportLinkId;
    }

    @Override
    public void run() {
        try {
            if (isDone) {
                return;
            }
            EnvVars ev = EnvVars.getRemote(channel);
            String runId = jobName + "-" + run.getId() + "-" + reportLinkId;
            if (ev != null && ev.containsKey(runId)) {
                String masterId = ev.get(runId, "");
                PerformanceBuildAction a = new PerformanceBuildAction(run, masterId);
                a.setReportUrl(ev.get(runId + "-" + masterId, ""));
                a.setLinkName(ev.get(runId + "-link-name", "BlazeMeter Report"));
                run.addAction(a);
                isDone = true;
                super.cancel();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to get report URL", e);
        }
    }

    public boolean isDone() {
        return isDone;
    }
}
