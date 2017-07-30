package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;

public class ProgressBody
{
  @SerializedName("progress")
  protected String _progress;
  
  public ProgressBody(String paramString)
  {
    this._progress = paramString;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/ProgressBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */