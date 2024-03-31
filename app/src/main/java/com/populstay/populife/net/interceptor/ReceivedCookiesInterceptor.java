package com.populstay.populife.net.interceptor;

import android.util.Log;

import com.populstay.populife.util.storage.PeachPreference;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
                //Log.v("OkHttp", "Received header: " + header);
            }

            PeachPreference.getAppPreference().edit()
                    .putStringSet("PREF_COOKIES", cookies)
                    .apply();
        }

        return originalResponse;
    }
}