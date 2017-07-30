package eu.se_bastiaan.tvnl.api.model.user.subscriptions;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.parceler.Parcel;


public class Plan
{
  @SerializedName("features")
  protected List<Feature> _features;
  @SerializedName("id")
  protected String _id;
  @SerializedName("price")
  protected Integer _price;
  @SerializedName("subtitle")
  protected String _subtitle;
  @SerializedName("title")
  protected String _title;
  
  public List<Feature> getFeatures()
  {
    return this._features;
  }
  
  public List<String> getFeaturesAsStrings()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this._features.iterator();
    while (localIterator.hasNext()) {
      localArrayList.add(((Feature)localIterator.next())._title);
    }
    return localArrayList;
  }
  
  public String getId()
  {
    return this._id;
  }
  
  public Integer getPrice()
  {
    return this._price;
  }
  
  public String getSubtitle()
  {
    return this._subtitle;
  }
  
  public String getTitle()
  {
    return this._title;
  }
  

  public static class Feature
  {
    @SerializedName("title")
    protected String _title;
    
    public String getTitle()
    {
      return this._title;
    }
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/subscriptions/Plan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */