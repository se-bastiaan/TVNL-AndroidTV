package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;
import org.threeten.bp.ZonedDateTime;


public class ProfileNotification
{
  @SerializedName("createdAt")
  protected ZonedDateTime _createdAt;
  @SerializedName("mediaId")
  protected String _mediaId;
  @SerializedName("showNewLabel")
  protected boolean _showNewLabel;
  
  public ZonedDateTime getCreatedAt()
  {
    return this._createdAt;
  }
  
  public String getMediaId()
  {
    return this._mediaId;
  }
  
  public boolean isShowNewLabel()
  {
    return this._showNewLabel;
  }
  
  public void setCreatedAt(ZonedDateTime paramZonedDateTime)
  {
    this._createdAt = paramZonedDateTime;
  }
  
  public void setMediaId(String paramString)
  {
    this._mediaId = paramString;
  }
  
  public void setShowNewLabel(boolean paramBoolean)
  {
    this._showNewLabel = paramBoolean;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/ProfileNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */