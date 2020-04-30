package model;

public class NIFMode {

    public static enum ImmediateMode {
        ImmediateMode(1),
        DelayMode(0);

        private final int value;

        private ImmediateMode(int value) { this.value = value; }

        public int getValue() { return this.value; }
    }

}
