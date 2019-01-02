package motsoeneng.t.bangdetector.bang_detector;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioAmplitudeListener extends Thread {
    MediaRecorder recorder;
    int delay;
    private int delayCount = 0;
    private boolean isRunning;
    private int maxAmplitude = 0;
    private Flashlight flashlight = null;
    private int lastTap = 0;
    private int amplitude;

    AudioAmplitudeListener(MediaRecorder recorder, int delay, int maxAmplitude, Context context) throws AudioAmplitudeListenerException {
        this.recorder = recorder;
        this.delay = delay;
        this.maxAmplitude = maxAmplitude;
        try {
            flashlight = new Flashlight(context);
        } catch (FlashlightException ex) {
            throw new AudioAmplitudeListenerException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void stopMe() {
        isRunning = false;
    }

    public void run() throws IllegalStateException {
        try {
            isRunning = true;
            Log.i(this.getClass().getName(), "Listener running ...");
            do {
                amplitude = recorder.getMaxAmplitude();
                if (amplitude > maxAmplitude) {
                    if (lastTap > (delayCount - 1000)) {
                        flashlight.turnOn();
                    } else {
                        flashlight.turnOff();
                    }
                    lastTap = delayCount;
                    Log.i(this.getClass().getName(), " Listening - Time: " + delayCount + "ms. Amp: " + amplitude + "v.");
                }
                delayCount += delay;
                sleep(delay);
            } while (isRunning);
            Log.i(this.getClass().getName(), " Exiting Listener");
            return;
        } catch (InterruptedException | FlashlightException ex) {
            throw new IllegalStateException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }
}
