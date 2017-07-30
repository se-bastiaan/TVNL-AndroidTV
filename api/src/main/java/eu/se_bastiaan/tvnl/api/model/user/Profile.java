package eu.se_bastiaan.tvnl.api.model.user;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.Iterator;
import java.util.List;
import org.parceler.Parcel;


public class Profile
  extends BaseObservable
{
  @SerializedName("ageRestriction")
  protected Integer _ageRestriction;
  @SerializedName("id")
  protected String _id;
  @SerializedName("isChild")
  protected boolean _isChild;
  @SerializedName("isOwner")
  protected boolean _isOwner;
  @SerializedName("name")
  protected String _name;
  @SerializedName("notifications")
  protected List<ProfileNotification> _notifications;
  @SerializedName("watched")
  protected List<ProfileWatchingItem> _watchedItems;
  @SerializedName("watching")
  protected List<ProfileWatchingItem> _watchingItems;
  
  public Profile copy()
  {
    Gson localGson = new Gson();
    return (Profile)localGson.fromJson(localGson.toJson(this), Profile.class);
  }
  
  public void deleteNotification(String paramString)
  {
    if (this._notifications != null)
    {
      Iterator localIterator = this._notifications.iterator();
      while (localIterator.hasNext()) {
        if (TextUtils.equals(paramString, ((ProfileNotification)localIterator.next()).getMediaId())) {
          localIterator.remove();
        }
      }
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    Profile localProfile;
    do
    {
      return true;
      if ((paramObject == null) || (getClass() != paramObject.getClass())) {
        return false;
      }
      localProfile = (Profile)paramObject;
    } while ((this._id != null) && (this._id.equals(localProfile._id)));
    return false;
  }
  
  public Integer getAgeRestriction()
  {
    return this._ageRestriction;
  }
  
  public String getId()
  {
    return this._id;
  }
  
  public String getName()
  {
    return this._name;
  }
  
  public int getNotificationCount()
  {
    if (this._notifications != null) {
      return this._notifications.size();
    }
    return 0;
  }
  
  public List<ProfileNotification> getNotifications()
  {
    return this._notifications;
  }
  
  public int getProgressForWatchingItem(String paramString)
  {
    if ((this._watchingItems != null) && (paramString != null))
    {
      Iterator localIterator = this._watchingItems.iterator();
      while (localIterator.hasNext())
      {
        ProfileWatchingItem localProfileWatchingItem = (ProfileWatchingItem)localIterator.next();
        if (paramString.equals(localProfileWatchingItem.getMediaId())) {
          return localProfileWatchingItem.getProgressSec();
        }
      }
    }
    return 0;
  }
  
  public List<ProfileWatchingItem> getWatchedItems()
  {
    return this._watchedItems;
  }
  
  public int hashCode()
  {
    if (this._id != null) {
      return this._id.hashCode();
    }
    return 0;
  }
  
  public boolean isChild()
  {
    return this._isChild;
  }
  
  public boolean isNew(String paramString)
  {
    if (this._notifications != null)
    {
      Iterator localIterator = this._notifications.iterator();
      while (localIterator.hasNext()) {
        if (TextUtils.equals(((ProfileNotification)localIterator.next()).getMediaId(), paramString)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean isOwner()
  {
    return this._isOwner;
  }
  
  public boolean isWatched(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (this._watchedItems != null))
    {
      Iterator localIterator = this._watchedItems.iterator();
      while (localIterator.hasNext())
      {
        ProfileWatchingItem localProfileWatchingItem = (ProfileWatchingItem)localIterator.next();
        if ((localProfileWatchingItem != null) && (localProfileWatchingItem.getMediaId() != null) && (localProfileWatchingItem.getMediaId().equals(paramString))) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void setAgeRestriction(Integer paramInteger)
  {
    this._ageRestriction = paramInteger;
    notifyPropertyChanged(2);
  }
  
  public void setChild(boolean paramBoolean)
  {
    this._isChild = paramBoolean;
    notifyPropertyChanged(12);
  }
  
  public void setName(String paramString)
  {
    if (!TextUtils.isEmpty(paramString)) {}
    for (this._name = paramString.replaceAll("[^0-9a-zA-Z+ ]+", "");; this._name = "")
    {
      notifyPropertyChanged(31);
      return;
    }
  }
  
  public void setNotifications(List<ProfileNotification> paramList)
  {
    this._notifications = paramList;
  }
  
  public void updateWatchingProgressForItem(String paramString, int paramInt)
  {
    if ((!TextUtils.isEmpty(paramString)) && (this._watchingItems != null))
    {
      Iterator localIterator = this._watchingItems.iterator();
      while (localIterator.hasNext())
      {
        ProfileWatchingItem localProfileWatchingItem = (ProfileWatchingItem)localIterator.next();
        if (paramString.equals(localProfileWatchingItem.getMediaId())) {
          localProfileWatchingItem.setProgressSec(paramInt);
        }
      }
    }
    for (int i = 1;; i = 0)
    {
      if (i == 0) {
        this._watchingItems.add(new ProfileWatchingItem(paramString, paramInt));
      }
      return;
    }
  }
}


/* Location:              /Users/Sebastiaan/Development/Android/Reverse Engineering/apks/npostart-dex2jar.jar!/eu.se_bastiaan.tvnl.api/model/user/Profile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */