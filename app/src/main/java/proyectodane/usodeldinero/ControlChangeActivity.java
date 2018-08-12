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

public class ControlChangeActivity extends AppCompatActivity {

    // TODO: Ver si se agrega un botón para cancelar todo (Ir a la pantalla anterior o poner en 0 el contador de vuelto)

    /**
     * Valor total de la compra
     */
    private String st_totalPurchase;

    /**
     * Vuelto total de la compra, esperado
     */
    private String st_change_expected;

    /**
     * Vuelto recibido de la compra (importe)
     */
    private String st_received_change;

    /**
     * Vuelto recibido de la compra (todos los ID de cada uno de los valores)
     */
    private ArrayList<String> al_receivedChange;

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

        // Obtengo el intent que inició el activity, extraigo el valor del pago total e inicio el vuelto recibido en 0
        Intent intent = getIntent();
        st_totalPurchase = intent.getStringExtra(getString(R.string.tag_total_value));
        st_change_expected = wm.expectedChangeValue(st_totalPurchase,this);
        st_received_change = getString(R.string.value_0);

        // ArrayList con todos los valores de billetes/monedas existentes. Calculo todos los valores a usar para pagar
        ArrayList<String> moneyValueNames = wm.getInstance().obtainMoneyValueNamesOfValidCurrency(this);

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
        Intent intent = new Intent(this, FinalizePurchaseActivity.class);
        al_receivedChange = new ArrayList<String>(); al_receivedChange.add(getString(R.string.value_10)); // TODO: Implementar en R3. Se debe cargar un listado con todos los billetes recibidos (cambio). Borrar el "new"
        intent.putStringArrayListExtra(getString(R.string.received_change),al_receivedChange);
        intent.putExtra(getString(R.string.tag_total_value),st_totalPurchase);
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

        // TODO: Implementar en R3. Se debe guardar en un listado cada ID de billete/moneda recibido (cambio)

        // Sumo al cambio recibido
        st_received_change = wm.addValues(st_received_change,getString(R.string.value_10)); // TODO: Sumar según billete elegido, sacar el "R.string.value_10"

        // Actualizo el texto del importe recibido
        TextView textView = findViewById(R.id.textView5);
        String st_textViewValue = getString(R.string.change_amount) + st_received_change;

        // Si el vuelto es el total, lo informo
        if(isChangeOK()) {
            st_textViewValue = st_textViewValue + " " + getString(R.string.change_OK);
        }

        // Reflejo el cambio en el TextView
        textView.setText(st_textViewValue);

    }

    /**
     *  Verifico si el vuelto recibido es igual al vuelto total
     *  y actualizo la hablitación de botones en base al resultado
     **/
    private boolean isChangeOK(){

        Button acceptChangeButton = (Button) findViewById(R.id.button10);
        Button addToChangeButton = (Button) findViewById(R.id.button9);
        boolean changeOk = true; // TODO: Cambiar por: wm.isTotalChangeReceivedOk(st_received_change,st_change_expected);

        if (changeOk) {
            acceptChangeButton.setEnabled(true);
            addToChangeButton.setEnabled(false);
        }

        return changeOk;
    }


    /**
     * Muestra el texto de ayuda para este activity
     **/
    public void showHelp(View view) {
        SnackBarManager sb = new SnackBarManager();
        sb.showTextIndefiniteOnClickActionDisabled(this,view,findViewById(R.id.coordinatorLayout_ControlChange),getString(R.string.help_text_control_change),10);
    }


}
