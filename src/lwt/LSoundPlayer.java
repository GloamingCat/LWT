package lwt;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;

public class LSoundPlayer {
	
	static {
	    //Disable loggers               
		Logger l = Logger.getLogger("org.jaudiotagger");
		l.setLevel(Level.OFF);
	}
	
	private static class LSoundClip extends StreamPlayer implements StreamPlayerListener {
		
		private boolean loop;
		
		public LSoundClip(String path, float volume, float pitch, boolean loop) throws StreamPlayerException {
			addStreamPlayerListener(this);
			this.loop = loop;
			open(new File(path));
			setGain(volume);
			setSpeedFactor(pitch);
			play();
		}

		@Override
		public void opened(Object arg0, Map<String, Object> arg1) {
		}

		@Override
		public void progress(int arg0, long arg1, byte[] arg2, Map<String, Object> arg3) {}

		@Override
		public void statusUpdated(StreamPlayerEvent arg0) {
			if (loop && arg0.getPlayerStatus() == Status.EOM) {
				try {
					this.play();
				} catch (StreamPlayerException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static ArrayList<StreamPlayer> sfx = new ArrayList<>();
	private static StreamPlayer bgm = null;
	
	public static StreamPlayer playAudio(String path, float volume, float pitch, boolean loop) {
		try {
			LSoundClip clip = new LSoundClip(path, volume, pitch, loop);
			return clip;
		} catch (StreamPlayerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void playBGM(String path, float volume, float pitch) {
		StreamPlayer clip = playAudio(path, volume, pitch, true);
		if (bgm != null) {
			bgm.stop();
		}
		bgm = clip;
	}
	
	public static void playSFX(String path, float volume, float pitch) {
		StreamPlayer clip = playAudio(path, volume, pitch, false);
		if (clip == null)
			return;
		for (int i = 0; i < sfx.size(); i++) {
			if (sfx.get(i).isPlaying())
				continue;
			sfx.set(i, clip);
			return;
		}
		sfx.add(clip);
	}
	
	public static void stop() {
		for (StreamPlayer clip : sfx) {
			clip.stop();
		}
		sfx.clear();
		if (bgm != null) {
			bgm.stop();
		}
		bgm = null;
	}
	
	/*
	private static ArrayList<Clip> sfx = new ArrayList<>();
	private static Clip bgm = null;
	
	private static Clip playAudio(String path, float volume, float pitch, boolean loop) {
		try {
			System.out.println(path);
			BufferedInputStream file = new BufferedInputStream(new FileInputStream(path));
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
			
			// Change Pitch
			{
				AudioFormat format = inputStream.getFormat();
				
				int bits = format.getSampleSizeInBits();
				if (format.getEncoding() == AudioFormat.Encoding.ULAW || format.getEncoding() == AudioFormat.Encoding.ALAW || bits != 8)
					bits = 16;
				int channels = format.getChannels();
				float rate = format.getSampleRate();
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate * pitch, 
						bits, channels, bits / 8 * channels, rate, format.isBigEndian());
				inputStream = AudioSystem.getAudioInputStream(format, inputStream);
			}
			
			Clip clip = AudioSystem.getClip();
			clip.open(inputStream);
			
			// Play
			if (loop)
				clip.setLoopPoints(0, -1);
			clip.start(); 
			
			// set volume
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume / 5) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
			
			// Set volume and pitch
			//FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
			//volumeControl.setValue(volume);
			//FloatControl pitchControl = (FloatControl) clip.getControl(FloatControl.Type.SAMPLE_RATE);
			//pitchControl.setValue(pitch);
			
			return clip;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	*/
}
