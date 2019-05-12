package com.ansaridroid.csacademy.util.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.ansaridroid.csacademy.R;


public class LoginSignupFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private Authenticate authenticate;

    private View mainView;
    private EditText fullNameET, signUpEmailET, signUpPasswordET, signUpConfirmPasswordET;
    private EditText loginEmailET, loginPasswordET;
    private TextView messageTextView;
    private Button loginButton, signUpButton, loginFormButton, signUpFormButton;

    private OnFragmentInteractionListener mListener;

    public LoginSignupFragment() {
        // Required empty public constructor
    }


    public static LoginSignupFragment newInstance(String param1, String param2) {
        LoginSignupFragment fragment = new LoginSignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticate = new Authenticate(getActivity());
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mainView= inflater.inflate(R.layout.fragemnt_login_signup, container, false);
        findViewsById();
        setOnClickListeners();
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= context;

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_header_button:

            case R.id.register_header_button:
                swapForm(v.getId());
                break;

            case R.id.register_button:
                if (validateSignUpForm()) {
                    String email = signUpEmailET.getText().toString();
                    String password = signUpPasswordET.getText().toString();
                    String displayName = fullNameET.getText().toString();
                    authenticate.createUser(email, password, displayName);
                }
                break;

            case R.id.login_button:
                if (validateLoginForm()) {
                    String email = loginEmailET.getText().toString();
                    String password = loginPasswordET.getText().toString();
                    authenticate.login(email, password);
                }
                break;

        }
    }

    private void findViewsById() {
        //SignUp fields
        fullNameET= mainView.findViewById(R.id.register_full_name_edit_text);
        signUpEmailET= mainView.findViewById(R.id.register_email_edit_text);
        signUpPasswordET= mainView.findViewById(R.id.register_password_edit_text);
        signUpConfirmPasswordET= mainView.findViewById(R.id.register_confirm_password_edit_text);

        //Login fields
        loginEmailET = mainView.findViewById(R.id.login_email_edit_text);
        loginPasswordET = mainView.findViewById(R.id.login_password_edit_text);
        messageTextView = mainView.findViewById(R.id.message_text_view);

        //SubmitButtons
        signUpButton= mainView.findViewById(R.id.register_button);
        loginButton= mainView.findViewById(R.id.login_button);

        //Form Switch buttons
        loginFormButton= mainView.findViewById(R.id.login_header_button);
        signUpFormButton= mainView.findViewById(R.id.register_header_button);

    }

    public void setOnClickListeners() {
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        loginFormButton.setOnClickListener(this);
        signUpFormButton.setOnClickListener(this);

    }

    //Validate SignUp Form
    private Boolean validateSignUpForm() {
        boolean valid= true;
        String email= signUpEmailET.getText().toString();
        String name= fullNameET.getText().toString();
        String password= signUpPasswordET.getText().toString();
        String confirmPassword= signUpConfirmPasswordET.getText().toString();

        if (TextUtils.isEmpty(name)){
            fullNameET.setError("Required.");
            valid= false;
        }else {
            fullNameET.setError(null);
        }

        if (TextUtils.isEmpty(email)){
            signUpEmailET.setError("Required.");
            valid= false;
        }else {
            signUpEmailET.setError(null);
        }

        if (password.length()<8){
            signUpPasswordET.setError("Minimum length is 8!");
            valid= false;
        }else {
            signUpPasswordET.setError(null);
        }

        if (!(password.equals(confirmPassword))){
            signUpConfirmPasswordET.setError("Password doesn't match.");
            valid= false;
        }else {
            signUpConfirmPasswordET.setError(null);
        }
        return valid;
    }

    //Validate Login Form
    private Boolean validateLoginForm() {
        boolean isValid = true;
        String email = loginEmailET.getText().toString();
        String password = loginPasswordET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmailET.setError("Required");
            isValid = false;
        }else {
            loginEmailET.setError(null);
        }

        if (password.length()== 0) {
            loginPasswordET.setError("Required");
            isValid = false;
        }else {
            loginPasswordET.setError(null);
        }

        return isValid;
    }

    private void swapForm(int id) {

        //Set errorMessageTextView Visibility to GONE if visible
        if (messageTextView.getVisibility() == View.VISIBLE) {
            messageTextView.setVisibility(View.GONE);
        }

        if(id== R.id.login_header_button) {
            mainView.findViewById(R.id.register_container_layout).setVisibility(View.GONE);
            mainView.findViewById(R.id.login_container_layout).setVisibility(View.VISIBLE);

            loginFormButton.setBackground(getResources().getDrawable(R.drawable.login_button_background));
            loginFormButton.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));

            signUpFormButton.setBackground(null);
            signUpFormButton.setTextColor(getResources().getColor(R.color.black));
        }else {
            mainView.findViewById(R.id.login_container_layout).setVisibility(View.GONE);
            mainView.findViewById(R.id.register_container_layout).setVisibility(View.VISIBLE);

            signUpFormButton.setBackground(getResources().getDrawable(R.drawable.register_button_background));
            signUpFormButton.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));

            loginFormButton.setBackground(null);
            loginFormButton.setTextColor(getResources().getColor(R.color.black));

        }
    }
}
