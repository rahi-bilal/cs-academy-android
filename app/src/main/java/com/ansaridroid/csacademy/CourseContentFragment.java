package com.ansaridroid.csacademy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ansaridroid.csacademy.util.authentication.OnFragmentInteractionListener;
import com.ansaridroid.csacademy.util.http.HTTPRequest;

public class CourseContentFragment extends Fragment {

    private Context context;
    private View fragmentLayout;
    private HTTPRequest http;

    private static String courseId;

    private OnFragmentInteractionListener mListener;

    public static CourseContentFragment newInstance(String courseId) {
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        CourseContentFragment fragment = new CourseContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = new HTTPRequest(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String courseId= null;
        Bundle args = getArguments();
        if (args!= null) {
            courseId= args.getString("courseId");
        }
        fragmentLayout = inflater.inflate(R.layout.course_description_layout, container, false);
        if (courseId!= null) {
            http.makeCourseDescriptionRequest(courseId);
        }
        return fragmentLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener  = (OnFragmentInteractionListener) context;
        }

        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
