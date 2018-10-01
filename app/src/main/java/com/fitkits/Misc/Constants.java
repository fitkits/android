package com.fitkits.Misc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fitkits.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by akshay on 31/07/17.
 */

public class Constants {

    public static String base_url = "http://142.93.208.162";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void internetToast(Context context) {
        Toast.makeText(context, R.string.TOAST_NO_INETERNET, Toast.LENGTH_SHORT).show();
    }

    public static void internetToastButLogged(Context context) {
        Toast.makeText(context, "No internet available. Your data might be logged tonight.", Toast.LENGTH_SHORT).show();
    }

    public static class PicassoTrustAll {

        private static Picasso mInstance = null;

        private PicassoTrustAll(Context context) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                client.sslSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mInstance = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(client.build()))
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        }
                    }).build();

        }

        public static Picasso getInstance(Context context) {
            if (mInstance == null) {
                new PicassoTrustAll(context);
            }
            return mInstance;
        }
    }
}
