package com.ansaridroid.csacademy.util.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ansaridroid.csacademy.R;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private View fragmetLayout;
    private Authenticate authenticate;
    private TextView welcomeMessageTextView;
    private Button verifyEmailButton;

    private OnFragmentInteractionListener mListener;

    //public constructor
    public VerifyEmailFragment() {

    }

    public static VerifyEmailFragment newInstance(String param1, String param2) {
        VerifyEmailFragment fragment = new VerifyEmailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        authenticate = new Authenticate(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmetLayout = inflater.inflate(R.layout.verify_email, container, false);
        findViewsById();
        setWelcomeMessage();
        setOnClickListeners();
        return fragmetLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        //Checking if container activity implements fragment interaction listeners
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setOnClickListeners() {
        verifyEmailButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_email_button:
                authenticate.sendVerificationLink();
                break;
        }
    }


    public void findViewsById() {
        welcomeMessageTextView = fragmetLayout.findViewById(R.id.welcome_text_view);
        verifyEmailButton = fragmetLayout.findViewById(R.id.verify_email_button);
    }

    public void setWelcomeMessage() {
        FirebaseUser user = authenticate.getCurrentUser();
        String message = "Welcome "+ user.getDisplayName()+ " to CSAcademy";
        welcomeMessageTextView.setText(message);
    }
}
