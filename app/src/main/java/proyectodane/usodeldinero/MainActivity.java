package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Agrego "Splash Screen"
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);

    }

}
