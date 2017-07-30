package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;


public class ProfileWatchingItem
{
  @SerializedName("mediaId")
  protected String _mediaId;
  @SerializedName("progress")
  protected int _progress;
  
  public ProfileWatchingItem() {}
  
  public ProfileWatchingItem(String mediaId, int progress)
  {
    this._mediaId = mediaId;
    this._progress = progress;
  }
  
  public String getMediaId()
  {
    return this._mediaId;
  }
  
  public int getProgressSec()
  {
    return this._progress;
  }
  
  public void setProgressSec(int paramInt)
  {
    this._progress = paramInt;
  }
}