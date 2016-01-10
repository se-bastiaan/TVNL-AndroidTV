package eu.se_bastiaan.tvnl.content;

import java.io.UnsupportedEncodingException;

public class KeyProvider {

    private static final byte[] data = {-19, -97, -77, -13, -91, -100, -69, -18, -117, -110, -18, -103, -96, -18, -90, -106, -18, -74, -89, -17, -124, -113, -17, -110, -67, -17, -95, -119, -17, -80, -87, -17, -66, -89, -51, -112, -35, -93, -32, -85, -122, -32, -71, -71, -31, -122, -114, -31, -105, -82, -31, -92, -105, -31, -78, -78, -30, -127, -117, -30, -112, -92, -30, -97, -106, -30, -83, -85, -30, -69, -118, -29, -118, -85, -29, -104, -121, -29, -89, -106, -29, -73, -89, -28, -124, -112, -28, -110, -92, -28, -95, -102};

    static String decode(byte[] paramArrayOfByte) {
        try {
            char[] arrayOfChar = new String(paramArrayOfByte, "UTF-8").toCharArray();
            int i = 8574923;
            for (int j = 0; j < arrayOfChar.length; j++) {
                arrayOfChar[j] = ((char) (i ^ arrayOfChar[j]));
                i += 930;
            }
            return String.valueOf(arrayOfChar);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return null;
    }

    public static String data() {
        return decode(data);
    }

}
