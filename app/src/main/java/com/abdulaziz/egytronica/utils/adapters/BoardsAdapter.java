package com.abdulaziz.egytronica.utils.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.Board;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by abdulaziz on 3/2/17.
 */

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.MyViewHolder>{

    private ArrayList<Board> boards;
    private Context mContext;
    private final PublishSubject<Board> onClickSubject = PublishSubject.create();

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTV;
        public ImageView statusIV;
        public CardView view;

        public MyViewHolder(View view) {
            super(view);
            this.nameTV = (TextView) view.findViewById(R.id.board_item_name_tv);
            this.statusIV = (ImageView) view.findViewById(R.id.board_item_status_iv);
            this.view = (CardView) view.findViewById(R.id.home_card_view);
        }
    }

    public BoardsAdapter(Context mContext, ArrayList<Board> boards) {
        this.boards = boards;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_list_layout, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Board board = boards.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(board);
            }
        });

        holder.nameTV.setText(board.getName());

        switch (board.getColorCode()){
            case 1:
                holder.statusIV.setImageResource(R.drawable.board_status_shape_green);
                break;
            case 2:
                holder.statusIV.setImageResource(R.drawable.board_status_shape_gray);
                break;
            case 3:
                holder.statusIV.setImageResource(R.drawable.board_status_shape_orange);
                break;
            case 4:
                holder.statusIV.setImageResource(R.drawable.board_status_shape_red);
                break;
            default:
                holder.statusIV.setImageResource(R.drawable.board_status_shape_green);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public Observable<Board> getPositionClicks(){return onClickSubject;}

}

