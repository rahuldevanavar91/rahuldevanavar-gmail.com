package com.android.searchapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.searchapplication.R;
import com.android.searchapplication.model.Hits;
import com.android.searchapplication.util.OnItemClickListener;
import com.android.searchapplication.view.MainActivity;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    public static int VIEW_TYPE_LOAD_MORE = 1;
    private List<Hits> mHitsList;
    private OnItemClickListener mOnItemClickListener;
    private int lastMoreRequestPos;
    private Context mContext;

    public SearchListAdapter(Context context, List<Hits> list, OnItemClickListener listener) {
        mContext = context;
        mHitsList = list;
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laod_more_layout, parent, false), viewType);
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false), viewType);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hits item = mHitsList.get(position);
        if (getItemViewType(position) != VIEW_TYPE_LOAD_MORE) {
            holder.title.setText(item.getTitle());
            holder.createdOn.setText(item.getCreatedAt());
            holder.author.setText(item.getAuthor());
            holder.itemView.setTag(position);
        } else {
            if (lastMoreRequestPos != position) {
                lastMoreRequestPos = position;
                ((MainActivity)mContext).loadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHitsList != null ? mHitsList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mHitsList.get(position).getViewType();
    }

    public void updateData(List<Hits> data) {
        mHitsList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView createdOn;

        public ViewHolder(@NonNull final View itemView, int viewType) {
            super(itemView);
            if (viewType != VIEW_TYPE_LOAD_MORE) {
                title = itemView.findViewById(R.id.title);
                author = itemView.findViewById(R.id.author);
                createdOn = itemView.findViewById(R.id.created_at);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) itemView.getTag();
                        mOnItemClickListener.onItemClick(mHitsList.get(pos).getUrl(), pos);
                    }
                });
            }
        }
    }
}
