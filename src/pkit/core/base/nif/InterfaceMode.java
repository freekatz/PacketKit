package pkit.core.base.nif;

public class InterfaceMode {
    public static enum RfmonMode {
        RfmonMode(1),
        NoRfmonMode(0);

        private final int value;

        private RfmonMode(int value) { this.value = value; }

        public int getValue() { return this.value; }
    }

    public static enum OfflineMode {
        OfflineMode(1),
        OnlineMode(0);

        private final int value;

        private OfflineMode(int value) { this.value = value; }

        public int getValue() { return this.value; }
    }
    public static enum ImmediateMode {
        ImmediateMode(1),
        DelayMode(0);

        private final int value;

        private ImmediateMode(int value) { this.value = value; }

        public int getValue() { return this.value; }
    }

}
