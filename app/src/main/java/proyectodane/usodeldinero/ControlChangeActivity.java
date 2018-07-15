package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControlChangeActivity extends AppCompatActivity {

    // TODO: Ver si se agrega un botón para cancelar todo (Ir a la pantalla anterior o poner en 0 el contador de vuelto)

    /**
     * Vuelto total de la compra
     */
    String st_total_change;

    /**
     * Vuelto recibido de la compra
     */
    String st_received_change;

    /**
     * El widget pager, maneja la animación y permite deslizar horizontalmente para acceder
     * a las imágenes anteriores y siguientes.
     */
    private ViewPager mPager;

    /**
     * El pager adapter, provee las páginas al ViewPager.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Un LinearLayout que representa la fila de puntos la cual indica la posición relativa de
     * la imagen y la cantidad total de imágenes del ViewPager.
     */
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;

    /**
     * ArrayList con todos los valores de billetes/monedas existentes
     */
    private ArrayList<String> moneyValueNames;

    /**
     * ArrayList con todos los Fragment instanciados, de billetes/monedas existentes
     */
    private ArrayList<ScreenSlidePageFragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_change);

        // Obtengo el intent que inició el activity, extraigo el valor del cambio total e inicio el vuelto recibido en 0
        Intent intent = getIntent();
        st_total_change = intent.getStringExtra(getString(R.string.tag_total_change));
        st_received_change = getString(R.string.value_0);

        // Calculo todos los valores a usar para pagar
        moneyValueNames = calculateMoneyValueNames();

        // Cargo todos los Fragment que alimentarán al PagerAdapter
        fragments = buildFragments();

        // Instancia un ViewPager y un PagerAdapter, para deslizar las imágenes
        mPager = (ViewPager) findViewById(R.id.pager_change);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),fragments);
        mPager.setAdapter(mPagerAdapter);

        // Instancia un LinearLayout, para representar los puntos debajo de las imágenes
        sliderDotsPanel = (LinearLayout) findViewById(R.id.SliderDots_change);

        // Setea la cantidad de puntos y el arreglo de ImageView para cada uno de ellos
        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        // Carga la imagen para cada punto en el estado inicial
        for(int i = 0; i < dotsCount; i++){

            // Instancia el ImageView y setea la imagen
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            // Prepara los parámetros de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            // Setea los parámetros de la imagen
            sliderDotsPanel.addView(dots[i], params);
        }

        // En el estado inicial, el primer punto será el seleccionado. Seteo la imagen.
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        // Agrego un listener que será invocado cuando la imagen cambie y actualizará las imágenes de los puntos
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /* "This method will be invoked when the current page is scrolled, either as part of a
                programmatically initiated smooth scroll or a user initiated touch scroll." */
            }

            @Override  // Cuando una nueva imagen es seleccionada, actualizo las imágenes de los puntos
            public void onPageSelected(int position) {

                // Actualizo las imágenes de los puntos inactivos
                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                // Actualizo la imagen del punto activo
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /* "Called when the scroll state changes" */
            }
        });

        // Actualizo el texto del importe recibido
        TextView textView = findViewById(R.id.textView5);
        st_received_change = getString(R.string.change_amount) + getString(R.string.value_0);
        textView.setText(st_received_change);

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
        if (mPager.getCurrentItem() == 0) {
            // Si el usuario se encuentra en la primer imagen y presiona el botón "back"
            // no permito que se vaya a otra pantalla, para evitar el error
            // de presionar ese botón por equivocación.
        } else {
            // Si es otra imagen que no sea la primera uso el "back" para volver atrás una imagen
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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


    /**
     * Creo la lista de valores a partir de todos billetes/monedas existentes
     * */
    private ArrayList<String> calculateMoneyValueNames(){

        // Instancio la lista de valores
        ArrayList<String> valueNames = new ArrayList<String>();

        // TODO: Aquí tengo que cargar todos los billetes y monedas existentes (Desde la base de datos)
        // Cargo la lista de valores
        valueNames = temp_arrayGeneratorFromWalletManager();
/*      valueNames.add(getString(R.string.tag_p5));
        valueNames.add(getString(R.string.tag_p5b));
        valueNames.add(getString(R.string.tag_p10));
        valueNames.add(getString(R.string.tag_p10b));
        valueNames.add(getString(R.string.tag_p20));
        valueNames.add(getString(R.string.tag_p20b));
        valueNames.add(getString(R.string.tag_p50));
        valueNames.add(getString(R.string.tag_p50b));
        valueNames.add(getString(R.string.tag_p100));
        valueNames.add(getString(R.string.tag_p100b));
        valueNames.add(getString(R.string.tag_p200));
        valueNames.add(getString(R.string.tag_p500));
        valueNames.add(getString(R.string.tag_p1000));*/


        return valueNames;
    }


    /**
     * Creo la lista de Fragment a partir de todos los valores de billetes/monedas de moneyValueNames
     * */
    private ArrayList<ScreenSlidePageFragment> buildFragments() {

        // Instancio la lista de Fragment
        ArrayList<ScreenSlidePageFragment> frags = new ArrayList<ScreenSlidePageFragment>();

        // Cargo la lista de Fragment
        for(int i = 0; i<moneyValueNames.size(); i++) {

            // Instancio el Fragment
            ScreenSlidePageFragment frag = new ScreenSlidePageFragment();

            // Creo un bundle para pasarle el ID del billete/moneda como argumento
            Bundle args = new Bundle();
            args.putString(getString(R.string.tag_money_value_name),moneyValueNames.get(i));
            frag.setArguments(args);
            frags.add(frag);
        }

        return frags;
    }

    // TODO: Borrar luego de probar. Solo test.
    private ArrayList<String> temp_arrayGeneratorFromWalletManager(){

        // Creo un Map con valores a guardar en wl WalletManager
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

        // Cargo los valores recién guardados en otro Map
        Map<String,String> tempMapLoad = WalletManager.getInstance().getCurrencyInWallet(this);

        // Paso los valores cargados a un ArrayList
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String,String> entry : tempMapLoad.entrySet()) {
            list.add(entry.getKey());
        }

        return list;
    }
}
