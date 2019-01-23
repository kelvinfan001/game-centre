package fall2018.csc2017.slidingtiles;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//View Class for downloading images, not tested.
/**
 * Downloading screen of background image
 */
public class DownloadingActivity extends AppCompatActivity {
    /**
     * Downloading Button from the view
     */
    private Button download;
    /**
     * username of the logged in user
     */
    private String username;
    /**
     * URL of the image given by the user on the text field
     */
    private EditText url;
    /**
     * Task that is executed when the download button is clicked
     */
    private DownloadingImage task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_image_activity);
        download = findViewById(R.id.download);
        url = findViewById(R.id.url_field);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resources", android.R.color.white); // the second parameter will be fallback if the preference is not found
        getWindow().setBackgroundDrawableResource(bg);
        addDownloadButtonListener();
    }

    /**
     * Download Button Listener. Runs the DownloadingImage class when clicked
     */
    private void addDownloadButtonListener() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new DownloadingImage();
                task.setListener(createListener());
                task.execute(url.getText().toString());
            }
        });
    }


    /**
     * Add the event that occurs when getting image from URL was successful
     * @return Listener
     */
    private DownloadingImage.Listener createListener() {
        return new DownloadingImage.Listener() {
            @Override
            public void onSuccess(Bitmap bmp) {
                Board.NUM_ROWS = 3;
                Board.NUM_COLS = 3;
                split(bmp);
                GameActivity.image_game = true;
                Intent intent = new Intent(DownloadingActivity.this, StartingActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("complexity", "Image");
                startActivity(intent);

            }
        };
    }

    /**
     * Split the image into pieces depending on the complexity
     * @param bmp Bitmap by the given url
     */
    private void split(Bitmap bmp){
        Bitmap image;
        Bitmap[] image_list;
        // adjust image size into screen size
        image = Bitmap.createScaledBitmap(bmp,1080,1920, false);
        System.out.println("Passed line 28");
        int tile_width = 1080/3;
        int tile_height = 1920/3;
        int x = 0;
        int y = 0;
        int numTiles = Board.NUM_COLS * Board.NUM_ROWS;
        image_list = new Bitmap[numTiles];
        // split image
        for(int i = 0; i < numTiles; i++){
            if(i > 0 && i%3 == 0){
                x = 0;
                y += tile_height;
            }else if (i > 0){
                x += tile_width;
            }
            image_list[i] = Bitmap.createBitmap(image, x, y, tile_width, tile_height);
        }
        GameActivity.backgrounds = image_list;
    }
    }

