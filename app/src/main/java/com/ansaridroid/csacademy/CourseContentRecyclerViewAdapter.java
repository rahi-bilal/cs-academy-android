package com.ansaridroid.csacademy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CourseContentRecyclerViewAdapter extends RecyclerView.Adapter<CourseContentRecyclerViewAdapter.CourseContentViewHolder> {

    JSONArray courseDescriptionArray;

    public CourseContentRecyclerViewAdapter(JSONArray courseDescriptionArray) {
        this.courseDescriptionArray= courseDescriptionArray;

    }

    @Override
    public CourseContentViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_content_container, parent, false);
        return new CourseContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseContentViewHolder courseContentViewHolder, int position) {
        JSONObject lessonObject;
        String lessonTitle, lessonUrl;
        try{
            lessonObject = courseDescriptionArray.getJSONObject(position);
            lessonTitle = lessonObject.getString("title");
            lessonUrl = lessonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        courseContentViewHolder.lessonTitleTextView.setText(lessonTitle);
        courseContentViewHolder.lessonNumberTextView.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return courseDescriptionArray.length();
    }

    public static class CourseContentViewHolder extends RecyclerView.ViewHolder {
        private TextView lessonNumberTextView;
        private TextView lessonTitleTextView;
        public CourseContentViewHolder(View view) {
            super(view);
            lessonNumberTextView = view.findViewById(R.id.lesson_number_text_view);
            lessonTitleTextView = view.findViewById(R.id.lesson_title_text_view);
        }

    }
}
