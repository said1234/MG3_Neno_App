package de.carpelibrum.multimedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MultiMediaActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {
	
	private boolean soundPoolBereit;
	
	private SoundPool soundPool;
	private int explosionID;
    private VideoView videoView;
    private ImageView imageView;
    
    private Bitmap bitmap; 
	private final String VIDEO_DATEI = "video.mp4";
	private MediaPlayer audioPlayer;


    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button) this.findViewById(R.id.button1);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.button2);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.button3);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.button4);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.button5);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.button6);
        button.setOnClickListener(this);


        
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		
		// nur ab 2.2 
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				soundPoolBereit = true;
				Log.d("carpelibrum", "geladen: soundID " + sampleId);
			}
		});
		
	
		
		explosionID = soundPool.load(this, R.raw.explosion, 1);
		
		// anzuzeigendes Bild vorbereiten
		bitmap    = BitmapFactory.decodeResource(getResources(), R.drawable.abstrakte_kunst);		
		imageView = (ImageView) this.findViewById(R.id.imageView1);
		imageView.setImageBitmap(bitmap);
		imageView.setVisibility(View.GONE); // zunächst unsichtbar

		// Videowiedergabe vorbereiten
		try {
		  Uri videoUri = videoDateiBereitstellen(); 
		  videoView = (VideoView) findViewById(R.id.videoView1); 
          videoView.setVisibility(View.GONE); // zunächst unsichtbar
		
		  videoView.setVideoURI(videoUri);
		  videoView.setKeepScreenOn(true);

		  MediaController mc = new MediaController(this);
		  videoView.setMediaController(mc);
		  mc.setMediaPlayer(videoView);
		}
		catch(Exception ex) {
			Log.d("carpelibrum", ex.getMessage());
		}

		

	    
		
    }
    
    
    
    

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("carpelibrum", " camera surface changed");
		
	}





	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("carpelibrum", " camera surface created");
		
	}





	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("carpelibrum", " camera surface destroyed");
		
	}





	@Override
	protected void onStop() {
		soundPool.release();
		super.onStop();
	}



	@Override
	public void onClick(View v) {
	   switch(v.getId()) {
	       case R.id.button1: audioVonDatei();
	                          break;
	       case R.id.button2: videoAbspielen();
	                          break; 
	       case R.id.button3: piepen();
	                          break; 
	       case R.id.button4: tonAusgeben();
	                          break; 
	       case R.id.button5: ausgabeMitSoundPool();
	                          break;
	       case R.id.button6: bildAnzeigen();
	                          break;


	   }
		
	}
	
	




	private void bildAnzeigen() {
		imageView.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.GONE);
	}




	private void ausgabeMitSoundPool() {
		
		if(soundPoolBereit) {
		   float lautstaerkeLinks  = 1.0f;
		   float lautstaerkeRechts = 1.0f;
		   float geschwindigkeit   = 1.f;
		   int endlosschleife      = 0;
		   soundPool.play(explosionID, lautstaerkeLinks, lautstaerkeRechts, 1, endlosschleife, geschwindigkeit);
		}
		
	}

	private void tonAusgeben() {
		TonAusgabe ton = new TonAusgabe(440, 2000);
		ton.abspielen();
	}

	private void piepen() {
		int streamTyp    = AudioManager.STREAM_SYSTEM;
		int lautstaerke = 100; // in  Prozent
		ToneGenerator tg = new ToneGenerator(streamTyp, lautstaerke); 		
		tg.startTone(ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE);
		tg.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 1000);
		
	}

	private void videoAbspielen() {
		imageView.setVisibility(View.GONE);
		videoView.setVisibility(View.VISIBLE);
		
		if(videoView.isPlaying()) {
			videoView.stopPlayback();
		}

		videoView.start();
		videoView.requestFocus();

	}
	



	

	private void audioVonDatei() {
		if(audioPlayer == null) {
			audioPlayer = MediaPlayer.create(this, R.raw.letsparty_ausschnitt); // Musik: http://www.playinmusic.com/music-loops-demo-songs.html
		    audioPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				Log.d("carpelibrum", "Audiodatei abgespielt");
			}
		});
		}
		
		if(audioPlayer.isPlaying()) {
			audioPlayer.stop();
		}
		
		audioPlayer.start();
	}





   private Uri videoDateiBereitstellen() throws IOException {
	   File sdCard = Environment.getExternalStorageDirectory();

	   File videoDatei = new File(sdCard.getAbsolutePath() + File.separator + VIDEO_DATEI);
	   
	   if( !videoDatei.exists()) {
	      InputStream input       = this.getAssets().open(VIDEO_DATEI);
	      String dateiName        = videoDatei.getAbsolutePath();
	      FileOutputStream output = new FileOutputStream(dateiName);

	      // umkopieren
	      byte[] puffer = new byte[1024];
	      int anzahl;

	      while ((anzahl = input.read(puffer))> 0){
	         output.write(puffer, 0, anzahl);
	      }

	      output.flush();
	      input.close(); 
	      output.close();
	   }
	   
	   Uri uri = Uri.parse("file://" + videoDatei.getAbsolutePath());
	   Log.d("carpelibrum", "Videodatei: " + uri.toString());
	   return uri; 

   }
    
    
	
	
}