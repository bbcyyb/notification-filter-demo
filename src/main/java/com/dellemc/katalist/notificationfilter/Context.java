package com.dellemc.katalist.notificationfilter;

import com.dellemc.katalist.notificationfilter.job.JobStatus;
import com.dellemc.katalist.notificationfilter.job.JobStatusEnum;

public class Context {
    private JobStatus jobStatus;
    private FilterStatus filterStatus;

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public FilterStatus getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(FilterStatus filterStatus) {
        this.filterStatus = filterStatus;
    }

    public Context(long lastCommittedOffset, JobStatusEnum jobStatusEnum, int partition) {
        this.jobStatus = new JobStatus(lastCommittedOffset, jobStatusEnum, partition);
        this.filterStatus = new FilterStatus();
    }

    public class FilterStatus {
        private boolean passed = false;

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }
    }
}

