package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BasketActivity extends AppCompatActivity {

    /**
     * Valor total de la compra
     * */
    private String st_total;

    /**
     * EditText que registra los valores ingresados por producto
     * */
    private EditText et_productValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // Al iniciar, seteo el total en cero
        st_total = getString(R.string.value_0);

        // Obtiene el ID del EditText del valor ingresado
        et_productValue = (EditText) findViewById(R.id.editText);
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
        if (!isFloatFormatValid(st_newValue)) {
            resetEditTextValue();
            return;
        }

        // Suma el nuevo valor al total (redondeando [FLOOR] para obtener hasta 2 decimales)
        String st_newTotal = addValues(st_total,st_newValue);

        // Si el total en la billetera no alcanza para pagar la compra total...
        if ( isGreaterThanTotalWallet(st_newTotal) ) {

            // Aviso que el dinero es insuficiente y descarto la suma
            Snackbar.make(findViewById(R.id.myCoordinatorLayout),R.string.insufficient_funds,Snackbar.LENGTH_LONG).show();

        } else {

            // Si alcanza, agrego el importe al total de la compra
            st_total = st_newTotal;

            // Actualiza el valor total actual en la vista
            TextView textView = findViewById(R.id.textView);
            textView.setText(getString(R.string.total_value) + st_total);
        }

        // Blanquea el valor a ingresar en la vista
        resetEditTextValue();

        // Si el total es mayor a cero, habilito el botón para pagar
        Button payButton = (Button) findViewById(R.id.button3);
        if (isGreaterThanValueCero(st_total)) {
            payButton.setEnabled(true);
        }

    }


    /** Envía a la pantalla de confirmación de compra
     * */
    public void sendToOrderTotal(View view) {
        // En caso de que haya quedado un valor a ingresar, blanquea el valor al irse de la vista
        if (!(et_productValue.getText().toString().isEmpty())) et_productValue.setText(getString(R.string.empty_string));

        Intent intent = new Intent(this, OrderTotalActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), String.valueOf(st_total));
        startActivity(intent);
    }

    /**
    * Blanquea el valor a ingresar en la vista del EditText
    **/
    private void resetEditTextValue(){
        et_productValue.setText(getString(R.string.empty_string));
    }


    // TODO: Pasar a la Clase WalletManager
    public boolean isFloatFormatValid(String val) {

        boolean isValid = true;

        try {
            BigDecimal bd_val = new BigDecimal(val);
        } catch (NumberFormatException e) {
            isValid = false;
        }

        return isValid;
    }

    // TODO: Pasar a la Clase WalletManager
    public boolean isGreaterThanTotalWallet(String val) {

        boolean isGreater = false;

        //TODO: Cargar el valor real de la billetera
        String st_totalWallet = getString(R.string.value_20);

        // Instancio los BigDecimal
        BigDecimal bd_value = new BigDecimal(val);
        BigDecimal bd_total = new BigDecimal(st_totalWallet);

        //Comparo los valores: Devuelve 1 si "bd_value" > "bd_total" )
        if (bd_value.compareTo(bd_total) == 1) {
            isGreater = true;
        }

        return isGreater;
    }

    // TODO: Pasar a la Clase WalletManager
    public boolean isValueAGreaterThanValueB(String valueA, String valueB) {

        boolean isGreater = false;

        // Instancio los BigDecimal
        BigDecimal bd_value = new BigDecimal(valueA);
        BigDecimal bd_total = new BigDecimal(valueB);

        //Comparo los valores: Devuelve 1 si "bd_value" > "bd_total" )
        if (bd_value.compareTo(bd_total) == 1) {
            isGreater = true;
        }

        return isGreater;
    }

    // TODO: Pasar a la Clase WalletManager
    public boolean isGreaterThanValueCero(String value) {
        return isValueAGreaterThanValueB(value,BigDecimal.ZERO.toPlainString());
    }

    // TODO: Pasar a la Clase WalletManager
    public String addValues(String val_1, String val_2){

        // Instancio los BigDecimal
        BigDecimal bd_v1 = new BigDecimal(val_1);
        BigDecimal bd_v2 = new BigDecimal(val_2);

        // Formato: Dejo (a lo sumo) dos decimales del número ingresado, truncando el resto
        bd_v1 = bd_v1.setScale(2, RoundingMode.FLOOR);
        bd_v2 = bd_v2.setScale(2, RoundingMode.FLOOR);

        // Sumo los valores
        BigDecimal result = bd_v1.add(bd_v2);

        // devuelvo el resultado en un String
        return result.toPlainString();
    }

}
