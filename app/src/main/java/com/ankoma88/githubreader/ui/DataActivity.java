package com.ankoma88.githubreader.ui;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankoma88.githubreader.R;
import com.ankoma88.githubreader.model.GitUser;
import com.ankoma88.githubreader.model.GitUserHolder;
import com.ankoma88.githubreader.util.DBHelper;
import com.ankoma88.githubreader.util.RoundImage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class DataActivity extends BaseFragmentActivity implements ReposFragment.Callbacks {
    public static final String TAG = "DataActivity";

    //needed for facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private GitUser mGitUser;
    DBHelper mDBHelper;

    ImageView mAvatar;
    TextView mUsernameCompanyTv;
    Button mFollowersBtn;
    Button mFollowingBtn;
    ImageButton mBrowserBtn;
    ImageButton mSaveBtn;
    ImageButton mShareBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGitUser = GitUserHolder.gitUser;
        Log.d(TAG, "mGitUser: " + mGitUser);

        //Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(DataActivity.this, getResources().getString(R.string.posted), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(DataActivity.this, getResources().getString(R.string.posting_cancelled), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(DataActivity.this, getResources().getString(R.string.posting_error), Toast.LENGTH_SHORT).show();
            }

        });

        //Subclass of SQLiteOpenHelper
        mDBHelper = new DBHelper(DataActivity.this);

        //Setup avatar image
        mAvatar = (ImageView) findViewById(R.id.avatar);
        Bitmap bm = null;
        if (mGitUser.getAvatar() != null) {
            bm = mGitUser.getAvatar();
        } else {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        }
        RoundImage roundedImage = new RoundImage(bm);
        mAvatar.setImageDrawable(roundedImage);

        //Setup title (username and company)
        mUsernameCompanyTv = (TextView) findViewById(R.id.username_company);
        mUsernameCompanyTv.setText(mGitUser.getUsername() + ", " + mGitUser.getCompany());

        //Setup followers
        mFollowersBtn = (Button) findViewById(R.id.btn_followers);
        mFollowersBtn.setText(String.valueOf(mGitUser.getFollowers()) +" "+ getResources().getString(R.string.def_followers));

        //Setup following
        mFollowingBtn = (Button) findViewById(R.id.btn_following);
        mFollowingBtn.setText(String.valueOf(mGitUser.getFollowing()) +" "+ getResources().getString(R.string.def_following));

        //Setup open in browser button
        mBrowserBtn = (ImageButton) findViewById(R.id.btn_browser);
        mBrowserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mGitUser.getHTMLUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Save username, followers and following to database
        mSaveBtn = (ImageButton) findViewById(R.id.btn_save);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                cv.put("username", mGitUser.getUsername());
                cv.put("followers", mGitUser.getFollowers());
                cv.put("following", mGitUser.getFollowing());

                db.insert("gituser_table", null, cv);
                Log.d(TAG, "Data saved to database!");
                Toast.makeText(DataActivity.this, getResources().getString(R.string.data_saved), Toast.LENGTH_SHORT).show();

                checkDataBase(db);

                mDBHelper.close();
            }
        });

        //Post profile link to Facebook
        mShareBtn = (ImageButton) findViewById(R.id.btn_share);
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(getResources().getString(R.string.interesting_github))
                            .setContentDescription(getResources().getString(R.string.useful_projects))
                            .setContentUrl(Uri.parse(mGitUser.getHTMLUrl()))
                            .build();

                    shareDialog.show(linkContent);
                }
            }
        });

    }

    @Override
    protected Fragment createFragment() {
        return new ReposFragment();
    }

    @Override
    public GitUser getUser() {
        return mGitUser;
    }

    //Get rows from database and write to log. Method is not necessary, it's just to be sure that the data was inserted correctly.
    private void checkDataBase(SQLiteDatabase db) {
        Cursor c = db.query("gituser_table", null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int usernameColIndex = c.getColumnIndex("username");
            int followersColIndex = c.getColumnIndex("followers");
            int followingColIndex = c.getColumnIndex("following");

            do {
                Log.d(TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", username = " + c.getString(usernameColIndex) +
                                ", followers = " + c.getInt(followersColIndex) +
                                ", following = " + c.getInt(followingColIndex));
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

    }

    //After sharing to Facebook
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
