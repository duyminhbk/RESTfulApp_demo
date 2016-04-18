package com.app.restfulapp.ultis;

import com.app.restfulapp.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by minhpham on 3/13/16.
 */
public class AppClientRequest {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(MainActivity mActivity,String url,  AsyncHttpResponseHandler responseHandler) {
        initClient(mActivity);
        client.get(url, responseHandler);
    }

    public static void post(MainActivity mActivity,String url, AsyncHttpResponseHandler responseHandler) {
        initClient(mActivity);
        client.post(url, responseHandler);
    }

    private static void initClient(MainActivity mActivity) {
        // client.setProxy("10.0.0.107", 8888);// define your proxy here
        client.setCookieStore(mActivity.getCookieStore());
        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
    }
}
