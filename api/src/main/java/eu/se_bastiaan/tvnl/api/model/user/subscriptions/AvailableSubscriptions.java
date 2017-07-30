package eu.se_bastiaan.tvnl.api.model.user.subscriptions;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.parceler.Parcel;


public class AvailableSubscriptions
{
  @SerializedName("plans")
  protected List<Plan> _plans;
  
  public List<Plan> getPlans()
  {
    return this._plans;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/subscriptions/AvailableSubscriptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */