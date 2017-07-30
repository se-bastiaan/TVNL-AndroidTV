package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import org.threeten.bp.ZonedDateTime;

public class ProfileWatchListAdded
{
  @SerializedName("createdAt")
  protected ZonedDateTime _createdAt;
  @SerializedName("mediaId")
  protected String mediaId;
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/ProfileWatchListAdded.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */