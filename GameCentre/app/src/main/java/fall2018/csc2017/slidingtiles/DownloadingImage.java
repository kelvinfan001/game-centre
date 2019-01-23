package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cited from "https://akira-watson.com/android/httpurlconnection-get.html"
 *
 * This class downloads the image from the given URL if possible
 */

public class DownloadingImage extends AsyncTask<String, Void, Bitmap>{
    /**
     * Listener of the task (sees if downloading went well or not)
     */
    private  Listener listener;

    /**
     * Downloads the image in background
     * @param params basically the URL given by the user
     * @return bmp Bitmap of the image given by url
     */
    protected Bitmap doInBackground(String... params) {

        return download(params[0]);
    }

    /**
     * If getting image was successful, calls a successful method
     * @param bmp bitmap by the given address
     */
    protected void onPostExecute(Bitmap bmp) {
        if (listener != null) {
            listener.onSuccess(bmp);
        }
    }

    /**
     * This methods gets the image by the given address, and returns it if successful
     * @param address the url
     * @return bmp the Bitmap of the image by given address
     */
    public static Bitmap download(String address){

        Bitmap bmp = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestProperty("Accept-Language", "jp");
            urlConnection.connect();

            int resp = urlConnection.getResponseCode();

            switch (resp) {
                case HttpURLConnection.HTTP_OK:
                    InputStream is = null;
                    try {
                        is = urlConnection.getInputStream();
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                        System.out.println("It went in");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("debug", "download error");
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bmp;
    }

    /**
     * Set listener for this task of downloading
     * @param listener a listen object
     */
    void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Calls onSuccess
     */
    interface Listener {
        void onSuccess(Bitmap bmp);
    }
}
