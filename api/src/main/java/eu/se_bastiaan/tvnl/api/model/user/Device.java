package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;


public class Device
{
  @SerializedName("id")
  protected String _id;
  @SerializedName("name")
  protected String _name;
  @SerializedName("uuid")
  protected String _uuid;
  
  public String getId()
  {
    return this._id;
  }
  
  public String getName()
  {
    return this._name;
  }
  
  public String getUuid()
  {
    return this._uuid;
  }
  
  public void setId(String paramString)
  {
    this._id = paramString;
  }
  
  public void setName(String paramString)
  {
    this._name = paramString;
  }
  
  public void setUuid(String paramString)
  {
    this._uuid = paramString;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Device.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */