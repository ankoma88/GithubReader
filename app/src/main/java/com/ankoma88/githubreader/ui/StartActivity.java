package com.ankoma88.githubreader.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ankoma88.githubreader.R;
import com.ankoma88.githubreader.model.GitUser;
import com.ankoma88.githubreader.model.GitUserHolder;
import com.ankoma88.githubreader.service.DataLoader;
import com.ankoma88.githubreader.util.CheckInternet;


public class StartActivity extends Activity {
    public static final String TAG = "StartActivity";
    private EditText mInput;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mInput = (EditText) findViewById(R.id.input_usrnm);
    }

    public void loadAndStart(View view) {

        //Check if internet available
        if (CheckInternet.isInternetAvailable(this)) {

            //Check if user entered input username
            mUsername = mInput.getText().toString();
            if ("".equals(mUsername)) {
                Toast.makeText(this, getResources().getString(R.string.enter_usrnm), Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, mUsername);
                FetchDataTask fetchDataTask = new FetchDataTask();
                fetchDataTask.execute();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchDataTask extends AsyncTask<Void, Void, GitUser> {
        ProgressDialog progressDialog = new ProgressDialog(StartActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle(getResources().getString(R.string.downloading));
            progressDialog.setMessage(getResources().getString(R.string.wait));
            progressDialog.show();
        }

        @Override
        protected GitUser doInBackground(Void... params) {
            return DataLoader.getGitUser(mUsername);
        }

        @Override
        protected void onPostExecute(GitUser gitUser) {
            super.onPostExecute(gitUser);

            if (gitUser.getUsername() == null) {
                Toast.makeText(StartActivity.this, getResources().getString(R.string.usr_not_found), Toast.LENGTH_SHORT).show();
            } else {

                Log.d(TAG, "gitUser name: " + gitUser);
                Log.d(TAG, "gitUser company: " + gitUser.getCompany());
                Log.d(TAG, "gitUser html_url: " + gitUser.getHTMLUrl());
                Log.d(TAG, "gitUser followers: " + gitUser.getFollowers());
                Log.d(TAG, "gitUser following: " + gitUser.getFollowing());
                Log.d(TAG, "gitUser repos_url: " + gitUser.getReposUrl());
                Log.d(TAG, "gitUser avatar_url: " + gitUser.getAvatarUrl());
                Log.d(TAG, "gitUser repos count: " + gitUser.getRepos().size());
                Log.d(TAG, "gitUser avatar is setup: " + (gitUser.getAvatar() != null));

                GitUserHolder.gitUser = gitUser;

                Intent i = new Intent(StartActivity.this, DataActivity.class);
                startActivity(i);
            }
            progressDialog.dismiss();
        }
    }

}
