package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    /**
     * Importe total a de la billetera, con la suma de la carga
     */
    private String st_total;

    /**
     * Importe a cargar
     */
    private String st_subtotal;

    /**
     * ArrayList con todos los valores de billetes/monedas a cargar en la billetera
     */
    private ArrayList<String> newLoadMoneyValueNames;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;

    /**
     * Instancia de WalletManager
     */
    private static final WalletManager wm = WalletManager.getInstance();

    /**
     * Clase que se encarga de manejar los mensajes emergentes
     */
    private SnackBarManager sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Inicio la lista de valores a cargar en la billetera
        newLoadMoneyValueNames = new ArrayList<String>();

        // Actualizo el valor del total, inicio el subtotal en cero y muestro en pantalla
        st_subtotal = getString(R.string.value_0);
        st_total = wm.obtainTotalCreditInWallet(this);
        refreshSubtotalAndTotal();

        // Inicio el SnackBarManager para luego crear mensajes emergentes
        sb = new SnackBarManager();

        // Obtengo todos los valores a mostrar para la carga de la billetera
        ArrayList<String> moneyValueNames = wm.obtainMoneyValueNamesOfValidCurrency(this);

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(this,
                (ViewPager) findViewById(R.id.pager_wallet),
                getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) findViewById(R.id.SliderDots_wallet),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

    }

    @Override
    public void onBackPressed() {
        imageSlideManager.defaultOnBackPressed();
    }

    /**
     * Cancela la carga en la billetera
     * */
    public void cancelLoad(View view) {
        sendToMain(view);
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Muestra el texto de ayuda para este activity
     **/
    public void showHelp(View view) {
        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_Wallet),getString(R.string.help_text_wallet),10);
    }

    /**
     * Actualiza el valor de la carga y del total
     **/
    public void refreshSubtotalAndTotal() {
        TextView textView = findViewById(R.id.textView6);
        textView.setText(getString(R.string.load_cash_sign) + st_subtotal + " - " + getString(R.string.total_cash_sign) + st_total);
    }

    // TODO: Ver si se agrega una cantidad límite de valores a ingresar en la billetera (Físicamente no se pueden poner infinitos billetes y/o monedas)
    /**
     * Sumo el valor seleccionado al subtotal de carga en billetera
     **/
    public void addValueToSubtotal (View view) {

        // Obtengo el ID del valor elegido
        String st_valueID = imageSlideManager.getActualValueID();

        // Obtengo el valor monetario a partir del ID
        String st_value = wm.obtainValueFormID(this,st_valueID);

        // Agrego el ID a la lista para la futura carga, sumo el valor al subtotal y total para luego mostrarlo
        newLoadMoneyValueNames.add(st_valueID);
        st_subtotal = wm.addValues(st_value,st_subtotal);
        st_total = wm.addValues(st_value,st_total);
        refreshSubtotalAndTotal();

        // Creo el mensaje para notificar el valor seleccionado a sumar a la billetera y lo muestro
        String st_snackBarText = getString(R.string.value_selected_for_load) + st_value;
        sb.showTextShortOnClickActionDisabled(findViewById(R.id.coordinatorLayout_Wallet),st_snackBarText,2);
    }


    /**
     * Agrega la carga de dinero seleccionada en la billetera y luego envía a la pantalla principal
     * */
    public void addValuesToWallet (View view){

        // Guardo todos los valores seleccionados hasta el momento
        for(String currentNewLoadMoneyValueName : newLoadMoneyValueNames) {
            wm.addCurrencyInWallet(this,currentNewLoadMoneyValueName);
        }

        sendToMain(view);
    }

}
