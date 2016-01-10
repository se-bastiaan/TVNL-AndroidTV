package eu.se_bastiaan.tvnl.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {

    public static String decrypt(String key, String iv, String data) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv.substring(0, 16).getBytes("UTF-8")));
            return new String(cipher.doFinal(Base64.decode(data, Base64.NO_PADDING)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
