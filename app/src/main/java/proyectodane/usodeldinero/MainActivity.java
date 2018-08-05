package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Agrego "Splash Screen"
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calculo todos los valores en la billetera a mostrar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesInWallet(this);

        // Clase que se encarga de manejar lo referido al slide de imágenes y puntos
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        ImageSlideManager imageSlideManager = new ImageSlideManager(this,
                (ViewPager) findViewById(R.id.pager_main),
                getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) findViewById(R.id.SliderDots_main),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
    }

    @Override
    public void onBackPressed() {
        //TODO: Ver si lo dejo sin que salga de la app, o si la cierro
    }

    /**
     * Envía a la pantalla de ingreso de importe de productos al presionar el botón
     **/
    public void sendToBasket(View view) {
        Intent intent = new Intent(this, BasketActivity.class);
        startActivity(intent);
    }

    /**
     * Envía a la pantalla de carga de billetera
     **/
    public void sendToLoadWallet(View view) {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    /**
     * Muestra el texto de ayuda para este activity
     **/
    public void showHelp(View view) {
        //Snackbar.make(findViewById(R.id.coordinatorLayout_Main),R.string.help_text_main,Snackbar.LENGTH_LONG).show(); // TODO: Versión original, borrar luego de implementar la nueva versión

        // Creo el Snackbar
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout_Main),R.string.help_text_main,Snackbar.LENGTH_LONG);

        // Agrego un botón al Snackbar
        snackbar.setAction("Listo",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acá se inicia la acción al presionar el botón del Snackbar
            }
        });

        // Obtengo el view del Snackbar para luego setear la cantidad máxima de líneas de texto permitidas
        View snackBarView = snackbar.getView();
        TextView snackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setMaxLines(4);

        // Muestro el Snackbar
        snackbar.show();
    }

}
