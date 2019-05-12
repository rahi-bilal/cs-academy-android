package com.ansaridroid.csacademy.util.authentication;


import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

public interface OnFragmentInteractionListener {
    void updateUI(FirebaseUser currentUser);
    void updateDashboardUI(String jsonResult);
    void updateCourseDescriptionUI(String jsonResult, String courseId);
    void updateNavBarListView(JSONArray courseDescriptionArray);
    void showMessage(String message);
    void showProgressBar();
    void hideProgressBar();
}
