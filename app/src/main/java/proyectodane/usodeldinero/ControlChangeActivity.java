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

    /**
     * Valor total de la compra
     */
    private String st_totalPurchase;

    /**
     * Vuelto total de la compra, esperado
     */
    private String st_changeExpected;

    /**
     * Vuelto recibido de la compra (importe)
     */
    private String st_receivedChangeValue;

    /**
     * Vuelto recibido de la compra (todos los ID de cada uno de los valores)
     */
    private ArrayList<String> al_receivedChange;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_change);

        // Obtengo el intent que inició el activity, extraigo el valor del pago total e inicio las variables del vuelto
        Intent intent = getIntent();
        st_totalPurchase = intent.getStringExtra(getString(R.string.tag_total_value));
        st_changeExpected = WalletManager.getInstance().expectedChangeValue(this,st_totalPurchase);
        st_receivedChangeValue = getString(R.string.value_0);
        al_receivedChange = new ArrayList<String>();

        // Actualizo el valor del vuelto total a recibir
        TextView tv_totalChange = findViewById(R.id.textView15);
        String st_totalChange = getString(R.string.total_change) + st_changeExpected;
        tv_totalChange.setText(st_totalChange);

        // ArrayList con todos los valores de billetes/monedas existentes. Calculo todos los valores a usar para pagar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().getInstance().obtainMoneyValueNamesOfValidCurrency(this);

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

        // Obtengo el ID del valor elegido y lo guardo en el ArrayList
        String st_valueID = imageSlideManager.getActualValueID();
        al_receivedChange.add(st_valueID);

        // Obtengo el valor monetario a partir del ID y lo sumo al vuelto actual
        String st_value = WalletManager.getInstance().obtainValueFormID(this,st_valueID);
        st_receivedChangeValue = WalletManager.getInstance().addValues(st_receivedChangeValue,st_value);

        // Actualizo el texto del importe recibido
        TextView textView = findViewById(R.id.textView5);
        String st_textViewValue = getString(R.string.change_received) + st_receivedChangeValue;

        // Si el vuelto es el total, lo informo
        if(isChangeOK()) {
            st_textViewValue = st_textViewValue + " " + getString(R.string.change_OK);
        }

        // Reflejo el cambio en el TextView
        textView.setText(st_textViewValue);

    }

    /**
     *  Verifico si el vuelto recibido es igual al vuelto total
     *  y actualizo la habilitación de botones en base al resultado
     **/
    private boolean isChangeOK(){

        Button acceptChangeButton = (Button) findViewById(R.id.button10);
        Button addToChangeButton = (Button) findViewById(R.id.button9);
        boolean changeOk = WalletManager.getInstance().isTotalChangeReceivedOk(st_receivedChangeValue, st_changeExpected);

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
        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_ControlChange),getString(R.string.help_text_control_change),10);
    }

}
