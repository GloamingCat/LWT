package lwt;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.*;

import javax.sound.sampled.*;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;

public class LSoundPlayer {

	private static ArrayList<LClip> sfx = new ArrayList<>();
	private static LClip bgm = null;

	private interface LClip {
		public void play() throws Exception;
		public void setVolume(float v);
		public void setPitch(float v);
		public void stop();
		public boolean isPlaying();
	}

	private static LClip playAudio(String path, float volume, float pitch, boolean loop) {
		LClip clip = null;
		System.out.println(path + " " + volume + " " + pitch);
		try {
			File file = new File(path);
			try {
				clip = new JavaxClip(loop ? null : sfx, file);
				clip.setPitch(pitch);
				clip.setVolume(volume);
				clip.play();
			} catch (Exception e) {
				e.printStackTrace();
				clip = new G3PClip(loop ? null : sfx, file);
				clip.setPitch(pitch);
				clip.setVolume(volume);
				clip.play();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			clip = null;
		}
		return clip;
	}

	public static void playBGM(String path, float volume, float pitch) {
		if (path == null || path.isEmpty())
			return;
		if (bgm != null) {
			bgm.stop();
		}
		var clip = playAudio(path, volume, pitch, true);
		bgm = clip;
	}

	public static void playSFX(String path, float volume, float pitch) {
		var clip = playAudio(path, volume, pitch, false);
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
		for (var clip : sfx) {
			clip.stop();
		}
		sfx.clear();
		if (bgm != null) {
			bgm.stop();
		}
		bgm = null;
	}

	public static void refresh(float volume, float pitch) {
		if (bgm != null) {
			bgm.setVolume(volume);
			bgm.setPitch(pitch);
		}
		for (var clip : sfx) {
			clip.setVolume(volume);
			clip.setPitch(pitch);
		}
	}

	private static class JavaxClip implements LClip {

		private Clip clip;
		private boolean stopped = false;
		private float pitch;
		private AudioInputStream baseAis;

		public JavaxClip (ArrayList<LClip> sfx, File file) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			baseAis = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			final LClip self = this;
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP && !stopped) {
						if (sfx == null)
							replay();
						else
							sfx.remove(self);
					}
				}
			});
		}
		
		private void open(float sampleRate) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			AudioFormat baseFormat = baseAis.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate() * sampleRate,
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate() * sampleRate,
					baseFormat.isBigEndian());
			AudioInputStream ais = AudioSystem.getAudioInputStream(decodedFormat, baseAis);
			if (clip.isOpen())
				clip.close();
			clip.open(ais);
		}
		
		protected void replay() {
			try {
				play();
				System.out.println("javax replay");
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
		public void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			try {
				open(pitch);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				open(1);
			}
			clip.start();
		}

		public void setPitch(float v) {
			pitch = v;
		}

		public void setVolume(float v) {
			FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float g = (float) (20.0 * Math.log10(v));
			control.setValue(g);
			//FloatControl volumeControl = (FloatControl) mixer.getControl(FloatControl.Type.VOLUME);
			//volumeControl.setValue(v);
		}

		public void stop() {
			stopped = true;
			clip.stop();
			clip.close();
		}

		public boolean isPlaying() {
			return clip.isRunning();
		}

	}
	
	private static Logger quietLogger = Logger.getLogger(StreamPlayer.class.getName());
	static {
		quietLogger.setLevel(Level.SEVERE);
	}

	private static class G3PClip extends StreamPlayer implements LClip {

		private File file;

		public G3PClip(ArrayList<LClip> sfx, File file) throws StreamPlayerException {
			super(quietLogger);
			this.file = file;
			G3PClip self = this;
			addStreamPlayerListener(new StreamPlayerListener() {
				@Override
				public void opened(Object dataSource, Map<String, Object> prop) {}

				@Override
				public void progress(int nEncodedBytes, long microsecondPosition, 
						byte[] pcmData, Map<String, Object> prop) {	}
				@Override
				public void statusUpdated(StreamPlayerEvent event) {
					if (event.getPlayerStatus() == Status.EOM) {
						if (sfx == null) {
							self.replay();
						} else {
							sfx.remove(self);
						}
					}
				}
			});
		}
		
		public void replay() {
			try {
				super.play();
				seekTo(0);
				System.out.println("replay");
			} catch (StreamPlayerException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void play() throws StreamPlayerException {
			try {
				open(file);
				super.play();
			} catch(Exception e) {
				setSpeedFactor(1);
				open(file);
				super.play();
			}
		}

		@Override
		public void setPitch(float v) {
			setSpeedFactor(v);
		}

		@Override
		public void setVolume(float v) {
			double g = 20 * Math.log10(v);
			setLogScaleGain(Math.min(Math.max(getMinimumGain(), g), getMaximumGain()));
		}

	}

}
