package com.populstay.populife.net.interceptor;

import android.util.Log;

import com.populstay.populife.util.storage.PeachPreference;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) PeachPreference.getAppPreference().getStringSet("PREF_COOKIES", new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            //Log.v("OkHttp", "Adding Header: " + cookie);
        }

        return chain.proceed(builder.build());
    }
}
