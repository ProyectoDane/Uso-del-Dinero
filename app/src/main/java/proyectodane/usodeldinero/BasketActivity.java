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

    /** Valor total guardado en la billetera
     * */
    Double d_walletTotal;

    /** Valor total de la compra
     * */
    Double d_total;

    /** EditText que registra los valores ingresados por producto
     * */
    EditText et_productValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        d_total = 0.0;
        et_productValue = (EditText) findViewById(R.id.editText); // Obtiene el ID del EditText del valor ingresado
        d_walletTotal = 20.0; // TODO: Cargar el valor real de la billetera
    }


    /** Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    /** Suma el valor ingresado al total
     * */
    public void addToTotal(View view) {

        String st_total;
        Double d_newValue;

        // Obtiene el valor ingresado, comprobando el formato válido
        try {
            d_newValue = Double.valueOf(et_productValue.getText().toString());
        } catch (NumberFormatException e) {
            d_newValue = 0.0;
        }

        // TODO: Ver el redondeo o truncamiento que se hace al ingresar importes, y si limito la cantidad de dígitos enteros
        // TODO: Ideal -> Solo permitir hasta 2 decimales, y una cantidad máxima de N enteros.

        // Suma el valor al total anterior, redondeando para obtener hasta 2 decimales
        d_newValue = Math.floor(d_newValue * 100) / 100;
        Double d_newTotal = d_total + d_newValue;
        d_newTotal = Math.floor(d_newTotal * 100) / 100;

        // TODO: Calcular si el total en la billetera alcanza para pagar la compra
        // Si el total en la billetera no alcanza para pagar la compra total,
        // descarto el producto que se está por agregar y aviso que el dinero es insuficiente.
        // Caso contrario, agrego el importe al total de la compra
        if (d_newTotal>d_walletTotal){
            Snackbar.make(findViewById(R.id.myCoordinatorLayout),R.string.insufficient_funds,Snackbar.LENGTH_LONG).show();
        } else {
            d_total = d_newTotal;

            // Convierte el valor a string
            st_total = getString(R.string.total_value) + String.valueOf(d_total); // Agrego el texto completo

            // Actualiza el valor total actual en la vista
            TextView textView = findViewById(R.id.textView);
            textView.setText(st_total);

        }

        // Blanquea el valor a ingresar en la vista
        et_productValue.setText(getString(R.string.empty_string));

        // Si el total es mayor a cero, habilito el botón para pagar
        Button payButton = (Button) findViewById(R.id.button3);
        if (d_total > 0.0) payButton.setEnabled(true);

    }


    /** Envía a la pantalla de confirmación de compra
     * */
    public void sendToOrderTotal(View view) {
        // Blanquea el valor a ingresar al irse de la vista, en caso de que haya quedado un valor a ingresar
        if (!(et_productValue.getText().toString().isEmpty())) et_productValue.setText(getString(R.string.empty_string));

        Intent intent = new Intent(this, OrderTotalActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), String.valueOf(d_total));
        startActivity(intent);
    }


}
