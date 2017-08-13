package com.abdulaziz.egytronica.ui.board.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.ui.board.view.components.ComponentsFragment;
import com.abdulaziz.egytronica.ui.board.view.details.DetailsFragment;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.PagerAdapter;
import com.google.gson.Gson;
import com.robohorse.pagerbullet.PagerBullet;

public class ViewBoardActivity extends AppCompatActivity implements
        ComponentsFragment.OnComponentsFragmentInteractionListener,
        DetailsFragment.OnDetailsFragmentInteractionListener{

    private PagerBullet viewPager;
    private PagerAdapter fragmentAdapter;

    private Board board;

    public static Intent getStartIntent(Context mContext){
        Intent i = new Intent(mContext, ViewBoardActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_board);

        //get board object from intent
        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                String jsonBoard = getIntent().getExtras().getString(GlobalEntities.BOARD_TAG);
                board = new Gson().fromJson(jsonBoard, Board.class);
            }
        }

        init();
    }

    private void init(){
        //pager
        viewPager = (PagerBullet) findViewById(R.id.view_board_pagerBullet);

        fragmentAdapter = new PagerAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(DetailsFragment.newInstance(board.getId()));
        fragmentAdapter.addFragment(ComponentsFragment.newInstance(board.getId()));

        viewPager.setAdapter(fragmentAdapter);

    }

    @Override
    public void onComponentsFragmentInteraction(String action, String message) {

    }

    @Override
    public void onDetailsFragmentInteraction(String action, String message) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
