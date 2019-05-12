package com.ansaridroid.csacademy;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansaridroid.csacademy.util.adapter.NavListAdapter;
import com.ansaridroid.csacademy.util.authentication.OnFragmentInteractionListener;
import com.ansaridroid.csacademy.util.http.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseLessonFragment extends Fragment implements View.OnClickListener {

    private static final String COURSE_ID = "courseId";
    private static final String COURSE_DESCRIPTION_ARRAY = "courseDescriptionArray";

    private String courseId;
    private String courseDescriptionArrayString;
    private JSONArray courseDescriptionArray;
    private JSONObject jsonObject;
    private String urlString;
    public static int LESSON_INDEX = 0;
    public static String BASE_URL = "https://us-central1-csacademy-df2c9.cloudfunctions.net/server/";

    private View fragmentLayout;
    private WebView courseLessonWebView;
    private Button prevButton;
    private Button nextButton;
    private ListView navBarListView;

    private Context context;
    private HTTPRequest httpRequest;

    private OnFragmentInteractionListener mListener;

    public CourseLessonFragment() {

    }

    public static CourseLessonFragment newInstance(String courseId, String courseDescriptionArray) {
        CourseLessonFragment fragment = new CourseLessonFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_ID, courseId);
        args.putString(COURSE_DESCRIPTION_ARRAY, courseDescriptionArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting bundle arguments
        if (getArguments() != null) {
            courseId = getArguments().getString(COURSE_ID);
            courseDescriptionArrayString = getArguments().getString(COURSE_DESCRIPTION_ARRAY);
        }
        //Instantiating httpRequest object
        httpRequest = new HTTPRequest(context);

        //Instantiating JSONArray for course description
        try{
            courseDescriptionArray = new JSONArray(courseDescriptionArrayString);
            jsonObject= courseDescriptionArray.getJSONObject(LESSON_INDEX);
            urlString = jsonObject.getString("url");

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.course_lessons_layout, container, false);

        //getting views reference
        findViewsById();

        //set navBarListView
        mListener.updateNavBarListView(courseDescriptionArray);

        //setting click listeners for prev and next buttons
        setOnClickListeners();

        //loading courseLessonWebView
        WebSettings webSettings = courseLessonWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        courseLessonWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mListener.showProgressBar();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mListener.hideProgressBar();
            }
        });
        courseLessonWebView.loadUrl((BASE_URL.concat(urlString)));

        //returning inflated(created) layout
        return fragmentLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadLessonURL() {
        try {
            jsonObject= courseDescriptionArray.getJSONObject(LESSON_INDEX);
            urlString = jsonObject.getString("url");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        courseLessonWebView.loadUrl(BASE_URL.concat(urlString));
    }

    private void findViewsById() {
        courseLessonWebView = fragmentLayout.findViewById(R.id.course_lessons_web_view);
        prevButton = fragmentLayout.findViewById(R.id.previous_button);
        nextButton = fragmentLayout.findViewById(R.id.next_button);
        navBarListView = fragmentLayout.findViewById(R.id.nav_bar_list_view);
    }

    private void setOnClickListeners() {
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_button:
                if (LESSON_INDEX!= 0) {
                    LESSON_INDEX--;
                    loadLessonURL();
                }
                break;
            case R.id.next_button:
                if (LESSON_INDEX!= (courseDescriptionArray.length()-1)) {
                    LESSON_INDEX++;
                    loadLessonURL();
                }
                break;
        }
    }
}
