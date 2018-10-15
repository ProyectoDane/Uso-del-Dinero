package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

public class DeleteCurrencyActivity extends AppCompatActivity {

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;

    /**
     * ID del valor seleccionado
     */
    String st_valueID;

    /**
     * Importe del valor seleccionado
     */
    String st_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_currency);

        // Obtengo todos los valores a mostrar para la carga de la billetera
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesOfValidCurrency(this);

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(this,
                (ViewPager) findViewById(R.id.pager_deleteCurrency),
                getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) findViewById(R.id.SliderDots_deleteCurrency),
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
     * Informo el valor seleccionado a borrar
     **/
    public void informDelete (View view) {
        // Obtengo el ID del valor elegido
        st_valueID = imageSlideManager.getActualValueID();

        // Obtengo el valor monetario a partir del ID
        st_value = WalletManager.getInstance().obtainValueFormID(this,st_valueID);

        // Envío mensaje de confirmación
        SnackBarManager sb = new SnackBarManager();
        sb.showTextShortOnClickActionDisabled(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.value_selected)+st_value,5);

        // Habilito botón para eliminar el valor
        Button confirmButton = (Button) findViewById(R.id.button26);
        confirmButton.setEnabled(true);
    }

    /**
     * Elimino el valor seleccionado
     **/
    public void deleteValue (View view) {

        // Elimino el valor de los registros


        // Elimino el archivo de imagen TODO: Habilitar al finalizar
/*        String pathToImage = getFilesDir() + "/" + st_valueID;
        File dir = getFilesDir();
        File file = new File(dir,st_valueID);
        file.delete();*/

        // Inhabilito botón para eliminar el valor
        Button confirmButton = (Button) findViewById(R.id.button26);
        confirmButton.setEnabled(false);

        // Envío mensaje de confirmación
        SnackBarManager sb = new SnackBarManager();
        sb.showTextIndefiniteOnClickActionStartActivity(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.msg_value_saved),5,MainActivity.class,this);

    }

}
