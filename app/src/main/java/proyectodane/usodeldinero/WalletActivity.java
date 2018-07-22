package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {

    /**
     * Importe total a de la billetera, sin sumar la carga
     */
    String st_total_wallet;

    /**
     * Importe total a cargar
     */
    String st_total_load;

    /**
     * ArrayList con todos los valores de billetes/monedas a cargar en la billetera
     */
    private ArrayList<String> loadMoneyValueNames;

    /**
     * ArrayList con todos los valores de billetes/monedas existentes
     */
    private ArrayList<String> moneyValueNames;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Calculo todos los valores a usar para pagar
        moneyValueNames = obtainMoneyValueNames();

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
     * Creo la lista de valores a partir de todos billetes/monedas existentes
     * */

    private ArrayList<String> obtainMoneyValueNames(){

        // Todo: Reemplazar esta forma de carga con la definitiva

        // Creo un Map con valores a guardar en el WalletManager
        Map<String,String> tempMapSave = new HashMap<String,String>();
        tempMapSave.put(getString(R.string.tag_p5),"5");
        tempMapSave.put(getString(R.string.tag_p5b),"5");
        tempMapSave.put(getString(R.string.tag_p10),"10");
        tempMapSave.put(getString(R.string.tag_p10b),"10");
        tempMapSave.put(getString(R.string.tag_p20),"20");
        tempMapSave.put(getString(R.string.tag_p20b),"20");
        tempMapSave.put(getString(R.string.tag_p50),"50");
        tempMapSave.put(getString(R.string.tag_p50b),"50");
        tempMapSave.put(getString(R.string.tag_p100),"100");
        tempMapSave.put(getString(R.string.tag_p100b),"100");
        tempMapSave.put(getString(R.string.tag_p200),"200");
        tempMapSave.put(getString(R.string.tag_p500),"500");
        tempMapSave.put(getString(R.string.tag_p1000),"1000");

        // Guardo los valores del Map
        WalletManager.getInstance().setCurrencyInWallet(this,tempMapSave);

        // Cargo los valores recién guardados, en otro Map
        Map<String,String> tempMapLoad = WalletManager.getInstance().getCurrencyInWallet(this);

        // Paso los valores cargados del Map a un ArrayList
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String,String> entry : tempMapLoad.entrySet()) {
            list.add(entry.getKey());
        }

        // Pruebo ordenar los elementos de la lista
        Collections.sort(list); // TODO: verificar luego con la nueva codificación de nombres de imágenes

        return list;
    }

}
