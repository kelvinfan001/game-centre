package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//View Class, not tested
/**
 * The activity for selecting the complexities of the game
 */
public class SlidingTilesComplexityActivity extends AppCompatActivity {

    private String username;
    private Button threes;
    private Button fours;
    private Button fives;
    private Button play_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // retrieve the current user from the previous activity
        username = getIntent().getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingtile_complexity);
        threes = findViewById(R.id.three);
        fours = findViewById(R.id.four);
        fives = findViewById(R.id.five);
        play_image =  findViewById(R.id.play_image);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resources", android.R.color.white); // the second parameter will be fallback if the preference is not found
        getWindow().setBackgroundDrawableResource(bg);
        addThreeButtonListener();
        addFourButtonListener();
        addFiveButtonListener();
        addPlayImageButtonListener();

    }

    /**
     * Listener for the button of 3 x 3. When it's clicked, move onto the StartingActivity
     * After it sets the NUM_COLS and NUM_ROWS to 3
     */
    private void addThreeButtonListener() {
        threes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Board.NUM_COLS = 3; // set the number of rows/cols here
                Board.NUM_ROWS = 3;
                GameActivity.image_game = false;
                Intent intent = new Intent(SlidingTilesComplexityActivity.this, StartingActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("complexity", "3 x 3");
                startActivity(intent);
            }
        });
    }

    /**
     * Listener for the button of 4 x 4. When it's clicked, move onto the StartingActivity
     * After it sets the NUM_COLS and NUM_ROWS to 4
     */
    private void addFourButtonListener() {
        fours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Board.NUM_ROWS = 4;
                Board.NUM_COLS = 4;
                GameActivity.image_game = false;
                Intent intent = new Intent(SlidingTilesComplexityActivity.this, StartingActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("complexity", "4 x 4");
                startActivity(intent);
            }
        });
    }

    /**
     * Listener for the button of 5 x 5. When it's clicked, move onto the StartingActivity
     * After it sets the NUM_COLS and NUM_ROWS to 5
     */
    private void addFiveButtonListener() {
        fives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Board.NUM_COLS = 5;
                Board.NUM_ROWS = 5;
                GameActivity.image_game = false;
                Intent intent = new Intent(SlidingTilesComplexityActivity.this, StartingActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("complexity", "5 x 5");
                startActivity(intent);
            }
        });
    }

    /**
     * Listener for the button of image game. When it's clicked, move onto the DownloadingActivity
     */
    private void addPlayImageButtonListener() {
        play_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlidingTilesComplexityActivity.this, DownloadingActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("complexity", "Image");
                startActivity(intent);
            }
        });
    }

}
