package com.ankoma88.githubreader.model;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.UUID;

public class GitUser {
    public static final String TAG = "GitUser";

    private UUID mId;
    private String mUsername;
    private String mCompany;
    private int mFollowers;
    private int mFollowing;
    private String mHTMLUrl;
    private String mReposUrl;
    private String mAvatarUrl;
    private Bitmap mAvatar;
    private ArrayList<Repo> mRepos = new ArrayList<>();


    public GitUser() {
        mId = UUID.randomUUID();
    }


    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public int getFollowers() {
        return mFollowers;
    }

    public void setFollowers(int followers) {
        mFollowers = followers;
    }

    public int getFollowing() {
        return mFollowing;
    }

    public void setFollowing(int following) {
        mFollowing = following;
    }

    public String getHTMLUrl() {
        return mHTMLUrl;
    }

    public void setHTMLUrl(String HTMLUrl) {
        mHTMLUrl = HTMLUrl;
    }

    public String getReposUrl() {
        return mReposUrl;
    }

    public void setReposUrl(String reposUrl) {
        mReposUrl = reposUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        mAvatar = avatar;
    }

    public ArrayList<Repo> getRepos() {
        return mRepos;
    }

    public void setRepos(ArrayList<Repo> repos) {
        mRepos = repos;
    }

    @Override
    public String toString() {
        return mUsername;
    }




}
