package de.carpelibrum.multimedia;



import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class TonAusgabe {

	    private final int SAMPLE_RATE = 8;  // samples pro millisekunde 
	    private final double PI_2 = 2 * Math.PI; 
	    private int anzahlSamples;
        private byte[] daten;
	    private AudioTrack audioTrack;
	    
	    
	/**
	 * Konstruktor
	 * @param frequenz in Hertz
	 * @param millis: Dauer in Millisekunden 
	 */

	public TonAusgabe(int frequenz, int millis) {
		init(frequenz, millis); 
	}
	
	
	/**
	 * Initialisierung 
	 * @param frequenz : Tonfrequenz in Hertz
	 * @param dauer_in_millis: Dauer in Millisekunden 
	 */
	private void init(int frequenz, int dauer_in_millis) {
		 anzahlSamples     = SAMPLE_RATE * dauer_in_millis;
		 daten           = new byte[2*anzahlSamples];
		 double d          = (double) (1000 * SAMPLE_RATE * 1.0 / frequenz);
		 
		 int pos = 0;
		 
		 // als WAVE Format 16 Bit PCM erzeugen
		 for(int i = 0; i < anzahlSamples; i++) {
			 short wert      = (short) (32767 * Math.sin(PI_2 * i / d));
			 daten[pos++]  = (byte) (wert & 0x00ff);
			 daten[pos++]  = (byte) ((wert & 0xff00) >>> 8);
		 }
		 

	}
	
	
	/**
	 * Ton abspielen
	 */
	public void abspielen() {
		if(audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
		}
		
	     audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                 1000 * SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                 AudioFormat.ENCODING_PCM_16BIT, daten.length,
                 AudioTrack.MODE_STATIC);
         audioTrack.write(daten, 0, daten.length);
		
        audioTrack.play();
	}

		

}
