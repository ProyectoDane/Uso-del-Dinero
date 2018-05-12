package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ControlChangeActivity extends AppCompatActivity {

    // TODO: Pantalla parecida a PayPurchaseActivity, pero aquí muestro una vez, todos los billetes
    // TODO: Abajo aparecen los botones para agregar el billete que se muestra y otro botón para aceptar el vuelto
    // TODO: Se debe controlar que el vuelto se encuentre OK. Acá se podría habilitar recién ahí el botón de aceptar vuelto
    // TODO: Ver si se agrega un botón para cancelar todo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_change);
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *  Envía a la pantalla de control de vuelto
     **/
    public void sendToFinalizePurchase(View view) {
        /*Intent intent = new Intent(this, FinalizePurchaseActivity.class);
        intent.putExtra(getString(R.string.tag_total_value),st_total); // TODO: Enviar el vuelto, detallando cada billete
        startActivity(intent);*/

    }

}
