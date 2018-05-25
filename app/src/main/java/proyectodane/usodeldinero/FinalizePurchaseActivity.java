package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FinalizePurchaseActivity extends AppCompatActivity {

    /**
     * Vuelto recibido de la compra
     */
    String st_received_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_purchase);

        // Obtengo el intent que inició el activity, extraigo el valor del cambio total e inicio el vuelto recibido en 0
        Intent intent = getIntent();
        st_received_change = intent.getStringExtra(getString(R.string.tag_total_change));
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
