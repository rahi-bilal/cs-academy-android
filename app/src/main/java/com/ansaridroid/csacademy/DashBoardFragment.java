package com.ansaridroid.csacademy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ansaridroid.csacademy.util.authentication.Authenticate;
import com.ansaridroid.csacademy.util.authentication.OnFragmentInteractionListener;
import com.ansaridroid.csacademy.util.http.HTTPRequest;

public class DashBoardFragment extends Fragment {

    private Context context;
    private View fragmentLayout;
    private Authenticate authenticate;
    private HTTPRequest httpRequest;

    private OnFragmentInteractionListener mListener;

    //Default constructor
    public DashBoardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        authenticate = new Authenticate(context);
        httpRequest = new HTTPRequest(context);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.user_dashboard, container, false);
        httpRequest.makeDashboardUIResourcesRequest();
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

    public static DashBoardFragment newInstance() {

        Bundle args = new Bundle();

        DashBoardFragment fragment = new DashBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
