package eu.se_bastiaan.tvnl.api.model.user;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import java.util.HashMap;
import java.util.Map;

import eu.se_bastiaan.tvnl.api.model.page.component.Link;


public class Account
  extends BaseObservable
{
  @SerializedName("createdAt")
  protected ZonedDateTime _createdAt;
  @SerializedName("birthday")
  protected String _dateOfBirth;
  @SerializedName("email")
  protected String _email;
  @SerializedName("externalId")
  protected String _externalId;
  @SerializedName("firstName")
  protected String _firstName;
  @SerializedName("gender")
  @Gender
  protected String _gender;
  @SerializedName("id")
  protected String _id;
  @SerializedName("isReceivingNewsletter")
  protected boolean _isReceivingNewsletter;
  @SerializedName("lastName")
  protected String _lastName;
  @SerializedName("_links")
  protected Map<String, Link> _links;
  @SerializedName("pinCode")
  protected String _pinCode;
  @SerializedName("preferredSubscription")
  protected String _preferredSubscription;
  @SerializedName("subscription")
  protected Subscription _subscription;
  protected ZonedDateTime _termsAcceptedAt;
  protected String _termsVersion;
  
  public Account() {}
  
  public Account(Account paramAccount)
  {
    this._id = paramAccount._id;
    this._externalId = paramAccount._externalId;
    this._firstName = paramAccount._firstName;
    this._lastName = paramAccount._lastName;
    this._dateOfBirth = paramAccount._dateOfBirth;
    this._gender = paramAccount._gender;
    this._email = paramAccount._email;
    this._termsVersion = paramAccount._termsVersion;
    this._termsAcceptedAt = paramAccount._termsAcceptedAt;
    this._isReceivingNewsletter = paramAccount._isReceivingNewsletter;
    this._pinCode = paramAccount._pinCode;
    this._createdAt = paramAccount._createdAt;
    this._preferredSubscription = paramAccount._preferredSubscription;
    this._subscription = paramAccount._subscription;
    if (paramAccount._links != null) {
      this._links = new HashMap(paramAccount._links);
    }
  }
  
  public ZonedDateTime getCreatedAt()
  {
    return this._createdAt;
  }
  
  public String getDateOfBirth()
  {
    return this._dateOfBirth;
  }
  
  public String getEmail()
  {
    return this._email;
  }
  
  public String getExternalId()
  {
    return this._externalId;
  }
  
  public String getFirstName()
  {
    return this._firstName;
  }
  
  public String getGender()
  {
    return this._gender;
  }
  
  public String getId()
  {
    return this._id;
  }
  
  public String getLastName()
  {
    return this._lastName;
  }
  
  public Map<String, Link> getLinks()
  {
    return this._links;
  }
  
  public String getPinCode()
  {
    return this._pinCode;
  }
  
  public String getPreferredSubscription()
  {
    return this._preferredSubscription;
  }
  
  public Subscription getSubscription()
  {
    return this._subscription;
  }
  
  public ZonedDateTime getTermsAcceptedAt()
  {
    return this._termsAcceptedAt;
  }
  
  public String getTermsVersion()
  {
    return this._termsVersion;
  }
  
  public boolean isReceivingNewsletter()
  {
    return this._isReceivingNewsletter;
  }
  
  public void setDateOfBirth(String paramString)
  {
    this._dateOfBirth = paramString;
    notifyPropertyChanged(16);
  }
  
  public void setEmail(String paramString)
  {
    this._email = paramString;
    notifyPropertyChanged(20);
  }
  
  public void setExternalId(String paramString)
  {
    this._externalId = paramString;
  }
  
  public void setFirstName(String paramString)
  {
    this._firstName = paramString;
    notifyPropertyChanged(21);
  }
  
  public void setGender(String paramString)
  {
    this._gender = paramString;
  }
  
  public void setId(String paramString)
  {
    this._id = paramString;
    notifyPropertyChanged(23);
  }
  
  public void setLastName(String paramString)
  {
    this._lastName = paramString;
    notifyPropertyChanged(26);
  }
  
  public void setPinCode(String paramString)
  {
    this._pinCode = paramString;
    notifyPropertyChanged(35);
  }
  
  public void setPreferredSubscription(String paramString)
  {
    this._preferredSubscription = paramString;
    notifyPropertyChanged(36);
  }
  
  public void setReceivingNewsletter(boolean paramBoolean)
  {
    this._isReceivingNewsletter = paramBoolean;
    notifyPropertyChanged(39);
  }
  
  public void setTermsAcceptedAt(ZonedDateTime paramZonedDateTime)
  {
    this._termsAcceptedAt = paramZonedDateTime;
    notifyPropertyChanged(42);
  }
  
  public void setTermsVersion(String paramString)
  {
    this._termsVersion = paramString;
    notifyPropertyChanged(43);
  }
  
  public String toString()
  {
    return "Account{_id='" + this._id + '\'' + ", _externalId='" + this._externalId + '\'' + ", _name='" + this._firstName + '\'' + ", _email='" + this._email + '\'' + ", _termsVersion='" + this._termsVersion + '\'' + ", _termsAcceptedAt=" + this._termsAcceptedAt + ", _isReceivingNewsletter=" + this._isReceivingNewsletter + ", _createdAt=" + this._createdAt + ", _links=" + this._links + ", _subscription='" + this._subscription + '\'' + ", _preferredSubscription='" + this._preferredSubscription + "'}";
  }
  
  public static @interface Gender
  {
    public static final String FEMALE = "female";
    public static final String MALE = "male";
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Account.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */