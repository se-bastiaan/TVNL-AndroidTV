package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;


public class OauthSession
{
  @SerializedName("access_token")
  protected String _accessToken = "";
  @SerializedName("expires_in")
  protected int _expiresIn;
  @SerializedName("refresh_token")
  protected String _refreshToken = "";
  @SerializedName("scope")
  protected String _scope = "";
  @SerializedName("token_type")
  protected String _tokenType = "";
  
  public OauthSession() {}
  
  public OauthSession(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
  {
    this._scope = paramString1;
    this._expiresIn = paramInt;
    this._tokenType = paramString2;
    this._refreshToken = paramString3;
    this._accessToken = paramString4;
  }
  
  public String getAccessToken()
  {
    return this._accessToken;
  }
  
  public int getExpiresIn()
  {
    return this._expiresIn;
  }
  
  public String getRefreshToken()
  {
    return this._refreshToken;
  }
  
  public String getScope()
  {
    return this._scope;
  }
  
  public String getTokenType()
  {
    return this._tokenType;
  }
  
  public void setAccessToken(String paramString)
  {
    this._accessToken = paramString;
  }
  
  public void setExpiresIn(int paramInt)
  {
    this._expiresIn = paramInt;
  }
  
  public void setRefreshToken(String paramString)
  {
    this._refreshToken = paramString;
  }
  
  public void setScope(String paramString)
  {
    this._scope = paramString;
  }
  
  public void setTokenType(String paramString)
  {
    this._tokenType = paramString;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/OauthSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */