package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import eu.se_bastiaan.tvnl.api.model.page.component.Link;
import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset;

public class ProfileWatchFavoritesList
{
  @SerializedName("count")
  protected int _count;
  @SerializedName("items")
  protected List<AbstractAsset> _items;
  @SerializedName("_links")
  protected Map<String, Link> _links;
  @SerializedName("total")
  protected int _total;
  
  public int getCount()
  {
    return this._count;
  }
  
  public List<AbstractAsset> getItems()
  {
    return this._items;
  }
  
  public Map<String, Link> getLinks()
  {
    return this._links;
  }
  
  public int getTotal()
  {
    return this._total;
  }
  
  public void setCount(int paramInt)
  {
    this._count = paramInt;
  }
  
  public void setItems(List<AbstractAsset> paramList)
  {
    this._items = paramList;
  }
  
  public void setLinks(Map<String, Link> paramMap)
  {
    this._links = paramMap;
  }
  
  public void setTotal(int paramInt)
  {
    this._total = paramInt;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/ProfileWatchFavoritesList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */