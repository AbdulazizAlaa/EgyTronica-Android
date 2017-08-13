package com.abdulaziz.egytronica.utils.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.News;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by abdulaziz on 4/26/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<News> newsList;
    private final PublishSubject<News> onclickSubject = PublishSubject.create();

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News news = newsList.get(position);

        holder.titleTV.setText(news.getTitle());
        holder.contentTV.setText(news.getContent());

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSubject.onNext(news);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public Observable<News> getPositionClicks(){return onclickSubject;}

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View mainView;
        public TextView titleTV, contentTV, dateTV;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.news_item_card_view);

            titleTV = (TextView) itemView.findViewById(R.id.news_item_title_tv);
            contentTV = (TextView) itemView.findViewById(R.id.news_item_content_tv);
        }
    }
}
