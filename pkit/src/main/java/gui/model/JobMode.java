package gui.model;

public enum JobMode {
    OfflineJob(0),
    OnlineJob(1),
    AnalysisJob(2),
    ApplyJob(3),
    SaveJob(4),
    ImportJob(5),
    ExportJob(6),
    SendOneJob(7),
    SendAllJob(8),
    ForwardOneJob(9),
    ForwardAllJob(10)
    ;

    private final int value;

    JobMode(int value) {
        this.value = value;
    }

    int getValue() { return this.value; }
}
