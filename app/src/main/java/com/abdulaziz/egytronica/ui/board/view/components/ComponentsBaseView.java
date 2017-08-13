package com.abdulaziz.egytronica.ui.board.view.components;

import com.abdulaziz.egytronica.data.model.Board;
import com.abdulaziz.egytronica.data.model.Component;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 3/9/17.
 */

public interface ComponentsBaseView extends BaseView {

    void componentsReqSuccess(ArrayList<Component> components, Board board);
    void componentsReqError(String e);
    void componentsReqComplete();

    void addReqSuccess(Component component);
    void addReqError(String e);

    void sendControlReqSuccess(Component component);
    void sendControlReqError(String e);
}
