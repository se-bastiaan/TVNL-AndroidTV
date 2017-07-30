package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProfilesResponse
{
  @SerializedName("profiles")
  protected List<Profile> _profiles;
  
  public List<Profile> getProfiles()
  {
    return this._profiles;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/ProfilesResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */