package fall2018.csc2017.pong;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import generalclasses.User;

/**
 * GameActivity of Pong game. Creates the SurfaceView and load the game if needed
 * Since this class only contains view and buttons, excluded from Unit test
 */
public class PongGameActivity extends AppCompatActivity {

    /**
     * View of the game
     */
    PongSurfaceView pongView;

    /**
     * Save Button
     */
    private Button saveButton;


    /**
     * User class of current user logged in
     */
    private User user;


    public static final String SAVE_FILENAME = "master_save_file.ser";

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get existing GameInfo (might be null)
        PongGameInfo gameInfo = (PongGameInfo) getIntent().getSerializableExtra("saveToLoad");
        String username = getIntent().getStringExtra("username");
        user = User.usernameToUser.get(username);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        if (gameInfo == null) {
            gameInfo = new PongGameInfo(size.x, size.y, username);
            pongView = new PongSurfaceView(this, size.x, size.y, gameInfo);
        }
        else {
            pongView = new PongSurfaceView(this, size.x, size.y, gameInfo);
        }

        viewSetUp(pongView);
    }

    private void viewSetUp(PongSurfaceView pongview){
        //Create your frame layout
        FrameLayout frameLayout = new FrameLayout(this);

        //Adding PongView into frameLayout
        frameLayout.addView(pongView);

        //Creating button
        saveButton = new Button(this);
        saveButton.setText("SAVE");
        saveButton.setBackgroundColor(Color.TRANSPARENT);
        addSaveButtonListener();

        /// Declaring and initializing LayoutParams for the frameLayout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        // Setting the location of the button
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.RIGHT;

        //Adding saveButton to frameLayout
        frameLayout.addView(saveButton, params);
        setContentView(frameLayout);
    }



    /**
     * Save the current hash map with each user's saves to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(User.usernameToUser);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Save button
     */
    private void addSaveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pongView.controller.setPaused(true);
                Toast.makeText(PongGameActivity.this, "Paused and Saved!", Toast.LENGTH_SHORT).show();
                @SuppressLint("SimpleDateFormat") String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new
                        Date());
                user.addSave("Pong", currentTime, pongView.getGameInfo());
                saveToFile(SAVE_FILENAME);
            }
        });
    }

    /**
     * Activates when game is started/resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        pongView.resume();
    }

    /**
     * Activates when game is paused/closed
     */
    @Override
    protected void onPause() {
        super.onPause();
        pongView.pause();
    }

}
