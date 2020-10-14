package Dictionary;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineEvent;

import java.io.File;
import java.io.IOException;

class AudioListener implements LineListener {
    private boolean done = false;
    @Override public synchronized void update(LineEvent event) {
        Type eventType = event.getType();
        if (eventType == Type.STOP || eventType == Type.CLOSE) {
            done = true;
            notifyAll();
        }
    }
    public synchronized void waitUntilDone() throws InterruptedException {
        while (!done) { wait(); }
    }
}

public class SoundClip{

    private static Clip clip;
    private static AudioInputStream audioInputStream;
    private static AudioListener listener;
    private static String filePath;

    public SoundClip() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        listener = new AudioListener();
        clip = AudioSystem.getClip();
        clip.addLineListener(listener);
        clip.open(audioInputStream);
    }

    public static void playSound(String src) throws IOException{
        String cd = System.getProperty("user.dir") + "/src/main/resources/sound/";
        src = cd + src;
        filePath = src;
        try {
            SoundClip audioPlayer = new SoundClip();
            clip.start();
            listener.waitUntilDone();
            clip.close();
            audioInputStream.close();
        } catch (Exception ex) {
            System.out.println("File not found.");
        }
    }

} 