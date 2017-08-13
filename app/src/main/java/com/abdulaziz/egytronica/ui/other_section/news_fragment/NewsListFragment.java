package com.abdulaziz.egytronica.ui.other_section.news_fragment;

import android.content.Context;
import android.content.Intent;
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
import com.abdulaziz.egytronica.data.model.News;
import com.abdulaziz.egytronica.ui.news.NewsActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.NewsAdapter;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsListFragment.OnNewsListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsListBaseView{

    private SwipeRefreshLayout refreshLayout;

    private ArrayList<News> newsList;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    private NewsListPresenter presenter;

    private OnNewsListFragmentInteractionListener mListener;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsListFragment.
     */
    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init(){

        //refresh layout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.news_list_refresh);
        refreshLayout.setOnRefreshListener(this);

        //recycler view
        recyclerView = (RecyclerView) getView().findViewById(R.id.news_list_recycler_view);

        newsList = new ArrayList<>();

        adapter = new NewsAdapter(getContext(), newsList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.getPositionClicks().subscribe(new Action1<News>() {
            @Override
            public void call(News news) {
                Log.i(GlobalEntities.NEWS_FRAGMENT, "call: "+news.getTitle());

                Intent i = NewsActivity.getStartIntent(getContext());
                i.putExtra(GlobalEntities.NEWS_ID_TAG, news.getId());
                startActivity(i);

            }
        });


        //presenter
        presenter = new NewsListPresenter(getContext(), DataManager.getInstance());
        presenter.attachView(this);

        refreshLayout.setRefreshing(true);

        presenter.getNewsList();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);

        newsList.clear();
        adapter.notifyDataSetChanged();

        presenter.getNewsList();
    }

    @Override
    public void newsListReqComplete() {
        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "newsListReqComplete: ");
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void newsListReqSuccess(ArrayList<News> newsList) {
        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "newsListReqSuccess: ");
        this.newsList.addAll(newsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void newsListReqError(String e) {
        Log.i(GlobalEntities.NEWS_LIST_PRESENTER_TAG, "newsListReqError: "+e);
        refreshLayout.setRefreshing(false);
        mListener.onNewsListFragmentInteraction(GlobalEntities.ERROR_TAG, e);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsListFragmentInteractionListener) {
            mListener = (OnNewsListFragmentInteractionListener) context;
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
    public interface OnNewsListFragmentInteractionListener {
        void onNewsListFragmentInteraction(String action, String message);
    }
}
