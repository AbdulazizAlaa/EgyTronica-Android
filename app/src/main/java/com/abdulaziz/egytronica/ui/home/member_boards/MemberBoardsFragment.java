package com.abdulaziz.egytronica.ui.home.member_boards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.ui.board.view.ViewBoardActivity;
import com.abdulaziz.egytronica.utils.adapters.BoardsAdapter;
import com.abdulaziz.egytronica.utils.DividerItemDecoration;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemberBoardsFragment.OnMemberBoardsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberBoardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberBoardsFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        MembersBoardsBaseView,
        View.OnClickListener{

    private ArrayList<Board> boards;

    private View main_view;

    private Toolbar toolbar;
    private Button logoutBtn;

    private BoardsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private SwipeRefreshLayout refreshLayout;

    private MemberBoardsPresenter mPresenter;

    private Request request;

    private OnMemberBoardsFragmentInteractionListener mListener;

    public MemberBoardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemberBoards.
     */
    public static MemberBoardsFragment newInstance() {
        MemberBoardsFragment fragment = new MemberBoardsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_boards, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init(){
        main_view = getView().findViewById(R.id.main_view);

        //toolbar menu
        toolbar = (Toolbar) getView().findViewById(R.id.members_boards_toolbar);
        logoutBtn = (Button) toolbar.findViewById(R.id.members_boards_logout_btn);

        logoutBtn.setOnClickListener(this);

        //Refresh layout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.members_boards_swipe_to_refresh);

        refreshLayout.setOnRefreshListener(this);

        //recycler view
        boards = new ArrayList<Board>();

        adapter = new BoardsAdapter(getContext(), boards);
        recyclerView = (RecyclerView) getView().findViewById(R.id.members_boards_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL_LIST));
        adapter.getPositionClicks()
                .subscribe(new Observer<Board>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Board board) {
                        Log.i(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "onNext: " +board.getName());
                        String jsonBoard = new Gson().toJson(board);

                        Intent i = ViewBoardActivity.getStartIntent(getContext());

                        i.putExtra(GlobalEntities.BOARD_TAG, jsonBoard);

                        startActivity(i);
                    }
                });

        recyclerView.setAdapter(adapter);

        //presenter
        mPresenter = new MemberBoardsPresenter(getContext(), DataManager.getInstance());
        mPresenter.attachView(this);

        // make a request to get user's boards
        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));

        mPresenter.getBoards(request);
        refreshLayout.setRefreshing(true);

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);

        boards.clear();
        adapter.notifyDataSetChanged();

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));

        mPresenter.getBoards(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.members_boards_logout_btn:
                Log.i(GlobalEntities.MY_BOARDS_ACTIVITY, "click: logout");
                mListener.onMemberBoardsFragmentInteraction(GlobalEntities.LOGOUT_TAG, "");
                break;
        }
    }

    @Override
    public void boardsReqSuccess(ArrayList<Board> boards) {
        Log.i(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "boardsReqSuccess: "+boards.size());

        this.boards.clear();
        this.boards.addAll(boards);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void boardsReqError(String e) {
        Log.i(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "boardsReqError: "+e);
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();

        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);

        //if error message contains any data about invalid token
        Utils.logout(getContext(), e);

    }

    @Override
    public void boardsReqComplete() {
        Log.i(GlobalEntities.MEMBERS_BOARDS_ACTIVITY, "boardsReqComplete: ");

        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMemberBoardsFragmentInteractionListener) {
            mListener = (OnMemberBoardsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMemberBoardsFragmentInteractionListener {
        void onMemberBoardsFragmentInteraction(String action, String message);
    }
}
