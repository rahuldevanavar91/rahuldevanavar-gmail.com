package com.android.searchapplication.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.searchapplication.adapter.SearchListAdapter;
import com.android.searchapplication.model.Hits;
import com.android.searchapplication.model.SearchResponse;
import com.android.searchapplication.network.ApiEndPoint;
import com.android.searchapplication.network.Resource;
import com.android.searchapplication.network.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private List<Hits> mHitsList;
    private MutableLiveData<Resource> mMutableLiveData = new MutableLiveData<>();
    private ApiEndPoint mApiEndPoint;
    private int mPageNumber = 1;
    private String mQuery;

    public SearchViewModel(Application application) {
        super(application);
        mApiEndPoint = RetrofitService.getRetrofitInstance().create(ApiEndPoint.class);
        mHitsList = new ArrayList<>();
    }

    public MutableLiveData<Resource> getMutableLiveData() {
        return mMutableLiveData;
    }

    public void getSearchResult(String query) {
        mHitsList.clear();
        mQuery = query;
        doNetworkCall();
    }

    private void doNetworkCall() {
        mMutableLiveData.postValue(Resource.loading());
        //todo not added page number is remianing
        mApiEndPoint.getSearchResponse(mQuery).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (!mHitsList.isEmpty() && mHitsList.get(mHitsList.size() - 1).getViewType() == SearchListAdapter.VIEW_TYPE_LOAD_MORE) {
                    mHitsList.remove(mHitsList.remove(mHitsList.size() - 1));
                }
                if (response.body().getNumOfPages() > mPageNumber) {
                    Hits hits = new Hits();
                    hits.setViewType(SearchListAdapter.VIEW_TYPE_LOAD_MORE);
                    mHitsList.add(hits);
                }
                mHitsList.addAll(response.body().getHits());
                mMutableLiveData.postValue(Resource.success(mHitsList));
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                mMutableLiveData.postValue(Resource.error("Error"));
            }
        });
    }

    public void loadMoreSearchResult() {
        mPageNumber++;
        doNetworkCall();
    }
}
