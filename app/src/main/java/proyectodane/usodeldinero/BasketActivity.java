package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class BasketActivity extends AppCompatActivity {

    /**
     * Valor total de la compra
     * */
    private String st_total_purchase;

    /**
     * EditText que registra los valores ingresados por producto
     * */
    private EditText et_productValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // Al iniciar, seteo el total en cero
        st_total_purchase = getString(R.string.value_0);

        // Obtiene el ID del EditText del valor ingresado
        et_productValue = (EditText) findViewById(R.id.editText);

        // Obtengo el saldo en billetera
        refreshTotalInWallet();
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Suma el valor ingresado al total
     * */
    public void addToTotal(View view) {

        // Obtiene el nuevo valor ingresado
        String st_newValue = et_productValue.getText().toString();

        // Verifico si tiene formato numérico inválido
        if ( !(WalletManager.getInstance().isFloatFormatValid(st_newValue)) ) {
            resetEditTextValue();
            return;
        }

        // Suma el nuevo valor al total (redondeando [FLOOR] para obtener hasta 2 decimales)
        String st_newTotal = WalletManager.getInstance().addValues(st_total_purchase,st_newValue);

        // Si el total en la billetera no alcanza para pagar la compra total...
        if ( WalletManager.getInstance().isGreaterThanTotalWallet(this,st_newTotal) ) {

            // Aviso que el dinero es insuficiente y descarto la suma
            Snackbar.make(findViewById(R.id.coordinatorLayout_Basquet),R.string.insufficient_funds,Snackbar.LENGTH_LONG).show();

        } else {

            // Si alcanza, agrego el importe al total de la compra
            st_total_purchase = st_newTotal;

            // Actualiza el valor total actual en la vista
            TextView textView = findViewById(R.id.textView);
            textView.setText(getString(R.string.total_purchase) + st_total_purchase);
        }

        // Blanquea el valor a ingresar en la vista
        resetEditTextValue();

        // Si el total es mayor a cero, habilito el botón para pagar
        Button payButton = (Button) findViewById(R.id.button3);
        if (WalletManager.getInstance().isGreaterThanValueZero(st_total_purchase)) {
            payButton.setEnabled(true);
        }

    }


    /**
     * Envía a la pantalla de confirmación de compra
     * */
    public void sendToOrderTotal(View view) {
        // En caso de que haya quedado un valor a ingresar, blanquea el valor al irse de la vista
        if (!(et_productValue.getText().toString().isEmpty())) et_productValue.setText(getString(R.string.empty_string));

        Intent intent = new Intent(this, OrderTotalActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), String.valueOf(st_total_purchase));
        startActivity(intent);
    }

    /**
    * Blanquea el valor a ingresar en la vista del EditText
    **/
    private void resetEditTextValue(){
        et_productValue.setText(getString(R.string.empty_string));
    }

    /**
     * Muestra el texto de ayuda para este activity
     **/
    public void showHelp(View view) {
        SnackBarManager sb = new SnackBarManager();
        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_Basquet),getString(R.string.help_text_basket),7);
    }

    /**
     * Actualiza el valor total ahorrado en la billetera
     **/
    public void refreshTotalInWallet() {
        TextView textView = findViewById(R.id.textView14);
        String newText = getString(R.string.saved_money_pesos) + WalletManager.getInstance().obtainTotalCreditInWallet(this);
        textView.setText(newText);
    }

}
