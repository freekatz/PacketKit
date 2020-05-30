package gui.model;

public enum ViewType {
    IndexView(0),
    CaptureView(1),
    SendView(2),
    SettingView(3),
    AboutView(4),
    ConfigView(5),
    AnalysisView(6)
    ;

    private final int value;

    ViewType(int value) { this.value = value; }

    int getValue() { return this.value; }
}
