package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.parceler.Parcel;


public class Credentials
{
  @SerializedName("grant_type")
  protected String _grantType;
  @SerializedName("password")
  protected String _password;
  @SerializedName("refresh_token")
  protected String _refreshToken;
  @SerializedName("username")
  protected String _username;
  
  protected Credentials() {}
  
  public Credentials(String paramString)
  {
    this._refreshToken = paramString;
    this._grantType = "refresh_token";
  }
  
  public Credentials(String paramString1, String paramString2)
  {
    this._username = paramString1;
    this._password = paramString2;
    this._grantType = "password";
  }
  
  public String getPassword()
  {
    return this._password;
  }
  
  public String getUsername()
  {
    return this._username;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GrantType
  {
    public static final String PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Credentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */