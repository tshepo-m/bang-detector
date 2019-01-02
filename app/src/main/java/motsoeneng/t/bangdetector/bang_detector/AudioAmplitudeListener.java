package motsoeneng.t.bangdetector.bang_detector;

import android.content.Context;
import android.media.AudioRecord;
import android.media.audiofx.Visualizer;
import android.util.Log;

public class AudioAmplitudeListener extends Thread {
    AudioRecord audioRecord;
    int delay;
    private int delayCount = 0;
    private boolean isRunning;
    private int maxAmplitude = 0;
    private Flashlight flashlight = null;
    private int lastTap = 0;
    private int bufferSize;

    AudioAmplitudeListener(AudioRecord audioRecord, int delay, int maxAmplitude, Context context, int bufferSize) throws AudioAmplitudeListenerException {
        this.audioRecord = audioRecord;
        this.delay = delay;
        this.maxAmplitude = maxAmplitude;
        this.bufferSize = bufferSize;
        Log.i(this.getClass().getName(), " BUFFER INIT: " + bufferSize);
        try {
            flashlight = new Flashlight(context);
        } catch (FlashlightException ex) {
            throw new AudioAmplitudeListenerException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    private int getAmplitude(short[] buffer, int bytesRead) {
        double sum = 0.0;
        double amplitude;
        for (int i = 0; i < bytesRead; i++) {
            sum += buffer[i] * buffer[i];
        }
        amplitude = Math.abs(sum / bytesRead);
        return (int) amplitude / 10;
    }

    public void stopMe() {
        isRunning = false;
    }

    public void run() throws IllegalStateException {
        try {
            int amplitude;
            int bytesRead;
            short[] buffer = new short[bufferSize];
            isRunning = true;
            Log.i(this.getClass().getName(), "Listener running ...");
            do {
                bytesRead = audioRecord.read(buffer, 0, bufferSize);
                amplitude = getAmplitude(buffer, bytesRead);
                if (amplitude > maxAmplitude) {
                    flashlight.toggle();
                    Log.i(getClass().getName(), "Audio read: Amp = " + amplitude);
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
