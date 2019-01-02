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
        try {
            flashlight = new Flashlight(context);
        } catch (FlashlightException ex) {
            throw new AudioAmplitudeListenerException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    private int calculateDecibel(byte[] buffer){
        int sum = 0;
        for(int i = 0; i < bufferSize; i++){
            sum += Math.abs(buffer[i]);
        }
        return  sum / bufferSize;
    }

    public void stopMe() {
        isRunning = false;
    }

    public void run() throws IllegalStateException {
        try {
            int decibel;
            bufferSize = 512;
            byte[] buffer = new byte[bufferSize];
            isRunning = true;
            Log.i(this.getClass().getName(), "Listener running ...");
            do {
                audioRecord.read(buffer, 0, bufferSize);
                decibel = calculateDecibel(buffer);
                Log.i(getClass().getName(), "Audio read: Db = " + decibel);
                delayCount += delay;
                sleep(delay);
            } while (isRunning);
            Log.i(this.getClass().getName(), " Exiting Listener");
            return;
        } catch (InterruptedException ex) {
            throw new IllegalStateException(this.getClass().getName() + ": " + ex.getMessage());
        }
    }
}
