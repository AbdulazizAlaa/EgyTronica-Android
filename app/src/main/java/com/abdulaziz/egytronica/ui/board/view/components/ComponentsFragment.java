package com.abdulaziz.egytronica.ui.board.view.components;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.abdulaziz.egytronica.data.model.Component;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.utils.DividerItemDecoration;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.BoardsAdapter;
import com.abdulaziz.egytronica.utils.adapters.ComponentsAdapter;
import com.abdulaziz.egytronica.utils.dialogs.AddComponentsDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComponentsFragment.OnComponentsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComponentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComponentsFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        ComponentsBaseView,
        AddComponentsDialog.ComponentsDialogInteractionInterface{

    private ArrayList<Component> components;

    private View main_view;

    private ComponentsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private SwipeRefreshLayout refreshLayout;

    private FloatingActionButton fab;

    private AddComponentsDialog addComponentsDialog;

    private ComponentsPresenter mPresenter;

    private Request request;
    private String board_id;

    private OnComponentsFragmentInteractionListener mListener;

    public ComponentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComponentsFragment.
     */
    public static ComponentsFragment newInstance(String board_id) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalEntities.BOARD_ID_TAG, board_id);

        ComponentsFragment fragment = new ComponentsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init(){
        main_view = getView().findViewById(R.id.main_view);

        //Refresh layout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.components_swipe_to_refresh);

        refreshLayout.setOnRefreshListener(this);

        //init add components dialog
        addComponentsDialog = new AddComponentsDialog(getContext(), 0);
        addComponentsDialog.setListener(this);

        //recycler view
        components = new ArrayList<Component>();

        adapter = new ComponentsAdapter(getContext(), components, false, 1);
        recyclerView = (RecyclerView) getView().findViewById(R.id.components_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL_LIST));
        adapter.getPositionClicks()
                .subscribe(new Observer<Component>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(GlobalEntities.COMPONENTS_FRAGMENT, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Component component) {

                        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "onNext: " +component.getName());


                        //making control component request
                        Board board = new Board();
                        board.setId(board_id);

                        request = Request.getInstance();
                        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
                        request.setComponent(component);
                        request.setBoard(board);

                        mPresenter.sendComponentControl(request);

                    }
                });

        recyclerView.setAdapter(adapter);
        
        //init floating action button
        fab = (FloatingActionButton) getView().findViewById(R.id.components_fragment_fab);

        fab.setOnClickListener(this);

        fab.setVisibility(View.INVISIBLE);

        //init presenter
        mPresenter = new ComponentsPresenter(getContext(), DataManager.getInstance());
        mPresenter.attachView(this);

        //making get components request
        Board board = new Board();
        board.setId(board_id);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setBoard(board);

        mPresenter.getComponents(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null){
            board_id = getArguments().getString(GlobalEntities.BOARD_ID_TAG, "");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_components, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnComponentsFragmentInteractionListener) {
            mListener = (OnComponentsFragmentInteractionListener) context;
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

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);

        //making get components request
        Board board = new Board();
        board.setId(board_id);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setBoard(board);

        mPresenter.getComponents(request);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.components_fragment_fab:
                Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "onClick: fab");
                addComponentsDialog.show();
                break;
        }
    }

    @Override
    public void componentsReqSuccess(ArrayList<Component> components, Board board) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "componentsReqSuccess: "+components.size());

        this.components.clear();
        this.components.addAll(components);

        adapter.setOwner(board.isOwner());
        adapter.setMemberType(board.getMemberType());

        adapter.notifyDataSetChanged();

        if(board.isOwner()){
            fab.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void componentsReqError(String e) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "componentsReqError: "+e);
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);

        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void componentsReqComplete() {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "componentsReqComplete: ");
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void addReqSuccess(Component component) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "addReqSuccess: "+component.getName());

        this.components.add(component);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addReqError(String e) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "addReqError: "+e);

        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendControlReqSuccess(Component component) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "sendControlReqSuccess: "+component.getName());

    }

    @Override
    public void sendControlReqError(String e) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "sendControlReqError: "+e);

        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(String name, String nodes, int type) {
        Log.i(GlobalEntities.COMPONENTS_FRAGMENT, "onResult: ");

        //making get components request
        Board board = new Board();
        board.setId(board_id);
        Component component = new Component();
        component.setName(name);
        component.setNodes(nodes);
        component.setType(type);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setComponent(component);
        request.setBoard(board);

        mPresenter.addComponent(request);
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
    public interface OnComponentsFragmentInteractionListener {
        void onComponentsFragmentInteraction(String action, String message);
    }
}
