package com.ansaridroid.csacademy.util.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ansaridroid.csacademy.util.authentication.Authenticate;

import com.ansaridroid.csacademy.util.authentication.OnFragmentInteractionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HTTPRequest {
    private Context context;
    private Authenticate authenticate;
    private RequestQueue requestQueue;

    private OnFragmentInteractionListener mListener;

    public HTTPRequest(Context context) {
        this.context = context;
        authenticate = new Authenticate(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }else {
            throw new Error(context + "Must implement OnFragmentInteractionListener");
        }

        //requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {

        if (requestQueue != null) {
            return requestQueue;
        }
        return requestQueue= Volley.newRequestQueue(context);
    }

    public void addToRequestQueue(Request request, String tag) {

        request.setTag(tag);
        getRequestQueue().add(request);
    }

    //Cancel all request with the given tag
    public void cancelRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    public void makeDashboardUIResourcesRequest() {
        mListener.showProgressBar();
        authenticate.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String url = "https://us-central1-csacademy-df2c9.cloudfunctions.net/server/courses";
                            String token = task.getResult().getToken();
                            JSONArray Params = new JSONArray();
                            JSONObject postParams = new JSONObject();
                            try {
                                postParams.put("x-auth", token);
                                Params.put(0, postParams);
                            } catch (JSONException e) {
                                Toast.makeText(context, "JSON Exception: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, Params,
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            mListener.hideProgressBar();
                                            mListener.updateDashboardUI(response.toString());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            mListener.hideProgressBar();
                                            Toast.makeText(context, "Courses: " + error.getStackTrace(), Toast.LENGTH_SHORT).show();
                                            mListener.updateDashboardUI(error.getCause().toString());
                                        }
                                    });
                            addToRequestQueue(jsonArrayRequest, "POST/courses");
                        }else {
                            mListener.hideProgressBar();
                            Toast.makeText(context, "Token not found error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void makeCourseDescriptionRequest(final String courseId) {
        mListener.showProgressBar();
        String url = "https://us-central1-csacademy-df2c9.cloudfunctions.net/server/courses/"+courseId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mListener.hideProgressBar();
                        mListener.updateCourseDescriptionUI(response.toString(), courseId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.hideProgressBar();
                        Toast.makeText(context, "Courses: " + error.getStackTrace(), Toast.LENGTH_SHORT).show();
                        mListener.updateCourseDescriptionUI(error.getCause().toString(), null);
                    }
        });
        addToRequestQueue(jsonArrayRequest, "GET/courses/:id");
    }
}
