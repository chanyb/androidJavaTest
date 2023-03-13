package com.example.smartbox_dup.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;



public class RequestManager {
    private static final String TAG = "this";
    private static RequestManager instance = new RequestManager();

    private RequestManager() {
        headers = new HashMap<>();
        requestQueue = Volley.newRequestQueue(GlobalApplication.getContext());
    }

    private static RequestQueue requestQueue;

    public static RequestManager getInstance() {
        if (instance == null) instance = new RequestManager();
        return instance;
    }

    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    public static int PUT = Request.Method.PUT;
    public static int DELETE = Request.Method.DELETE;

    public enum Return {
        JSON,
        STRING
    }

    private Map<String, String> headers;
    private Listener listener;

    public void setHeaders(Map<String, String> _headers) {
        headers = _headers;
    }


    /**
     * @param method      GET | POST | PUT | DELETE
     * @param RETURN_TYPE JSON | STRING
     * @param url         Destination URL
     * @param object      params
     * @param listener    Success and Error Listener interface implementation
     */
    public void addRequest(int method, Return RETURN_TYPE, String url, JSONObject object, Listener listener) {
        this.listener = listener;

        Request request = null;

        switch (RETURN_TYPE) {
            case JSON:
                request = new JsonObjectRequest(method, url, object, successListener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                };
                break;
            case STRING:
                if(object==null) {
                    request = new StringRequest(method, url, successListener, errorListener);
                    break;
                }
                for(int i = 0; i<object.names().length(); i++){
                    String key = null;
                    Object value = null;
                    try {
                        key = object.names().getString(i);
                        value = URLEncoder.encode(String.valueOf(object.get(object.names().getString(i))), "utf-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (i==0) url = url + "?" + key + "=" + value;
                    else url = url + "&" + key + "=" + value;
                }
                Log.i("this", "URL -> " + url);

                request = new StringRequest(method, url, successListener, errorListener);
                break;
        }

        request.setRetryPolicy(new DefaultRetryPolicy(2000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    /**
     * @param url       Destination URL
     * @param params    parameter key value pairs
     * @param listener  Success and Error Listener interface implementation
     */
    public void addUrlencodedRequest(String url, Map<String, String> params, Listener listener) {
        this.listener = listener;

        Request request = new StringRequest(Request.Method.POST, url, successListener, errorListener) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    /**
     * @param context       Context
     * @param url           Destination URL
     * @param params        body params
     * @param fileParams    file params
     * @param listener      Success and Error Listener interface implementation
     */
    public void addMultipartRequest(Context context, String url, Map<String, Object> params, Map<String, File> fileParams, Listener listener) {
        this.listener = listener;
        Request request = new MultipartRequest(context, Request.Method.POST, url, params, fileParams, successListener, errorListener);
        requestQueue.add(request);
    }

    public interface Listener {
        void onSuccess(Object response);

        void onError(String errorString);
    }

    Response.Listener successListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            if (listener != null) {
                listener.onSuccess(response);
                listener = null;
            }

        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (listener != null) {
                Log.e("this", "RequestManager", error);
                listener.onError(error.toString());
                listener = null;
            }
        }
    };


}
