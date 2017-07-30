package eu.se_bastiaan.tvnl.api.model.user;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import eu.se_bastiaan.tvnl.api.model.page.component.Link;
import org.parceler.Parcel;
import org.threeten.bp.ZonedDateTime;


public class Term
{
  @SerializedName("lead")
  protected String _lead;
  @SerializedName("_links")
  protected Map<String, Link> _links;
  @SerializedName("slug")
  protected String _slug;
  @SerializedName("text")
  protected String _text;
  @SerializedName("title")
  protected String _title;
  @SerializedName("updatedAt")
  protected ZonedDateTime _updatedAt;
  @SerializedName("version")
  protected String _version;
  
  public String getLead()
  {
    return this._lead;
  }
  
  public Map<String, Link> getLinks()
  {
    return this._links;
  }
  
  public String getSlug()
  {
    return this._slug;
  }
  
  public String getText()
  {
    return this._text;
  }
  
  public String getTitle()
  {
    return this._title;
  }
  
  public ZonedDateTime getUpdatedAt()
  {
    return this._updatedAt;
  }
  
  public String getVersion()
  {
    return this._version;
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Term.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */