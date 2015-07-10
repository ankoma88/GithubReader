package com.ankoma88.githubreader.model;


import java.util.UUID;

public class Repo {

    private UUID mId;
    private String mRepoName;
    private String mLanguage;
    private int mForks;
    private int mStars;

    public Repo() {
        mId = UUID.randomUUID();
    }

    public Repo(String repoName, int forks, int stars) {
        mId = UUID.randomUUID();
        mRepoName = repoName;
        mForks = forks;
        mStars = stars;
    }

    public UUID getId() {
        return mId;
    }

    public String getRepoName() {
        return mRepoName;
    }

    public void setRepoName(String repoName) {
        mRepoName = repoName;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public int getForks() {
        return mForks;
    }

    public void setForks(int forks) {
        mForks = forks;
    }

    public int getStars() {
        return mStars;
    }

    public void setStars(int stars) {
        mStars = stars;
    }

    @Override
    public String toString() {
        return mRepoName;
    }
}
