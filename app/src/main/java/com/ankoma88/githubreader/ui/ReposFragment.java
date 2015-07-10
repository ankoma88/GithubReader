package com.ankoma88.githubreader.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ankoma88.githubreader.R;
import com.ankoma88.githubreader.model.GitUser;
import com.ankoma88.githubreader.model.Repo;

import java.util.ArrayList;


public class ReposFragment extends ListFragment {
    private static final String TAG = "DataFragment";

    private Callbacks mCallbacks;

    public interface Callbacks {
        GitUser getUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ArrayList<Repo> reposList = mCallbacks.getUser().getRepos();

        ReposAdapter adapter = new ReposAdapter(reposList);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private class ReposAdapter extends ArrayAdapter<Repo>{
        public ReposAdapter(ArrayList<Repo> repos) {
            super(getActivity(), 0, repos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_gitrepo, parent, false);
            }

            Repo r = getItem(position);

            //Setup repo name
            TextView repoNameTextView = (TextView)convertView.findViewById(R.id.gitrepo_list_item_repoName);
            repoNameTextView.setText(r.getRepoName());

            //Setup repo language
            TextView language = (TextView)convertView.findViewById(R.id.gitrepo_list_item_language);
            language.setText(getResources().getString(R.string.language) +" "+ r.getLanguage());

            //Setup repo forks
            TextView forksCount = (TextView)convertView.findViewById(R.id.gitrepo_list_item_countForks);
            forksCount.setText(Integer.toString(r.getForks()));

            //Setup repo stars
            TextView starsCount = (TextView)convertView.findViewById(R.id.gitrepo_list_item_countStars);
            starsCount.setText(Integer.toString(r.getStars()));

            return convertView;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
