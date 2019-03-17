package proyectodane.usodeldinero;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            //Muestro la flecha atrás en el actionbar
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    /**
//     * Informo el valor seleccionado a borrar
//     **/
//    public void informDelete (View view) {
//        // Obtengo el ID del valor elegido
//        st_valueID = imageSlideManager.getActualValueID();
//
//        // Obtengo el valor monetario a partir del ID
//        st_value = WalletManager.getInstance().obtainValueFormID(this,st_valueID);
//
//        // Envío mensaje de confirmación
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextShortOnClickActionDisabled(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.value_selected)+st_value,5);
//
//        // Actualizo el texto con el valor seleccionado
//        TextView textView = findViewById(R.id.textView13);
//        String text = getString(R.string.value_selected)+st_value;
//        textView.setText(text);
//
//        // Habilito botón para eliminar el valor
//        Button confirmButton = (Button) findViewById(R.id.button26);
//        confirmButton.setEnabled(true);
//
//    }

//    /**
//     * Elimino el valor seleccionado
//     **/
//    public void deleteValue (View view) {
//
//        // Verifico si este es el último valor existente, en ese caso, impido su eliminación
//        if(WalletManager.getInstance().isThereOnlyOneValidCurrency(this)){
//
//            // Inhabilito el botón para seleccionar y el botón para eliminar el valor
//            setEnabledFalseSelectAndConfirmButton();
//
//            // Informo sobre la imposibilidad de eliminar y salgo de la activity
//            SnackBarManager sb = new SnackBarManager();
//            sb.showTextIndefiniteOnClickActionStartActivity(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.error_on_file_delete_last_value),5,MainActivity.class,this);
//            return;
//        }
//
//        // Elimino el valor del registro y también los posibles valores de este mismo tipo que pudieran existir en la billetera
//        WalletManager.getInstance().deleteExistingCurrency(this,st_valueID);
//
//        // Elimino el archivo de imagen
//        File dir = getFilesDir();
//        File file = new File(dir,st_valueID);
//        boolean deleteOK = file.delete();
//
//        // Inhabilito el botón para seleccionar y el botón para eliminar el valor
//        setEnabledFalseSelectAndConfirmButton();
//
//        // Informo sobre error, en caso de existir
//        if(!deleteOK){
//            SnackBarManager sb = new SnackBarManager();
//            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.error_on_file_delete),5);
//            return;
//        }
//
//        // Envío mensaje de confirmación
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionStartActivity(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.msg_value_deleted),5,MainTabActivity.class,this);
//
//    }


    public void deleteSelectedValue (View view) {
        // Obtengo el ID del valor elegido
        st_valueID = imageSlideManager.getActualValueID();

        // Obtengo el valor monetario a partir del ID
        st_value = WalletManager.getInstance().obtainValueFormID(this,st_valueID);

        // Verifico si este es el último valor existente, en ese caso, impido su eliminación
        if(WalletManager.getInstance().isThereOnlyOneValidCurrency(this)){

            // Inhabilito el botón para seleccionar y el botón para eliminar el valor
            setEnabledFalseSelectAndConfirmButton();

            // Informo sobre la imposibilidad de eliminar y salgo de la activity
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionStartActivity(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.error_on_file_delete_last_value),5,MainActivity.class,this);
            return;
        }

        // Envío mensaje de confirmación
        final Context context = this;
        String msgValueSelected = getString(R.string.value_selected) + st_value;

        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.question_delete_value_selected))
                .setMessage(msgValueSelected)
                // Especifico un Listener para permitir llevar a cabo acciones cuando se acepta
                .setPositiveButton(getString(R.string.delete_value), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Elimino el valor del registro y también los posibles valores de este mismo tipo que pudieran existir en la billetera
                        WalletManager.getInstance().deleteExistingCurrency(context,st_valueID);

                        // Elimino el archivo de imagen
                        File dir = getFilesDir();
                        File file = new File(dir,st_valueID);
                        boolean deleteOK = file.delete();

                        // Inhabilito el botón para seleccionar y el botón para eliminar el valor
                        setEnabledFalseSelectAndConfirmButton();

                        // Informo sobre error, en caso de existir
                        if(!deleteOK){
                            new AlertDialog.Builder(context)
                                    .setTitle(getString(R.string.error_on_file_delete))
                                    .setPositiveButton(getString(android.R.string.ok),null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            return;
                        }

                        // Envío mensaje de confirmación
                        new AlertDialog.Builder(context)
                                .setTitle(getString(R.string.msg_value_deleted))
                                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(context, MainTabActivity.class);
                                        context.startActivity(intent);

                                        }
                                    })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setCancelable(false)
                                .show();

                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    /**
     * Inhabilito el botón para seleccionar y el botón para eliminar el valor
     **/
    private void setEnabledFalseSelectAndConfirmButton(){
//        Button selectButton = (Button) findViewById(R.id.button25);
//        selectButton.setEnabled(false);
        Button confirmButton = (Button) findViewById(R.id.button26);
        confirmButton.setEnabled(false);
    }

//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_deleteCurrency),getString(R.string.help_text_delete_currency),10);
//    }

}
