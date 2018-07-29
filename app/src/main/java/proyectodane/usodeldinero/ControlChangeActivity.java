package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ControlChangeActivity extends AppCompatActivity {

    // TODO: Ver si se agrega un botón para cancelar todo (Ir a la pantalla anterior o poner en 0 el contador de vuelto)

    /**
     * Vuelto total de la compra
     */
    private String st_total_change;

    /**
     * Vuelto recibido de la compra
     */
    private String st_received_change;

    /**
     * ArrayList con todos los valores de billetes/monedas existentes
     */
    private ArrayList<String> moneyValueNames;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;

    /**
     * Instancia de WalletManager
     */
    private static final WalletManager wm = WalletManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_change);

        // Obtengo el intent que inició el activity, extraigo el valor del cambio total e inicio el vuelto recibido en 0
        Intent intent = getIntent();
        st_total_change = intent.getStringExtra(getString(R.string.tag_total_change));
        st_received_change = getString(R.string.value_0);

        // Calculo todos los valores a usar para pagar
        moneyValueNames = wm.obtainMoneyValueNamesOfValidCurrency(this);

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(this,
                (ViewPager) findViewById(R.id.pager_change),
                getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) findViewById(R.id.SliderDots_change),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *  Envía a la pantalla de finalización de la compra
     **/
    public void sendToFinalizePurchase(View view) {
        // TODO: Implementar en R3. Se debe mandar un listado con todos los billetes recibidos (cambio)
        Intent intent = new Intent(this, FinalizePurchaseActivity.class);
        ArrayList<String> al_receivedChange = new ArrayList<String>();
        intent.putStringArrayListExtra(getString(R.string.received_change),al_receivedChange);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        imageSlideManager.defaultOnBackPressed();
    }

    /**
     *  Actualizo el valor del vuelto recibido
     **/
    public void addToChange(View view){
        // Actualizo el texto del importe recibido
        TextView textView = findViewById(R.id.textView5);
        st_received_change = getString(R.string.change_amount) + getString(R.string.value_10); // TODO: cargar según billete elegido

        // Si el vuelto es el total, lo informo
        if(changeOK()) st_received_change = st_received_change + " " + getString(R.string.change_OK); // TODO: Se podría inhabilitar el botón de "agregar al vuelto"

        // Muestro el nuevo valor
        textView.setText(st_received_change);
    }

    /**
     *  Verifico si el vuelto recibido es igual al vuelto total
     **/
    private boolean changeOK(){

        // TODO: Implementar verificando si el vuelto recibido es igual (o mayor) al vuelto total

        // Si el total es mayor a cero, habilito el botón para pagar
        Button acceptChangeButton = (Button) findViewById(R.id.button10);
        Button addToChangeButton = (Button) findViewById(R.id.button9);
        if (true){
            acceptChangeButton.setEnabled(true);
            addToChangeButton.setEnabled(false);
        }
        return true;
    }

}
