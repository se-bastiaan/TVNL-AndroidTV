package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.parceler.Parcel;


public class Terms
{
  @SerializedName("code")
  protected String _code;
  @SerializedName("currentTermsVersion")
  protected String _currentTermsVersion;
  @SerializedName("latestTermsVersion")
  protected String _latestTermsVersion;
  @SerializedName("message")
  protected String _message;
  @SerializedName("terms")
  protected List<Term> _terms;
  
  public String getCode()
  {
    return this._code;
  }
  
  public String getCurrentTermsVersion()
  {
    return this._currentTermsVersion;
  }
  
  public String getLatestTermsVersion()
  {
    return this._latestTermsVersion;
  }
  
  public String getMessage()
  {
    return this._message;
  }
  
  public List<Term> getTerms()
  {
    return this._terms;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Terms.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */