package com.abdulaziz.egytronica.data;

import android.content.Context;
import android.util.Log;

import com.abdulaziz.egytronica.data.local.DatabaseHelper;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.model.Response;
import com.abdulaziz.egytronica.data.remote.Service;
import com.abdulaziz.egytronica.utils.GlobalEntities;

import okhttp3.ResponseBody;
import rx.Observable;


/**
 * Created by abdulaziz on 10/24/16.
 */
public class DataManager {

    private static DataManager dataManager;

    private final Service mService;
    private final DatabaseHelper mDatabaseHepler;
    private final PreferencesHelper mPreferencesHelper;
    private final Context mContext;

    private DataManager(Context mContext, Service mService, DatabaseHelper mDatabaseHepler, PreferencesHelper mPreferencesHelper) {
        Log.i(GlobalEntities.DATA_MANAGER_TAG, "DataManager: created");
        this.mService = mService;
        this.mDatabaseHepler = mDatabaseHepler;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mContext = mContext;
    }

    public static DataManager getInstance(Context mContext, Service mService, DatabaseHelper mDatabaseHepler, PreferencesHelper mPreferencesHelper){
        if(dataManager == null){
            dataManager = new DataManager(mContext, mService, mDatabaseHepler, mPreferencesHelper);
        }
        return dataManager;
    }

    public static DataManager getInstance() {
        if(dataManager != null){
            return dataManager;
        }

        throw new IllegalArgumentException("Should use getInstance(Context mContext, Service mService, DatabaseHelper mDatabaseHepler, PreferencesHelper mPreferencesHelper, SocialNetworks socialNetworks) at least once before using this method.");
    }

    public static boolean isNull(){ return dataManager == null; }

    public PreferencesHelper getPreferencesHelper(){ return mPreferencesHelper;}

    public Observable<Response> login(Request r){ return mService.login(r);}

    public Observable<Response> register(Request r){ return mService.register(r);}

    public Observable<Response> verifyMobile(Request r){ return mService.verifyMobile(r.getToken(), r);}
//    public Observable<Response> verifyMobile(Request r){ return mService.verifyMobile("Bearer "+r.getToken(), r);}

    public Observable<Response> resendPin(Request r){ return mService.resendPin(r.getToken());}
//    public Observable<Response> resendPin(Request r){ return mService.resendPin("Bearer "+r.getToken(), r);}

    public Observable<Response> createBoard(Request r){ return mService.createBoard(r.getToken(), r);}
//    public Observable<Response> createBoard(Request r){ return mService.createBoard("Bearer "+r.getToken(), r);}

    public Observable<Response> getBoards(Request r){ return mService.getBoards(r.getToken());}
//    public Observable<Response> getBoards(Request r){ return mService.getBoards("Bearer "+r.getToken());}

    public Observable<Response> getMemberBoards(Request r){ return mService.getMemberBoards(r.getToken());}
//    public Observable<Response> getMemberBoards(Request r){ return mService.getMemberBoards("Bearer "+r.getToken());}

    public Observable<Response> getBoard(Request r){ return mService.getBoard(r.getBoard().getId(), r.getToken());}
//    public Observable<Response> getBoard(Request r){ return mService.getBoard("Bearer "+r.getToken(), r.getBoard().getId());}

    public Observable<Response> removeMember(Request r){ return mService.removeMember(r.getBoard().getId(), r.getUser().getId(), r.getToken());}
//    public Observable<Response> removeMember(Request r){ return mService.removeMember("Bearer "+r.getToken(), r.getBoard().getId(), r.getUser().getId());}

    public Observable<Response> addMember(Request r){ return mService.addMember(r.getBoard().getId(), r.getToken(), r);}
//    public Observable<Response> addMember(Request r){ return mService.addMember("Bearer "+r.getToken(), r);}

    public Observable<Response> getMembers(Request r){ return mService.getMembers(r.getBoard().getId(), r.getToken());}
//    public Observable<Response> getMembers(Request r){ return mService.getMembers("Bearer "+r.getToken(), r.getBoard().getId());}

    public Observable<Response> addComponent(Request r){ return mService.addComponent(r.getBoard().getId(), r.getToken(), r);}
//    public Observable<Response> addComponent(Request r){ return mService.addComponent(r.getBoard().getId(), "Bearer "+r.getToken(), r);}

    public Observable<Response> getComponents(Request r){ return mService.getComponents(r.getBoard().getId(), r.getToken());}
//    public Observable<Response> getComponents(Request r){ return mService.getComponents("Bearer "+r.getToken(), r.getBoard().getId());}

    public Observable<Response> sendComponentControl(Request r){ return mService.sendComponentControl(r.getBoard().getId(), r.getComponent().getId(), r.getToken(), r);}
//    public Observable<Response> sendComponentControl(Request r){ return mService.sendComponentControl("Bearer "+r.getToken(), r.getBoard().getId(), r.getComponent().getId(), r);}

    public Observable<Response> sendRegistrationId(Request r){ return mService.sendRegistrationId(r.getToken(), r);}
//    public Observable<Response> sendRegistrationId(Request r){ return mService.sendRegistrationId("Bearer "+r.getToken(), r);}

    public Observable<Response> getEvents(){ return mService.getEvents();}

    public Observable<Response> getNewsList(){ return mService.getNewsList();}

    public Observable<Response> getNews(Request r){ return mService.getNews(r.getId());}


}
