package eu.se_bastiaan.tvnl.api.model.user;

import android.content.Context;
import com.google.gson.annotations.SerializedName;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.parceler.Parcel;


public class Subscription
{
  @SerializedName("autoRenewal")
  protected boolean _autoRenewal;
  @SerializedName("id")
  protected String _id;
  @SerializedName("plan")
  protected String _plan;
  
  public String getHumanPlan(Context paramContext)
  {
    String str = this._plan;
    int i = -1;
    switch (str.hashCode())
    {
    }
    for (;;)
    {
      switch (i)
      {
      default: 
        return paramContext.getString(2131231152);
        if (str.equals("nlziet"))
        {
          i = 0;
          continue;
          if (str.equals("npoplus"))
          {
            i = 1;
            continue;
            if (str.equals("free")) {
              i = 3;
            }
          }
        }
        break;
      }
    }
    return paramContext.getString(2131231153);
    return paramContext.getString(2131231157);
  }
  
  public String getId()
  {
    return this._id;
  }
  
  public String getPlan()
  {
    return this._plan;
  }
  
  public boolean isAutoRenewal()
  {
    return this._autoRenewal;
  }
  
  public void setAutoRenewal(boolean paramBoolean)
  {
    this._autoRenewal = paramBoolean;
  }
  
  public void setId(String paramString)
  {
    this._id = paramString;
  }
  
  public void setPlan(String paramString)
  {
    this._plan = paramString;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Plan
  {
    public static final String FREE = "free";
    public static final String NL_ZIET = "nlziet";
    public static final String PREMIUM = "npoplus";
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */