package com.abdulaziz.egytronica.ui.other_section.events_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.DataManager;
import com.abdulaziz.egytronica.data.model.Event;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.EventsAdapter;

import java.util.ArrayList;
import java.util.Locale;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsListFragment.OnEventsListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, EventsBaseView{

    private SwipeRefreshLayout refreshLayout;

    private ArrayList<Event> eventsList;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;

    private EventsPresenter presenter;

    private OnEventsListFragmentInteractionListener mListener;

    public EventsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventsListFragment.
     */
    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
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
        return inflater.inflate(R.layout.fragment_events_list, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init(){

        //refresh layout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.events_list_refresh);
        refreshLayout.setOnRefreshListener(this);

        //recycler view
        recyclerView = (RecyclerView) getView().findViewById(R.id.events_list_recycler_view);

        eventsList = new ArrayList<>();

        adapter = new EventsAdapter(getContext(), eventsList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.getPositionClicks().subscribe(new Action1<Event>() {
            @Override
            public void call(Event event) {
                Log.i(GlobalEntities.EVENTS_FRAGMENT, "call: "+event.getTitle());

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)", event.getLat(), event.getLng(), event.getLat(), event.getLng(), event.getTitle());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        //presenter
        presenter = new EventsPresenter(getContext(), DataManager.getInstance());
        presenter.attachView(this);

        refreshLayout.setRefreshing(true);

        presenter.getEvents();

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);

        eventsList.clear();
        adapter.notifyDataSetChanged();

        presenter.getEvents();
    }

    @Override
    public void eventsListReqComplete() {
        Log.i(GlobalEntities.EVENTS_PRESENTER_TAG, "eventsListReqComplete: ");
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void eventsListReqSuccess(ArrayList<Event> eventsList) {
        Log.i(GlobalEntities.EVENTS_PRESENTER_TAG, "eventsListReqSuccess: ");
        this.eventsList.addAll(eventsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eventsListReqError(String e) {
        Log.i(GlobalEntities.EVENTS_PRESENTER_TAG, "eventsListReqError: "+e);
        refreshLayout.setRefreshing(false);
        mListener.onEventsListFragmentInteraction(GlobalEntities.ERROR_TAG, e);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventsListFragmentInteractionListener) {
            mListener = (OnEventsListFragmentInteractionListener) context;
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
    public interface OnEventsListFragmentInteractionListener {
        void onEventsListFragmentInteraction(String action, String message);
    }
}
