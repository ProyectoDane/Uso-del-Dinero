package proyectodane.usodeldinero;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import proyectodane.usodeldinero.BasketFragment.OnShopFragmentChangeListener;
import static proyectodane.usodeldinero.MainTabActivity.PAY_PURCHASE_FRAGMENT_ID;


public class OrderTotalFragment extends Fragment implements OnClickListener {

    /**
     * Valor total de la compra
     */
    private String total;

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private OnShopFragmentChangeListener listener;

    public OrderTotalFragment() { }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof OnShopFragmentChangeListener) {
            listener = (OnShopFragmentChangeListener) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_total, container, false);

        // Obtengo los valores de los argumentos
        Bundle bundle = getArguments();
        total = bundle.getString(getString(R.string.tag_total_value));

        // Capturo el TextView y coloco el texto indicando el total de compra y total ahorrado
        TextView tv_purchase = rootView.findViewById(R.id.textView3);
        TextView tv_wallet = rootView.findViewById(R.id.textView11);
        String st_purchase = getString(R.string.total_purchase) + total;
        String st_wallet = getString(R.string.saved_money_pesos) + WalletManager.getInstance().obtainTotalCreditInWallet(getActivity());
        tv_purchase.setText(st_purchase);
        tv_wallet.setText(st_wallet);

        // Asigno los botones a escuchar
        Button addProductButton = (Button) rootView.findViewById(R.id.button6);
        addProductButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button6:
                sendToPayPurchase();
                break;

        }
    }


    /**
     * Envía a la pantalla de pago de la compra
     **/
    public void sendToPayPurchase() {

        // Guardo los datos para mandarlo al próximo Fragment
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.tag_total_value), total);

        // Llamo al listener y le envío los datos del fragment a llamar y los datos en el bundle
        listener.changeFragment(PAY_PURCHASE_FRAGMENT_ID, bundle);

    }

}
