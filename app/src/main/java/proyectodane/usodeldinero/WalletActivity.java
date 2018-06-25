package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
    }

    /**
     * Cancela la carga en la billetera
     * */
    public void cancelLoad(View view) {
        sendToMain(view);
    }

    /**
     * Envía a la pantalla principal
     * */
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
