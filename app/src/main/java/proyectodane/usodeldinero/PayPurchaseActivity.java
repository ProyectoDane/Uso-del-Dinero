package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PayPurchaseActivity extends AppCompatActivity {

    String st_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_purchase);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        st_total = intent.getStringExtra(getString(R.string.tag_total_value));
        // TODO: Luego se usa el total para calcular el vuelto que debo recibir
    }

    /** Envía a la pantalla principal */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /** Envía a la pantalla de control de vuelto */
    public void sendToControlChange(View view) {
        this.sendToMain(view); //TODO: borrar esta línea y descomentar las líneas del final al crear "ControlChangeActivity"

        /*Intent intent = new Intent(this, ControlChangeActivity.class);
        intent.putExtra(getString(R.string.tag_total_value),st_total);
        startActivity(intent);*/
    }


}
