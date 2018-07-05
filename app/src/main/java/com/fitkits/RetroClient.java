package com.fitkits;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.*;
import okhttp3.Interceptor.Chain;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshay on 20/12/17.
 */

public class RetroClient {

  private static final String ROOT_URL = "https://139.59.80.139";
 static String authToken="";

  private static Retrofit getRetrofitInstance(String username,String password, Context context) {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    clientBuilder.addInterceptor(loggingInterceptor);
    if (!TextUtils.isEmpty(username)
        && !TextUtils.isEmpty(password)) {
       authToken = Credentials.basic(username, password);
    }
    if (!TextUtils.isEmpty(authToken)) {
      Interceptor headerAuthorizationInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
          okhttp3.Request request = chain.request();
          Headers headers = request.headers().newBuilder().add("Authorization", authToken).build();
          request = request.newBuilder().headers(headers).build();
          return chain.proceed(request);
        }
      };

      if (!clientBuilder.interceptors().contains(headerAuthorizationInterceptor)) {
        clientBuilder.addInterceptor(headerAuthorizationInterceptor);
      }
    }
    try {
      clientBuilder.sslSocketFactory(getSSLConfig(context).getSocketFactory());
    } catch (Exception e) {
      e.printStackTrace();
    }
    clientBuilder.hostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });

    return new Retrofit
        .Builder()
        .client(clientBuilder.build())
        .baseUrl(ROOT_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }


  public static ApiService getApiService(String username,String password, Context context) {
    return getRetrofitInstance(username,password, context).create(ApiService.class);
  }

  private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
      KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

    // Loading CAs from an InputStream
    CertificateFactory cf = null;
    cf = CertificateFactory.getInstance("X.509");

    Certificate ca;
    // I'm using Java7. If you used Java6 close it manually with finally.
    try (InputStream cert = context.getResources().openRawResource(R.raw.nginxselfsigned)) {
      ca = cf.generateCertificate(cert);
    }

    // Creating a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Creating a TrustManager that trusts the CAs in our KeyStore.
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Creating an SSLSocketFactory that uses our TrustManager
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

    return sslContext;
  }
}
