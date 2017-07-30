package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;

public class MediaIdRequestBody
{
  @SerializedName("mediaId")
  protected String _mediaId;
  
  public MediaIdRequestBody(String paramString)
  {
    this._mediaId = paramString;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/MediaIdRequestBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */