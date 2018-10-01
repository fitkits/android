package com.fitkits.Services;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fitkits.Model.User;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.RetroClient;
import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Akshay on 02-11-2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences myPrefs;
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Freshchat.getInstance(this).setPushRegistrationToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sendRegistrationToServer(token);

    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        ApiService apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());
        User user=new User(token);
        apiService.updateProfile("/api/v1/cms/users/"+myPrefs.getString("user_id",""),user).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<com.fitkits.Model.User>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(com.fitkits.Model.User value) {



                }

                @Override
                public void onError(Throwable e) {

                    Log.d("Response",e.getMessage());

                }

                @Override
                public void onComplete() {

                }
            });
    }
}
