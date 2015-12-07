package controller;

import model.*;
import view.PhasePanel;

/**
 * Created by baptiste on 06/12/15.
 */
public class PhaseController {

    private PhasePanel view;
    private BluetoothManager model;
    public PhaseController(PhasePanel v, BluetoothManager m){
        view = v;
        model = m;
        model.phaseController = this;
    }

    public void changePhase(Phases p){
        switch (p){
            case Teleop:
                view.loadTeleoperation();
                break;
            case FollowL:
                view.loadFollowingLine();
                break;
            case End:
                view.loadEnd();
                break;
        }
    }
}
