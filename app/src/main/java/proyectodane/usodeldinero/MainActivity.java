package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Splash Screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Envía a la pantalla de compra al presionar el botón */
    public void sendToPurchase(View view) {
       // Intent intent = new Intent(this, PurchaseActivity.class);
       // startActivity(intent);
    }


}
