package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigurationActivity extends AppCompatActivity {

    Boolean emptyWalletRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        emptyWalletRequested = false;
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

    /**
     * Vacía la billetera, confirmando la acción antes
     * */
    public void emptyWallet(View view) {

        Button emptyWallet = (Button) findViewById(R.id.button32);

        if(!emptyWalletRequested){

            // Envío mensaje de confirmación
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_config),getString(R.string.msg_confirm_wallet_empty),5);

            // Cambio el texto del botón
            emptyWallet.setText(getString(R.string.confirm_empty_wallet));

            emptyWalletRequested = true;

        }else{

            // Vacío la billetera
            WalletManager.getInstance().removeAllCurrencyFromWallet(this);

            // Envío mensaje de información
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_config),getString(R.string.msg_wallet_emptied),5);

            // Inhabilito el botón de vaciar billetera
            emptyWallet.setEnabled(false);
        }
    }

}
