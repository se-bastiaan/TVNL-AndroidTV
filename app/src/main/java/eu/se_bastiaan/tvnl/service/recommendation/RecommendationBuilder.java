package eu.se_bastiaan.tvnl.service.recommendation;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import eu.se_bastiaan.tvnl.R;

public class RecommendationBuilder {

    private Context context;
    private NotificationManager notificationManager;

    private int id;
    private int priority;
    private int smallIcon;
    private String title;
    private String description;
    private String imageUri;
    private String backgroundUri;
    private PendingIntent intent;

    public RecommendationBuilder() {
    }

    public RecommendationBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public RecommendationBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public RecommendationBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public RecommendationBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public RecommendationBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public RecommendationBuilder setImage(String uri) {
        imageUri = uri;
        return this;
    }

    public RecommendationBuilder setBackgroundContentUri(String uri) {
        backgroundUri = uri;
        return this;
    }

    public RecommendationBuilder setIntent(PendingIntent intent) {
        this.intent = intent;
        return this;
    }

    public RecommendationBuilder setSmallIcon(int resourceId) {
        smallIcon = resourceId;
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Notification build() throws IOException {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle extras = new Bundle();
        if (backgroundUri != null) {
            extras.putString(Notification.EXTRA_BACKGROUND_IMAGE_URI, backgroundUri);
        }

        FutureTarget<Bitmap> futureTarget = Glide.with(context)
                .load(imageUri)
                .asBitmap()
                .into((int) context.getResources().getDimension(R.dimen.card_info_width), (int) context.getResources().getDimension(R.dimen.card_thumbnail_height));

        Bitmap image = null;
        try {
            image = futureTarget.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Glide.clear(futureTarget);

        Notification notification = new NotificationCompat.BigPictureStyle(
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setPriority(priority)
                        .setLocalOnly(true)
                        .setOngoing(true)
                        .setColor(context.getResources().getColor(R.color.primary))
                        .setCategory(Notification.CATEGORY_RECOMMENDATION)
                        .setLargeIcon(image)
                        .setSmallIcon(smallIcon)
                        .setContentIntent(intent)
                        .setExtras(extras))
                .build();

        notificationManager.notify(id, notification);
        notificationManager = null;
        return notification;
    }

    @Override
    public String toString() {
        return "RecommendationBuilder{" +
                ", id=" + id +
                ", priority=" + priority +
                ", smallIcon=" + smallIcon +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", backgroundUri='" + backgroundUri + '\'' +
                ", intent=" + intent +
                '}';
    }
}