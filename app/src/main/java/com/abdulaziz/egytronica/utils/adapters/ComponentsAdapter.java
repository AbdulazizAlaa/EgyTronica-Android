package com.abdulaziz.egytronica.utils.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.Component;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by abdulaziz on 3/8/17.
 */

public class ComponentsAdapter extends RecyclerView.Adapter<ComponentsAdapter.MyViewHolder>{

    private ArrayList<Component> components;
    private Context mContext;
    private Boolean isOwner;
    private int memberType;
    private final PublishSubject<Component> onClickSubject = PublishSubject.create();

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTV, nodesTV, statusTV, heatLossTV, effectOnPowerTV, pauseDescTV;
        public ImageView statusIV, typeIV;
        public CardView cardView;
        public SwitchCompat pauseSwitch;
        public EditText pauseTimeED;
        public Button pauseBtn;
        public View pauseLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.component_name_tv);
            nodesTV = (TextView) itemView.findViewById(R.id.component_nodes_tv);
            statusTV = (TextView) itemView.findViewById(R.id.component_status_tv);
            heatLossTV = (TextView) itemView.findViewById(R.id.component_heat_loss_tv);
            effectOnPowerTV = (TextView) itemView.findViewById(R.id.component_effect_on_power_tv);
            pauseDescTV = (TextView) itemView.findViewById(R.id.component_pause_desc_tv);

            pauseTimeED = (EditText) itemView.findViewById(R.id.component_pause_time_ed);

            statusIV = (ImageView) itemView.findViewById(R.id.component_status_iv);
            typeIV = (ImageView) itemView.findViewById(R.id.component_type_iv);

            cardView = (CardView) itemView.findViewById(R.id.component_card_view);

            pauseBtn = (Button) itemView.findViewById(R.id.component_pause_btn);

            pauseSwitch = (SwitchCompat) itemView.findViewById(R.id.component_pause_switch);

            pauseLayout = itemView.findViewById(R.id.component_pause_layout);
        }
    }

    public ComponentsAdapter(Context mContext, ArrayList<Component> components, Boolean isOwner, int memberType) {
        this.components = components;
        this.mContext = mContext;
        this.isOwner = isOwner;
        this.memberType = memberType;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_item_layout, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Component component = components.get(position);

        holder.nameTV.setText(component.getName());
        holder.nodesTV.setText(component.getNodes());
        holder.statusTV.setText(component.getStatus());
        holder.heatLossTV.setText(component.getHeatLoss()+" °C");
        holder.effectOnPowerTV.setText(component.getEffectOnPower()+" mw");

        if(isOwner || memberType == 0){//owner or engineer
            holder.pauseLayout.setVisibility(View.VISIBLE);
        }else{
            holder.pauseLayout.setVisibility(View.GONE);
        }

        if(component.getIsPaused().equals("0")){
            holder.pauseSwitch.setChecked(false);
        }else{
            holder.pauseSwitch.setChecked(true);
            holder.pauseTimeED.setVisibility(View.VISIBLE);
            holder.pauseDescTV.setVisibility(View.VISIBLE);
            holder.pauseBtn.setVisibility(View.VISIBLE);
        }

        holder.pauseTimeED.setText(component.getPauseTime());

        holder.pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                component.setIsPaused("1");
                component.setPauseTime(holder.pauseTimeED.getText().toString());
                onClickSubject.onNext(component);
            }
        });

        holder.pauseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    component.setIsPaused("1");
                    component.setPauseTime(holder.pauseTimeED.getText().toString());

                    holder.pauseSwitch.animate()
                            .translationX(-holder.pauseSwitch.getWidth())
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    holder.pauseSwitch.animate()
                                            .translationX(0)
                                            .setDuration(0);
                                    holder.pauseTimeED.setVisibility(View.VISIBLE);
                                    holder.pauseBtn.setVisibility(View.VISIBLE);
                                    holder.pauseDescTV.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                }else{
                    component.setIsPaused("0");
                    component.setPauseTime("0");

                    onClickSubject.onNext(component);

                    holder.pauseTimeED.setVisibility(View.INVISIBLE);
                    holder.pauseBtn.setVisibility(View.INVISIBLE);
                    holder.pauseDescTV.setVisibility(View.INVISIBLE);
                    holder.pauseSwitch.animate()
                            .translationX(holder.pauseSwitch.getWidth())
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    holder.pauseSwitch.animate()
                                            .translationX(0)
                                            .setDuration(0);
                                    holder.pauseTimeED.setVisibility(View.GONE);
                                    holder.pauseBtn.setVisibility(View.GONE);
                                    holder.pauseDescTV.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                }
            }
        });

        switch (component.getColorCode()){
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

        switch (component.getType()){
            case 1:
                holder.typeIV.setImageResource(R.drawable.resistor);
                break;
            case 2:
                holder.typeIV.setImageResource(R.drawable.inductor);
                break;
            case 3:
                holder.typeIV.setImageResource(R.drawable.capacitor);
                break;
            case 4:
                holder.typeIV.setImageResource(R.drawable.diode);
                break;
            case 5:
                holder.typeIV.setImageResource(R.drawable.transistor);
                break;
            case 6:
                holder.typeIV.setImageResource(R.drawable.chip);
                break;
            default:
                holder.typeIV.setImageResource(R.drawable.resistor);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    public Observable<Component> getPositionClicks(){return onClickSubject;}

}
