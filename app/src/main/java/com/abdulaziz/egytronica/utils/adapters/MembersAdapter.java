package com.abdulaziz.egytronica.utils.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.User;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by abdulaziz on 3/3/17.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder>{

    private ArrayList<User> users;
    private Context mContext;
    private final PublishSubject<User> onClickSubject = PublishSubject.create();

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTV, removeTV;

        public ViewHolder(View view){
            super(view);
            this.nameTV = (TextView) view.findViewById(R.id.members_item_member_name_tv);
            this.removeTV = (TextView) view.findViewById(R.id.members_item_remove_tv);
        }
    }

    public MembersAdapter(Context mContext, ArrayList<User> users) {
        this.users = users;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_item_layout, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.nameTV.setText(user.getName());

        holder.removeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public Observable<User> getPositionClicks(){return onClickSubject;}
}
