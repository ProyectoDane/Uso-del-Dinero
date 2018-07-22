package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class FinalizePurchaseActivity extends AppCompatActivity {

    /**
     * Vuelto recibido de la compra, en forma de listado de String
     */
    private ArrayList<String> al_receivedChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_purchase);

        // TODO: Implementar en R3. Se debe recibir un listado con todos los billetes recibidos (cambio)
        // Obtengo el intent que inició el activity, extraigo el listado de los billetes recibidos (cambio)
        Intent intent = getIntent();
        al_receivedChange = intent.getStringArrayListExtra(getString(R.string.received_change));

        // TODO: Si recibo un listado vacío quiere decir que no tenía que recibir cambio (pago justo)
        // TODO: Entonces debo cambiar el mensaje del textView2 Sacando "Vuelto recibido"...
        // TODO: Haciendo invisible el botón de "Guardar Vuelto"...
        // TODO: ...y Cambiando la etiqueta del botón "No guardar vuelto", por un Aceptar"
    }

    @Override
    public void onBackPressed() {
        // No realiza acción, para obligar al usuario a tomar la decisión a través de los botones
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Guarda todos los billetes recibidos en concepto de vuelto y regresa a la pantalla principal
     * */
    public void saveToWallet(View view) {
        // TODO: Aquí se deben guardar todos los billetes recibidos (vuelto), antes de ir a la pantalla principal
        sendToMain(view);
    }

}
