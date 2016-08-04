package com.ankoma88.githubreader.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ankoma88.githubreader.model.GitUser;
import com.ankoma88.githubreader.model.Repo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DataLoader {
    public static final String TAG = "DataLoader";
    private static final String GITHUB_API_USERS_ROOT = "https://api.github.com/users/";

    public static GitUser getGitUser(String username) {
        JsonObject jsonUser = getJSONUser(username);
        if (jsonUser == null) {
            return new GitUser();
        }
        GitUser gitUser = parseJSONUser(jsonUser);
        JsonArray jsonRepos = getRepos(gitUser.getReposUrl());
        ArrayList<Repo> repos = parseRepos(jsonRepos);
        gitUser.setRepos(repos);
        Bitmap avatar = getAvatar(gitUser.getAvatarUrl());
        gitUser.setAvatar(avatar);
        return gitUser;
    }


    public static JsonObject getJSONUser(String data) {
        URL url = null;
        JsonObject rootObj = new JsonObject();
        try {
            url = new URL(GITHUB_API_USERS_ROOT + data);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            rootObj = root.getAsJsonObject();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "User not found!");
            return null;
        }

        return rootObj;
    }

    public static GitUser parseJSONUser(JsonObject jsonObject) {
        GitUser gitUser = new GitUser();
        gitUser.setUsername(jsonObject.get("login").getAsString());
        gitUser.setFollowers(jsonObject.get("followers").getAsInt());
        gitUser.setFollowing(jsonObject.get("following").getAsInt());
        gitUser.setHTMLUrl(jsonObject.get("html_url").getAsString());
        gitUser.setReposUrl(jsonObject.get("repos_url").getAsString());
        gitUser.setAvatarUrl(jsonObject.get("avatar_url").getAsString());

        //Company is not always present
//        JsonPrimitive compJson = (JsonPrimitive) jsonObject.get("company");
//        if (compJson != null) {
//            gitUser.setCompany(compJson.getAsString());
//        } else gitUser.setCompany("no company");

        return gitUser;
    }

    public static JsonArray getRepos(String reposUrl) {
        URL url = null;
        JsonArray rootJsonArray = new JsonArray();
        try {
            url = new URL(reposUrl);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            rootJsonArray = (JsonArray) jp.parse(new InputStreamReader((InputStream) request.getContent()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Repos not found!");
            return rootJsonArray;
        }

        return rootJsonArray;

    }

    public static ArrayList<Repo> parseRepos(JsonArray jsonArray) {
        ArrayList<Repo> reposList = new ArrayList<>();

        for(int i=0;i<jsonArray.size();i++) {
            JsonObject jsonRepo = (JsonObject) jsonArray.get(i);
            String name = jsonRepo.get("name").getAsString();
            int forks = jsonRepo.get("forks_count").getAsInt();
            int stars = jsonRepo.get("stargazers_count").getAsInt();
            Repo repo = new Repo(name, forks, stars);

            //Language is not always present
            JsonElement langJson = jsonRepo.get("language");
            if (!langJson.isJsonNull()) {
                repo.setLanguage(langJson.getAsString());
            } else repo.setLanguage("several languages");

            reposList.add(repo);
        }

        return reposList;
    }

    public static Bitmap getAvatar(String avatarUrl) {
        try {
            URL url = new URL(avatarUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap avatar = BitmapFactory.decodeStream(input);
            return avatar;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }


}