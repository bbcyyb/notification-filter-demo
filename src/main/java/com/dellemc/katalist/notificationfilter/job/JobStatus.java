package com.dellemc.katalist.notificationfilter.job;

public class JobStatus {

    private long lastCommittedOffset;
    private JobStatusEnum jobStatus;
    private final int partition;

    public JobStatus(long lastCommittedOffset,
                            JobStatusEnum jobStatus, int partition) {
        this.lastCommittedOffset = lastCommittedOffset;
        this.jobStatus = jobStatus;
        this.partition = partition;
    }

    public long getLastCommittedOffset() {
        return lastCommittedOffset;
    }

    public void setLastCommittedOffset(long lastCommittedOffset) {
        this.lastCommittedOffset = lastCommittedOffset;
    }

    public JobStatusEnum getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getPartition() {
        return partition;
    }

    protected void doToString(StringBuilder sb) {

    }

    @Override
    public String toString() {
        StringBuilder sb  = new StringBuilder();
        sb.append("[JobStatus: {");
        sb.append("partition=" + partition);
        sb.append("lastCommittedOffset=" + lastCommittedOffset);
        sb.append("jobStatus=" + jobStatus.name());
        sb.append("}");
        doToString(sb);
        sb.append("]");
        return sb.toString();
    }
}
