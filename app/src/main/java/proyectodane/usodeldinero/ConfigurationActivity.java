package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }

    /**
     * Envía a la pantalla de nuevo valor
     * */
    public void sendToNewCurrency(View view) {
        Intent intent = new Intent(this, NewCurrencyActivity.class);
        startActivity(intent);
    }

    /**
     * Envía a la pantalla de eliminar valor
     * */
    public void sendToDeleteCurrency(View view) {
        Intent intent = new Intent(this, DeleteCurrencyActivity.class);
        startActivity(intent);
    }


}
