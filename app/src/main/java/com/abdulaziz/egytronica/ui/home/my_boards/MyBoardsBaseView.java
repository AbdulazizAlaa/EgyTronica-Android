package com.abdulaziz.egytronica.ui.home.my_boards;

import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 3/7/17.
 */

public interface MyBoardsBaseView extends BaseView {

    void boardsReqSuccess(ArrayList<Board> boards);
    void boardsReqError(String e);
    void boardsReqComplete();

}