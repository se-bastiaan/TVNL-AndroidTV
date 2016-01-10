package eu.se_bastiaan.tvnl.model;

import eu.se_bastiaan.tvnl.content.KeyProvider;
import eu.se_bastiaan.tvnl.util.SecurityUtil;

public class EncryptedStreamData {

    private String data;
    private String iv;
    private Boolean tt888;

    public String getData() {
        return data;
    }

    public String getIv() {
        return iv;
    }

    public String getUrl() {
        return SecurityUtil.decrypt(KeyProvider.data(), getIv(), getData());
    }

    public Boolean getTt888() {
        return tt888;
    }

}
