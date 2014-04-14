package com.transcription;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;

import static edu.cmu.pocketsphinx.Assets.syncAssets;

/**
 * Class that will take a given audio file and transcribe it into text.
 *
 * Created by chwebb on 4/14/14.
 */
public class SphinxTranscriptionService extends IntentService {

    private static final String ASSET_LIST_NAME = "assets.lst";

    public SphinxTranscriptionService() {
        super("name");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        System.loadLibrary("pocketsphinx_jni");

        // Move Assets to the SD Card for absolute path
        File appDir = null;
        try {
            appDir = loadAssets(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set Up configuration to match expected audio. The model loaded for the audio must
        // match the sample rate of the configuration (i.e. 8k sample rate to 8k model)
        Config config = Decoder.defaultConfig();
        config.setString("-hmm",appDir + "models/hmm/en-us");   // Full generic English model
        config.setString("-dict", appDir + "models/lm/cmu07a.dic");
        config.setString("-lm", appDir + "models/grammar/digits.gram");
        config.setFloat("-samprate", 8000);
        config.setInt("-maxhmmpf", 2000);
        config.setInt("-maxwpf", 10);
        config.setInt("-pl_window", 2);
        config.setBoolean("-backtrace", true);
        config.setBoolean("-bestpath", false);

        //Add configuration to a new Decoder
        Decoder decoder = new Decoder(config);

        // Open WAV File
        File file = new File(appDir, "models/test/road01.wav");

        //Generate Filestream
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[8000];
        assert in != null;
        int readSize = 0;
        try {
            readSize = in.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        short[] shortArr = new short[readSize/2];
        for (int i = 0; i < readSize/2 ; i++) {
            shortArr[i] = ( (short)( ( buf[i*2] & 0xff )|( buf[i*2 + 1] << 8 ) ) );
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        decoder.startUtt(null);
        decoder.processRaw(shortArr, shortArr.length, false, false);
        decoder.endUtt();
        Hypothesis hypothesis = decoder.hyp();
        String text = hypothesis.getHypstr();
        int score = hypothesis.getBestScore();
    }

    /**
     * Function to load all of the assets in the assets folder to the external SD card to save on
     * internal space and allow for direct absolute paths for the sphinx required files.
     *
     * @param context
     * @return
     * @throws IOException
     */
    private File loadAssets(Context context) throws IOException {
        AssetManager assets = context.getAssets();
        Reader reader = new InputStreamReader(assets.open(ASSET_LIST_NAME));
        BufferedReader br = new BufferedReader(reader);
        File appDir = getApplicationDir(context);
        Set<String> assetPaths = new HashSet<String>();
        String path;

        while (null != (path = br.readLine())) {
            File extFile = new File(appDir, path);
            extFile.getParentFile().mkdirs();
            assetPaths.add(extFile.getPath());

            //Log.i(TAG, "copy " + path + " to " + extFile);
            copyStream(assets.open(path), new FileOutputStream(extFile));
        }

        //removeUnusedAssets(new File(appDir, ASSET_LIST_NAME), assetPaths);

        return appDir;

    }

    /**
     * Returns external files directory for the application. Returns path to
     * directory on external storage which is guaranteed to be unique for the
     * running application.
     *
     * @param context application context
     * @return path to application directory or null if it does not exists
     * @throws IOException if the directory does not exist
     *
     * @see android.content.Context#getExternalFilesDir
     * @see android.os.Environment#getExternalStorageState
     */
    public static File getApplicationDir(Context context) throws IOException {
        File dir = context.getExternalFilesDir(null);
        if (null == dir)
            throw new IOException("cannot get external files dir, " +
                    "external storage state is not accessible");
        return dir;
    }


    /**
     * Copies raw asset resources to external storage of the device. Copies
     * raw asset resources to external storage of the device. Implementation
     * is borrowed from Apache Commons.
     *
     * @param source source stream
     * @param dest   destination stream
     * @throws IOException if an I/O error occurs
     */
    private static void copyStream(InputStream source, OutputStream dest)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int nread;

        while ((nread = source.read(buffer)) != -1) {
            if (nread == 0) {
                nread = source.read();
                if (nread < 0)
                    break;

                dest.write(nread);
                continue;
            }

            dest.write(buffer, 0, nread);
        }
    }
}
