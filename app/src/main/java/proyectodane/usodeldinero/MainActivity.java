package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Agrego "Splash Screen"
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Verifico inicialización de archivo de valores
        WalletManager.getInstance().checkFirstRun(this);

        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);

    }

}
