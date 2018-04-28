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

    /** Envía a la pantalla de ingreso de importe de productos al presionar el botón */
    public void sendToBasket(View view) {
        Intent intent = new Intent(this, BasketActivity.class);
        startActivity(intent);
    }


}
