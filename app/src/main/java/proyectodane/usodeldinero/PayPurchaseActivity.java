package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PayPurchaseActivity extends AppCompatActivity {

    /**
     * Valor total de la compra
     */
    String st_total;

    /**
     * Vuelto total de la compra
     */
    String st_change;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_purchase);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        st_total = intent.getStringExtra(getString(R.string.tag_total_value));
        // TODO: Luego se usa el total para calcular el vuelto que debo recibir

        // TODO: Aquí tengo que calcular el listado de billetes que uso para pagar y su respectivo vuelto
        // Por ejemplo si pago $90, calculo a partir de lo que tengo en la billetera y...
        // ...obtengo como resultado: $50, $20, $10, $10
        // Entonces creo un vector que tenga los ID que representen cada billete: [p50,p20,p10,p10]
        // Y luego dentro de getItem devuelvo el ID correspondiente según la posición en el vector que se encuentre


        // Instancia un ViewPager y un PagerAdapter, para deslizar las imágenes
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Instancia un LinearLayout, para representar los puntos debajo de las imágenes
        sliderDotsPanel = (LinearLayout) findViewById(R.id.SliderDots);

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
    public void sendToControlChange(View view) {
        // TODO: Primero verificar con si el vuelto es 0, ya que en ese caso no hace falta controlar el vuelto
        st_change = getString(R.string.value_10); // TODO: Reemplazar por la linea que calcula el vuelto
        Intent intent = new Intent(this, ControlChangeActivity.class);
        intent.putExtra(getString(R.string.tag_total_change),st_change); // TODO: Enviar el cambio (importe) que debo recibir
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

    // TODO: Crear acá también sendToFinalizePurchase(), para los casos donde el vuelto es 0

}
