package com.ansaridroid.csacademy;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansaridroid.csacademy.util.adapter.NavListAdapter;
import com.ansaridroid.csacademy.util.authentication.Authenticate;
import com.ansaridroid.csacademy.util.authentication.LoginSignupFragment;
import com.ansaridroid.csacademy.util.authentication.OnFragmentInteractionListener;
import com.ansaridroid.csacademy.util.authentication.VerifyEmailFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, View.OnClickListener {
    private FirebaseUser user;
    private Authenticate authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                user = authenticate.getCurrentUser();
                updateNavigationUI(user);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //getting current user
        authenticate = new Authenticate(this);
        user = authenticate.getCurrentUser();

        //update UI
        updateUI(user);

        //setting up onClickListeneres
        setOnClickListeners();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                //updating navBarListView
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                updateNavBarListView(null);

                //Updating main layout
                FragmentManager fm = getSupportFragmentManager();
                user = authenticate.getCurrentUser();
                updateUI(user);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_button:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                updateNavBarListView(null);
                authenticate.logoutUser();
                break;
        }
    }


    //Set onClick listeners

    public void setOnClickListeners() {

        //For logout Button
        NavigationView navView = findViewById(R.id.nav_view);
        View navHeader = navView.getHeaderView(0);
        Button logoutButton = navHeader.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);
    }

    //Update the UI with the current user
    @Override
    public void updateUI(FirebaseUser currentUser) {
        FragmentManager fm = getSupportFragmentManager();
        if (currentUser == null) {
            Fragment loginSignUpFragment = LoginSignupFragment.newInstance(null, null);
            fm.beginTransaction().replace(R.id.main_content_container, loginSignUpFragment).commit();
        } else if (!currentUser.isEmailVerified()) {
            Fragment verifyEmailFragment = VerifyEmailFragment.newInstance(null, null);
            fm.beginTransaction().replace(R.id.main_content_container, verifyEmailFragment).commit();
        } else {
            Fragment dashboardFragment = DashBoardFragment.newInstance();
            fm.beginTransaction().replace(R.id.main_content_container, dashboardFragment).commit();
        }
    }

    @Override
    public void showProgressBar() {
        View progressOverLay = findViewById(R.id.progress_overlay);
        progressOverLay.bringToFront();
        progressOverLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        View progressOverLay = findViewById(R.id.progress_overlay);
        progressOverLay.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        TextView errorMessageTextView = findViewById(R.id.message_text_view);
        if (message!= null) {
            errorMessageTextView.setText(message);
        }
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateDashboardUI(String jsonResult){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        JSONArray courseArray;
        JSONObject courseObject= null;
        try{
            courseArray = new JSONArray(jsonResult);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        LinearLayout container = findViewById(R.id.courses_container_linear_layout);
        LayoutInflater inflater =  getLayoutInflater();

        for (int i = 0; i < courseArray.length(); i++) {
            View view = inflater.inflate(R.layout.course_container, container, false);
            TextView titleTV= view.findViewById(R.id.course_title_text_view);
            TextView authorTV= view.findViewById(R.id.course_author_text_vew);
            ImageView courseIV= view.findViewById(R.id.course_image_view);
            final String courseId;
            try {
                courseObject = courseArray.getJSONObject(i);
                titleTV.setText(courseObject.getString("title"));
                authorTV.setText(courseObject.getString("author"));
                courseId = courseObject.getString("courseId");
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseContentFragment courseDescriptionFragment = CourseContentFragment.newInstance(courseId);
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.main_content_container, courseDescriptionFragment).commit();
                }
            });
            container.addView(view, i);
        }

    }

    @Override
    public void updateCourseDescriptionUI(final String jsonResult, final String courseId) {
        JSONArray courseDescriptionArray= null;

        try{
            courseDescriptionArray = new JSONArray(jsonResult);
        }catch (JSONException e) {
            Toast.makeText(this, "JSON Exception", Toast.LENGTH_SHORT).show();
        }
        RecyclerView courseContentsRecyclerView = findViewById(R.id.course_contents_recycler_view);
        courseContentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter courseContentAdapter = new CourseContentRecyclerViewAdapter(courseDescriptionArray);
        courseContentsRecyclerView.setAdapter(courseContentAdapter);

        //Setting Listener for Start learning(join_button)
        Button joinCourseButton = findViewById(R.id.join_button);
        joinCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment courseLessonFragment = CourseLessonFragment.newInstance(courseId, jsonResult);
                fm.beginTransaction().replace(R.id.main_content_container, courseLessonFragment).commit();
            }
        });
    }

    @Override
    public void updateNavBarListView(final JSONArray courseDescriptionArray) {
        ListView navBarListView = findViewById(R.id.nav_bar_list_view);
        if (courseDescriptionArray == null) {
            navBarListView.setAdapter(null);
            return;
        }

        JSONObject jsonObject;
        final String data[] = new String[courseDescriptionArray.length()];
        for (int i= 0; i<courseDescriptionArray.length(); i++) {
            try {
                jsonObject = courseDescriptionArray.getJSONObject(i);
                data[i] = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        NavListAdapter navListAdapter = new NavListAdapter(this, R.layout.nav_bar_list_item, data);
        navBarListView.setAdapter(navListAdapter);

        navBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                WebView courseLessonWebView = findViewById(R.id.course_lessons_web_view);

                //closing drawer
                drawer.closeDrawer(GravityCompat.START);

                //updating lesson index
                CourseLessonFragment.LESSON_INDEX = position;

                JSONObject jsonObject;
                String url = null;
                try {
                    jsonObject = courseDescriptionArray.getJSONObject(position);
                    url = jsonObject.getString("url");
                    courseLessonWebView.loadUrl(CourseLessonFragment.BASE_URL.concat(url));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateNavigationUI(FirebaseUser currentUser) {
        //Referencing all views in header
        NavigationView navView = findViewById(R.id.nav_view);
        View navHeader = navView.getHeaderView(0);
        TextView displayNameTextView = navHeader.findViewById(R.id.username_text_view);
        TextView emailIdTextView = navHeader.findViewById(R.id.user_id_text_view);
        TextView userPhotoTextView = navHeader.findViewById(R.id.user_photo_text_view);
        Button logoutButton = navHeader.findViewById(R.id.logout_button);

        if(currentUser!= null){
            //Setting logout button visible
            logoutButton.setVisibility(View.VISIBLE);

            //Updating contents of navHeader
            String displayName = currentUser.getDisplayName().trim();
            userPhotoTextView.setText(""+displayName.charAt(0));
            displayNameTextView.setText(displayName);
            emailIdTextView.setText(currentUser.getEmail());
        }else {
            //Setting up logout button invisible
            logoutButton.setVisibility(View.GONE);

            userPhotoTextView.setText("");
            displayNameTextView.setText(getString(R.string.nav_header_title));
            emailIdTextView.setText(getString(R.string.nav_header_subtitle));
        }

    }

}
