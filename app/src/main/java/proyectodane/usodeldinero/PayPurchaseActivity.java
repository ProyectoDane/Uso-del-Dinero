package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class PayPurchaseActivity extends AppCompatActivity {

    /**
     * Valor total de la compra
     */
    private String st_totalPurchase;

    /**
     * Vuelto total de la compra
     */
    private String st_change;

    /**
     * ArrayList con todos los valores de billetes/monedas calculados para dar el pago
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
        setContentView(R.layout.activity_pay_purchase);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        st_totalPurchase = intent.getStringExtra(getString(R.string.tag_total_value));

        // Calculo todos los valores a usar para pagar
        moneyValueNames = wm.obtainMoneyValueNamesOfPayment(this,st_totalPurchase);

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(this,
                (ViewPager) findViewById(R.id.pager_pay),
                getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) findViewById(R.id.SliderDots_pay),
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
     *  Envía a la pantalla de control de vuelto.
     *  Si no corresponde vuelto, envía directamente a finalizar
     **/
    public void sendToControlChange(View view) {

        if ( wm.isChangeExpected(this,st_totalPurchase) ) {
            Intent intent = new Intent(this, ControlChangeActivity.class);
            intent.putExtra(getString(R.string.tag_total_value),st_totalPurchase);
            startActivity(intent);
        } else {
            sendToFinalizePurchase(view);
        }

    }

    @Override
    public void onBackPressed() {
        imageSlideManager.defaultOnBackPressed();
    }

    /**
     *  Envía a la pantalla de finalización de la compra (para los casos donde el vuelto es nulo)
     **/
    public void sendToFinalizePurchase(View view) {
        Intent intent = new Intent(this, FinalizePurchaseActivity.class);

        // Creo un array vacío (que representa un vuelto nulo)
        ArrayList<String> al_receivedChangeEmpty = new ArrayList<String>();

        intent.putStringArrayListExtra(getString(R.string.received_change),al_receivedChangeEmpty);
        intent.putExtra(getString(R.string.tag_total_value),st_totalPurchase);
        startActivity(intent);
    }


    /**
     * Muestra el texto de ayuda para este activity
     **/
    public void showHelp(View view) {
        SnackBarManager sb = new SnackBarManager();
        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_PayPurchase),getString(R.string.help_text_pay_purchase),10);
    }

}
