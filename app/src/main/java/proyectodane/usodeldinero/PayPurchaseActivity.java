package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PayPurchaseActivity extends AppCompatActivity {


    /**
     * Valor total de la compra
     */
    String st_total;

    /**
     * El widget pager, maneja la animación y permite deslizar horizontalmente para acceder
     * a las imágenes anteriores y siguientes.
     */
    private ViewPager mPager;

    /**
     * El pager adapter, provee las páginas al ViewPager.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_purchase);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        st_total = intent.getStringExtra(getString(R.string.tag_total_value));
        // TODO: Luego se usa el total para calcular el vuelto que debo recibir

        // Instancia un ViewPager y un PagerAdapter, para deslizar las imágenes
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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
        this.sendToMain(view); //TODO: borrar esta línea y descomentar las líneas del final al crear "ControlChangeActivity"

        /*Intent intent = new Intent(this, ControlChangeActivity.class);
        intent.putExtra(getString(R.string.tag_total_value),st_total);
        startActivity(intent);*/
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

}
