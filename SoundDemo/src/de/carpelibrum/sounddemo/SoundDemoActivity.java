package de.carpelibrum.sounddemo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SoundDemoActivity extends Activity implements OnClickListener {
    private Button geraeuschButton;
    private Button musikButton;
    private MediaPlayer mediaplayer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        mediaplayer = MediaPlayer.create(this, R.raw.gong);
        if (mediaplayer != null) {
            mediaplayer.start();
        }
        
        geraeuschButton = (Button) this.findViewById(R.id.button1);
        geraeuschButton.setOnClickListener(this);
        
        musikButton = (Button) this.findViewById(R.id.button2);
        musikButton.setOnClickListener(this);
    }

	@Override
	protected void onStop() {
		mediaplayer.stop();
		super.onStop();
	}


	public void onClick(View v) {
		
		if (mediaplayer == null)
			return;
		
		if(v == geraeuschButton) {
			AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.gong);
			mediaplayer.stop();
			mediaplayer.reset();
			try {
				mediaplayer.setDataSource(fd.getFileDescriptor(),
						                  fd.getStartOffset(),
						                  fd.getLength());
				mediaplayer.prepare();
				mediaplayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if(v == musikButton) {
	        Uri soundUri = Uri.parse(Uri.encode("http://www.carpelibrum.de/test/spacemusic.mp3"));
			mediaplayer.stop();
			mediaplayer.reset();
			try {
				mediaplayer.setDataSource(this, soundUri);
				mediaplayer.prepare();
				mediaplayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}    
	}
}

