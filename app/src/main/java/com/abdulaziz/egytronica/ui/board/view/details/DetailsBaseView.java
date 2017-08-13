package com.abdulaziz.egytronica.ui.board.view.details;

import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.User;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 3/3/17.
 */

public interface DetailsBaseView extends BaseView {

    void boardReqSuccess(Board board);
    void boardReqError(String e);
    void boardReqComplete();

    void membersReqSuccess(ArrayList<User> users);
    void membersReqError(String e);
    void membersReqComplete();

    void addReqSuccess(User user);
    void addReqError(String e);

    void removeReqSuccess();
    void removeReqError(String e);

}
