package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrderTotalActivity extends AppCompatActivity {

    String st_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_total);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        st_total = intent.getStringExtra(getString(R.string.tag_total_value));

        // TODO: Para futuras actualizaciones, borrar esta línea
        st_total = getString(R.string.total_purchase_pesos_20);

        // Capturo el TextView and coloco el texto indicando el total
        TextView textView = findViewById(R.id.textView3);
        textView.setText(st_total);

    }

    /** Envía a la pantalla principal */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /** Envía a la pantalla de pago de la compra */
    public void sendToPayPurchase(View view) {
        Intent intent = new Intent(this, PayPurchaseActivity.class);
        intent.putExtra(getString(R.string.tag_total_value),st_total);
        startActivity(intent);
    }

}
