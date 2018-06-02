package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BasketActivity extends AppCompatActivity {

    Double d_total;
    EditText productValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        d_total = 0.0;
        productValue = (EditText) findViewById(R.id.editText); // Obtiene el ID del EditText del valor ingresado
    }


    /** Envía a la pantalla principal */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    /** Suma el valor ingresado al total */
    public void addToTotal(View view) {

        String st_total;
        Double d_value;

        // Obtiene el valor ingresado, comprobando el formato válido
        try {
            d_value = Double.valueOf(productValue.getText().toString());
        } catch (NumberFormatException e) {
            d_value = 0.0;
        }

        // TODO: Ver el redondeo o truncamiento que se hace al ingresar importes. Ver si limito la cantidad de dígitos enteros
        // TODO: Ideal -> Solo permitir hasta 2 decimales, y una cantidad máxima de N enteros.
        // Suma el valor al total anterior, redondeando para obtener hasta 2 decimales
        d_value = Math.floor(d_value * 100) / 100;
        d_total = d_total + d_value;
        d_total = Math.floor(d_total * 100) / 100;

        // Convierte el valor a string
        st_total = getString(R.string.total_value) + String.valueOf(d_total); // Agrego el texto completo

        // Actualiza el valor total actual en la vista
        TextView textView = findViewById(R.id.textView);
        textView.setText(st_total);

        // Blanquea el valor a ingresar en la vista
        productValue.setText(getString(R.string.empty_string));

        // Si el total es mayor a cero, habilito el botón para pagar
        Button payButton = (Button) findViewById(R.id.button3);
        if (d_total > 0.0) payButton.setEnabled(true);

    }


    /** Envía a la pantalla de confirmación de compra
     * */
    public void sendToOrderTotal(View view) {
        // Blanquea el valor a ingresar al irse de la vista, en caso de que haya quedado un valor a ingresar
        if (!(productValue.getText().toString().isEmpty())) productValue.setText(getString(R.string.empty_string));

        Intent intent = new Intent(this, OrderTotalActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), String.valueOf(d_total));
        startActivity(intent);
    }


}
