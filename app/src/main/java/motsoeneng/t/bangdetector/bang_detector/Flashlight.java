package motsoeneng.t.bangdetector.bang_detector;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class Flashlight {
    private Context context;
    private CameraManager cameraManager;
    private String cameraID = "0";
    private boolean isCameraOn = false;

    public Flashlight(Context context) throws FlashlightException {
        this.context = context;

        cameraManager = context.getSystemService(CameraManager.class);
        try {
            for (String cameraID : cameraManager.getCameraIdList()) {
                if (Integer.parseInt(cameraID) == 0) {
                    this.cameraID = cameraID;
                }
            }
        } catch (CameraAccessException ex) {
            throw new FlashlightException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void toggle() throws FlashlightException {
        try {
            isCameraOn = !isCameraOn;
            cameraManager.setTorchMode(cameraID, isCameraOn);
        } catch (CameraAccessException ex) {
            throw new FlashlightException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void turnOn() throws FlashlightException {
        try {
            isCameraOn = true;
            cameraManager.setTorchMode(cameraID, isCameraOn);
        } catch (CameraAccessException ex) {
            throw new FlashlightException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void turnOff() throws FlashlightException {
        try {
            isCameraOn = false;
            cameraManager.setTorchMode(cameraID, isCameraOn);
        } catch (CameraAccessException ex) {
            throw new FlashlightException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public boolean getState(){
        return isCameraOn;
    }
}
