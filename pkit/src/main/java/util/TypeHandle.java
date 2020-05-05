package util;

public class TypeHandle {

    public static byte HexToByte(String hex) {
        hex = hex.toLowerCase();
        byte highDit = (byte) (Character.digit(hex.charAt(0), 16) & 0xFF);
        byte lowDit = (byte) (Character.digit(hex.charAt(1), 16) & 0xFF);
        return (byte) (highDit << 4 | lowDit);
    }

    public static String ByteToHex(byte b) {
        int v = b & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) return "0"+hv.toLowerCase();
        else return hv.toLowerCase();
    }

    public static String IntToHex(int i) {
        String hv = Integer.toHexString(i);
        if (hv.length()%2==1) hv = "0" + hv.toLowerCase();
        else hv = hv.toLowerCase();

        return "0".repeat(Math.max(0, 4 - hv.length())) +hv;
    }

    public static byte[] HexToBytes(String[] hex) {
        if (hex.length==0) return null;
        byte[] bytes = new byte[hex.length];

        int index = 0;
        for (String h : hex) {
            bytes[index] = TypeHandle.HexToByte(h);
            index += 1;
        }


        return bytes;
    }


    public static String[] BytesToHex(byte[] bytes) {
        if (bytes.length == 0)
            return null;
        String[] hex = new String[bytes.length];
        int index = 0;
        for (byte b : bytes) {
            hex[index] = TypeHandle.ByteToHex(b);

            index += 1;
        }
        return hex;
    }

    public static byte[] TxtToBytes(char[] txt) {
        if (txt.length == 0)
            return null;
        byte[] bytes = new byte[txt.length];
        int index = 0;
        for (char c : txt) {
            bytes[index] = (byte)c;

            index += 1;
        }
        return bytes;
    }

    public static char[] BytesToTxt(byte[] bytes) {
        if (bytes.length == 0)
            return null;
        char[] txt = new char[bytes.length];
        int index = 0;
        for (byte b : bytes) {
            txt[index] = (char)b;

            index += 1;
        }
        return txt;
    }
}
