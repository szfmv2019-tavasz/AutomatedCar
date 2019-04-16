package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.model.objects.NpcCar;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.VirtualFunctionBus;

import java.util.List;

public class Tempomat extends SystemComponent {
    boolean active;
    int speedLimit;
    int manualLimit;
    NpcCar target;

    public Tempomat(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {
        setSpeedLimit();
        keepSpeed();
    }

    private void setTarget(){
        //TODO
        getObjectsFromRadarSensor();
    }

    private List<WorldObject> getObjectsFromRadarSensor() {
        //TODO
        return null;
    }

    private void setSpeedLimit(){
        //TODO
    }

    private void setManualLimit(){
        //TODO
    }

    private void setActive(){
        //TODO
        setTarget();
    }

    private void keepSpeed(){
        //TODO
        accelerate();
        decelerate();
    }

    private void accelerate(){
        //TODO
    }

    private void decelerate(){
        //TODO
    }
}
