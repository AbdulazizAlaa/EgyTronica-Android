package com.abdulaziz.egytronica.ui.board.view.details;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.model.User;
import com.abdulaziz.egytronica.utils.DividerItemDecoration;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.MembersAdapter;
import com.abdulaziz.egytronica.utils.Utils;
import com.abdulaziz.egytronica.utils.dialogs.AddMembersDialog;

import java.util.ArrayList;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener, DetailsBaseView, AddMembersDialog.MembersDialogInteractionInterface{

    private ProgressDialog progress;
    private View main_view;
    AddMembersDialog addMembersDialog;

    private TextView boardNameTV;
    private TextView adviceTv;

    private TextView statusTV;
    private TextView outputEfficiencyTV;
    private TextView tempTV;
    private TextView humidityTV;
    private TextView runTimeTV;
    private TextView refreshTimeTV;
    private TextView maintenanceDateTv;

    private TextView engAddTV;
    private TextView techAddTV;

    private ImageView statusIV;

    private CardView engCardView;
    private CardView techCardView;

    private FloatingActionButton fab;

    private DividerItemDecoration divider;

    private ArrayList<User> engineers;
    private RecyclerView engineersRecyclerView;
    private MembersAdapter engineersAdapter;
    private LinearLayoutManager engineersLayoutManager;

    private ArrayList<User> technicians;
    private RecyclerView techniciansRecyclerView;
    private MembersAdapter techniciansAdapter;
    private LinearLayoutManager techniciansLayoutManager;

    private Request request;
    private Board board;
    private String board_id;

    private DetailsPresenter mPresenter;

    private OnDetailsFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(String board_id) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalEntities.BOARD_ID_TAG, board_id);

        DetailsFragment fragment = new DetailsFragment();
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

        //init presenter
        mPresenter = new DetailsPresenter(getContext(), DataManager.getInstance());
        mPresenter.attachView(this);

        //requesting board details
        Board tempBoard = new Board();
        tempBoard.setId(board_id);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setBoard(tempBoard);

        mPresenter.getBoard(request);

        //progress Dialog
        progress = new ProgressDialog(getContext());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setTitle("Please Wait..");

        //init add member dialog
        addMembersDialog = new AddMembersDialog(getContext(), 0);
        addMembersDialog.setListener(this);

        //main view for snackbar
        main_view = getView().findViewById(R.id.main_view);

        //init layout text views
        boardNameTV = (TextView) getView().findViewById(R.id.details_fragment_board_name_tv);
        adviceTv = (TextView) getView().findViewById(R.id.details_fragment_advice_tv);

        statusTV = (TextView) getView().findViewById(R.id.details_fragment_board_status_tv);
        outputEfficiencyTV = (TextView) getView().findViewById(R.id.details_fragment_board_out_efficiency_tv);
        tempTV = (TextView) getView().findViewById(R.id.details_fragment_board_temp_tv);
        humidityTV = (TextView) getView().findViewById(R.id.details_fragment_board_humidity_tv);
        runTimeTV = (TextView) getView().findViewById(R.id.details_fragment_board_running_time_tv);
        refreshTimeTV = (TextView) getView().findViewById(R.id.details_fragment_board_refresh_time_tv);
        maintenanceDateTv = (TextView) getView().findViewById(R.id.details_fragment_board_maintainance_date_tv);

        engAddTV = (TextView) getView().findViewById(R.id.details_fragment_members_eng_add_tv);
        techAddTV = (TextView) getView().findViewById(R.id.details_fragment_members_tech_add_tv);

        //init card view container for the eng and tech
        engCardView = (CardView) getView().findViewById(R.id.details_fragment_members_eng_card_view);
        techCardView = (CardView) getView().findViewById(R.id.details_fragment_members_tech_card_view);

        //init status image view
        statusIV = (ImageView) getView().findViewById(R.id.details_fragment_status_iv);

        //click listeners
        engAddTV.setOnClickListener(this);
        techAddTV.setOnClickListener(this);

        //init floating action button
        fab = (FloatingActionButton) getView().findViewById(R.id.details_fragment_fab);

        fab.setOnClickListener(this);

        //make manager control invisible by default
        engCardView.setVisibility(View.INVISIBLE);
        techCardView.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);

        //observer for handling members remove
        Observer observer = new Observer<User>() {
            @Override
            public void onCompleted() {
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onError: "+e.getMessage());

            }

            @Override
            public void onNext(User user) {
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onNext: "+user.getName());

                handleProgress("Removing...");

                User tempUser = new User();
                tempUser.setId(user.getId());
                Board tempBoard = new Board();
                tempBoard.setId(board_id);

                request = Request.getInstance();
                request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
                request.setUser(tempUser);
                request.setBoard(tempBoard);

                mPresenter.removeMember(request);

            }
        };

        //item divider
        divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        //init engineers recycler view
        engineers = new ArrayList<User>();

        engineersLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        engineersAdapter = new MembersAdapter(getContext(), engineers);
        engineersAdapter.getPositionClicks().subscribe(observer);

        engineersRecyclerView = (RecyclerView) getView().findViewById(R.id.details_fragment_members_eng_recycler_view);
        engineersRecyclerView.addItemDecoration(divider);
        engineersRecyclerView.setLayoutManager(engineersLayoutManager);
        engineersRecyclerView.setAdapter(engineersAdapter);

        //init technicians recycler view
        technicians = new ArrayList<User>();

        techniciansLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        techniciansAdapter = new MembersAdapter(getContext(), technicians);
        techniciansAdapter.getPositionClicks().subscribe(observer);

        techniciansRecyclerView = (RecyclerView) getView().findViewById(R.id.details_fragment_members_tech_recycler_view);
        techniciansRecyclerView.addItemDecoration(divider);
        techniciansRecyclerView.setLayoutManager(techniciansLayoutManager);
        techniciansRecyclerView.setAdapter(techniciansAdapter);

    }

    private void fillDetails(Board board){
        boardNameTV.setText(board.getName());
        adviceTv.setText(board.getAdvice());

        statusTV.setText(board.getStatus());
        outputEfficiencyTV.setText(board.getOutputEfficiency());
        tempTV.setText(board.getTemp());
        humidityTV.setText(board.getHumidity());
        refreshTimeTV.setText(board.getRefreshTime());
        maintenanceDateTv.setText(board.getLastMaintainance());

        String[] runTimeArray = board.getRunTime().split(",");
        if(runTimeArray.length == 1){
            runTimeTV.setText(runTimeArray[0]+" Hours 0 Minutes");
        }else if(runTimeArray.length == 2){
            runTimeTV.setText(runTimeArray[0]+" Hours "+runTimeArray[1]+" Minutes");
        }

        switch (board.getColorCode()){
            case 1:
                statusIV.setImageResource(R.drawable.board_status_shape_green);
                break;
            case 2:
                statusIV.setImageResource(R.drawable.board_status_shape_gray);
                break;
            case 3:
                statusIV.setImageResource(R.drawable.board_status_shape_orange);
                break;
            case 4:
                statusIV.setImageResource(R.drawable.board_status_shape_red);
                break;
            default:
                statusIV.setImageResource(R.drawable.board_status_shape_green);
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null){
            board_id = getArguments().getString(GlobalEntities.BOARD_ID_TAG, "");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsFragmentInteractionListener) {
            mListener = (OnDetailsFragmentInteractionListener) context;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.details_fragment_members_eng_add_tv:
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onClick: Add Engineer");
                addMembersDialog.setType(0);
                addMembersDialog.show();
                break;
            case R.id.details_fragment_members_tech_add_tv:
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onClick: Add Technician");
                addMembersDialog.setType(1);
                addMembersDialog.show();
                break;
            case R.id.details_fragment_fab:
                Log.i(GlobalEntities.DETAILS_FRAGMENT, "onClick: Add");
                addMembersDialog.setType(0);
                addMembersDialog.show();
                break;
        }
    }

    @Override
    public void boardReqSuccess(Board board) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "boardReqSuccess: "+board.getName());
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "boardReqSuccess: "+board.isOwner());

        this.board = board;
        fillDetails(this.board);

        if(board.isOwner()){
            engCardView.setVisibility(View.VISIBLE);
            techCardView.setVisibility(View.VISIBLE);

            fab.setVisibility(View.VISIBLE);

            //requesting board details
            Board tempBoard = new Board();
            tempBoard.setId(board_id);

            request = Request.getInstance();
            request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
            request.setBoard(tempBoard);

            mPresenter.getMembers(request);
        }


    }

    @Override
    public void boardReqError(String e) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "boardReqError: "+e);
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();
        handleProgress("");
    }

    @Override
    public void boardReqComplete() {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "boardReqComplete: ");
    }

    @Override
    public void membersReqSuccess(ArrayList<User> users) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "membersReqSuccess: "+users.size());
        engineers.clear();
        technicians.clear();
        for(User u : users){
            if(u.getPivot().getType() == 0){
                engineers.add(u);
            }else if(u.getPivot().getType() == 1){
                technicians.add(u);
            }
        }
        engineersAdapter.notifyDataSetChanged();
        techniciansAdapter.notifyDataSetChanged();
    }

    @Override
    public void membersReqError(String e) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "membersReqError: "+e);

        Utils.logout(getContext(), e);
    }

    @Override
    public void membersReqComplete() {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "membersReqComplete: ");

    }

    @Override
    public void addReqSuccess(User user) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "addReqSuccess: "+user.getName());
        handleProgress("");
        if(user.getPivot().getType() == 0){
            engineers.add(user);
            engineersAdapter.notifyDataSetChanged();
        }else if(user.getPivot().getType() == 1){
            technicians.add(user);
            techniciansAdapter.notifyDataSetChanged();
        }

        //updating members records
        Board tempBoard = new Board();
        tempBoard.setId(board_id);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setBoard(tempBoard);

        mPresenter.getMembers(request);
    }

    @Override
    public void addReqError(String e) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "addReqError: "+e);
        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();

        //if error message contains any data about invalid token
        Utils.logout(getContext(), e);
    }

    @Override
    public void removeReqSuccess() {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "removeReqSuccess: ");
        handleProgress("");

        //updating members records
        Board tempBoard = new Board();
        tempBoard.setId(board_id);

        request = Request.getInstance();
        request.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.Key.AUTH_TOKEN, ""));
        request.setBoard(tempBoard);

        mPresenter.getMembers(request);
    }

    @Override
    public void removeReqError(String e) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "removeReqError: "+e);
        handleProgress("");
        Snackbar.make(main_view, e, Snackbar.LENGTH_SHORT).show();

        //if error message contains any data about invalid token
        Utils.logout(getContext(), e);
    }

    @Override
    public void onResult(String email, int type) {
        Log.i(GlobalEntities.DETAILS_FRAGMENT, "onResult: "+email);
        handleProgress("Adding Contributor...");

        User user = new User();
        user.setEmail(email);
        user.setType(type);
        Board board = new Board();
        board.setId(board_id);

        request = Request.getInstance();
        request.setUser(user);
        request.setBoard(board);

        mPresenter.addMember(request);
    }

    private void handleProgress(String message){
        if(progress.isShowing()){
            Log.i(GlobalEntities.DETAILS_FRAGMENT, "handleProgress: showing");
            progress.dismiss();
        }else if(!message.equals("")){
            Log.i(GlobalEntities.DETAILS_FRAGMENT, "handleProgress: not showing");
            progress.setMessage(message);
            progress.show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for  more information.
     */
    public interface OnDetailsFragmentInteractionListener {
        void onDetailsFragmentInteraction(String action, String message);
    }
}
