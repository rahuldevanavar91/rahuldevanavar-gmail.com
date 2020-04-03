package com.android.searchapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.searchapplication.R;
import com.android.searchapplication.adapter.SearchListAdapter;
import com.android.searchapplication.model.Hits;
import com.android.searchapplication.network.Resource;
import com.android.searchapplication.util.OnItemClickListener;
import com.android.searchapplication.viewModel.SearchViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Observer<Resource>, OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;
    private SearchViewModel mSearchViewModel;
    private SearchListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mSearchViewModel.getMutableLiveData().observeForever(this);
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.search_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar = findViewById(R.id.progress_bar);
        mSearchView = findViewById(R.id.search_view);


        findViewById(R.id.search__button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mSearchView.getQuery().toString();
                if (query.length() > 2) {
                    mSearchViewModel.getSearchResult(query);
                } else {
                    Toast.makeText(MainActivity.this, R.string.search_toast_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onChanged(Resource resource) {
        switch (resource.status) {
            case SUCCESS:
                if (mAdapter != null) {
                    mAdapter.updateData((List<Hits>) resource.data);
                } else {
                    mAdapter = new SearchListAdapter(this, (List<Hits>) resource.data, this);
                    mRecyclerView.setAdapter(mAdapter);
                }
                mProgressBar.setVisibility(View.GONE);
                break;
            case ERROR:
                Toast.makeText(getApplicationContext(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                break;
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(Object data, int position) {
        Intent intent = new Intent(this, SearchDetailScreen.class);
        intent.putExtra(getString(R.string.url), (String) data);
        startActivity(intent);
    }

    public void loadMore() {
        mSearchViewModel.loadMoreSearchResult();
    }
}
