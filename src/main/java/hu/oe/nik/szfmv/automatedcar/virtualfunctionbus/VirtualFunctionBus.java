package hu.oe.nik.szfmv.automatedcar.virtualfunctionbus;

import hu.oe.nik.szfmv.automatedcar.model.WorldObject;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.SystemComponent;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyCollisionPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyPowertrainPacket;

import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.camera.ReadOnlyCameraPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyCarPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyPowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlySamplePacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlySteeringPacket;
import hu.oe.nik.szfmv.automatedcar.virtualfunctionbus.packets.ReadOnlyInputPacket;


import java.util.ArrayList;
import java.util.List;

/**
 * This is the class for the Virtual Function Bus. Components are only
 * allowed to collect sensory data exclusively using the VFB. The VFB stores the
 * input and output signals, inputs only have setters, while outputs only have
 * getters respectively.
 */
public class VirtualFunctionBus {

    public ReadOnlySamplePacket samplePacket;
    public ReadOnlySteeringPacket steeringPacket;
    public ReadOnlyCarPacket carPacket;
    public ReadOnlyCameraPacket cameraPacket;

    public ReadOnlyPowertrainPacket powertrainPacket;

    /**
     * The output values of the keyboard input component
     */
    public ReadOnlyInputPacket inputPacket;

    public ReadOnlyCollisionPacket collisionPacket;

    public List<WorldObject> worldObjects = new ArrayList<>();

    private List<SystemComponent> components = new ArrayList<>();


    /**
     * Registers the provided {@link SystemComponent}
     *
     * @param comp a class that implements @{link ISystemComponent}
     */
    public void registerComponent(SystemComponent comp) {
        components.add(comp);
    }

    /**
     * Calls cyclically the registered {@link SystemComponent}s once the virtual function bus has started.
     */
    public void loop() {
        for (SystemComponent comp : components) {
            comp.loop();
        }
    }

}
