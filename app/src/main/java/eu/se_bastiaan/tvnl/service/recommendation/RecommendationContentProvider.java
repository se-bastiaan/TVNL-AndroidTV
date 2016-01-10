package eu.se_bastiaan.tvnl.service.recommendation;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.TVNLApplication;

public class RecommendationContentProvider extends ContentProvider {

    public static String AUTHORITY = "eu.se_basitaan.tvnl.RecommendationContentProvider";
    public static String CONTENT_URI = "content://" + AUTHORITY + "/";

    @Inject
    OkHttpClient okHttpClient;

    @Override
    public boolean onCreate() {
        TVNLApplication.get().appComponent().inject(this);
        return true;
    }

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode)
            throws FileNotFoundException {
        ParcelFileDescriptor[] pipe = null;

        String url = uri.getPath();

        try {
            String decodedUrl = URLDecoder.decode(url.replaceFirst("/", ""),
                    "UTF-8");
            pipe = ParcelFileDescriptor.createPipe();

            OkUrlFactory factory = new OkUrlFactory(okHttpClient);
            HttpURLConnection connection = factory.open(new URL(decodedUrl));

            new TransferThread(connection.getInputStream(),
                    new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]))
                    .start();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception opening pipe", e);
            throw new FileNotFoundException("Could not open pipe for: "
                    + uri.toString());
        }

        return (pipe[0]);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return "image/*";
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        return 0;
    }

    static class TransferThread extends Thread {
        InputStream in;
        OutputStream out;

        TransferThread(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            byte[] buf = new byte[8192];
            int len;

            try {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(),
                        "Exception transferring file", e);
            }
        }
    }

}