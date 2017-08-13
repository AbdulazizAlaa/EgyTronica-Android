package com.abdulaziz.egytronica.ui.board.create;

import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 3/4/17.
 */

public interface CreateBoardBaseView extends BaseView {

    void createBoardReqSuccess(Board board);
    void createBoardReqError(String e);
    void createBoardReqComplete();

}
