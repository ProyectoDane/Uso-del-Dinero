package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrderTotalActivity extends AppCompatActivity {

    /**
     * Valor total de la compra
    */
    private String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_total);

        // Obtengo el intent que inició el activity y extraigo el valor del total
        Intent intent = getIntent();
        total = intent.getStringExtra(getString(R.string.tag_total_value));

        // Capturo el TextView y coloco el texto indicando el total de compra y total ahorrado
        TextView tv_purchase = findViewById(R.id.textView3);
        TextView tv_wallet = findViewById(R.id.textView11);
        String st_purchase = getString(R.string.total_purchase) + total;
        String st_wallet = getString(R.string.saved_money_pesos) + WalletManager.getInstance().obtainTotalCreditInWallet(this);
        tv_purchase.setText(st_purchase);
        tv_wallet.setText(st_wallet);

    }

    /**
     * Envía a la pantalla principal
     **/
    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Envía a la pantalla de pago de la compra
     **/
    public void sendToPayPurchase(View view) {
        Intent intent = new Intent(this, PayPurchaseActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), total);
        startActivity(intent);
    }

}
