package proyectodane.usodeldinero;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import static proyectodane.usodeldinero.MainTabActivity.SHOP_CONTROL_CHANGE_FRAGMENT_ID;
import static proyectodane.usodeldinero.MainTabActivity.SHOP_FINALIZE_PURCHASE_FRAGMENT_ID;


public class PayPurchaseFragment extends Fragment implements OnClickListener{


    /**
     * Valor total de la compra
     */
    private String totalPurchase;

    /**
     * ArrayList con todos los valores de billetes/monedas calculados para dar el pago
     */
    private ArrayList<String> moneyValueNames;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private BasketFragment.OnShopFragmentChangeListener listener;


    public PayPurchaseFragment() { }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof BasketFragment.OnShopFragmentChangeListener) {
            listener = (BasketFragment.OnShopFragmentChangeListener) context;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pay_purchase, container, false);

        // Obtengo los valores de los argumentos
        Bundle bundle = getArguments();
        totalPurchase = bundle.getString(getString(R.string.tag_total_value));

        // Actualizo el valor de pago total
        TextView tv_totalPurchase = rootView.findViewById(R.id.textView4);
        String st_totalPurchase = getString(R.string.total_payment_of_the_purchase) + totalPurchase;
        tv_totalPurchase.setText(st_totalPurchase);

        // Calculo todos los valores a usar para pagar
        moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesOfPayment(getActivity(), totalPurchase);

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_pay),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_pay),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

        // Asigno los botones a escuchar
        Button purchasePayedButton = (Button) rootView.findViewById(R.id.button8);
        purchasePayedButton.setOnClickListener(this);

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
            case R.id.button8:
                sendToControlChange();
                break;

        }
    }


    /**
     *  Envía a la pantalla de control de vuelto.
     *  Si no corresponde vuelto, envía directamente a finalizar la compra
     **/
    public void sendToControlChange() {

        if ( WalletManager.getInstance().isChangeExpected(getActivity(), totalPurchase) ) {

            // Guardo los datos para mandarlo al próximo Fragment
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.tag_total_value),totalPurchase);

            // Llamo al listener y le envío los datos del fragment a llamar y los datos en el bundle
            listener.changeFragment(SHOP_CONTROL_CHANGE_FRAGMENT_ID, bundle);

        } else {

            sendToFinalizePurchase();

        }
    }


    /**
     *  Envía a la pantalla de finalización de la compra (para los casos donde el vuelto es nulo)
     **/
    public void sendToFinalizePurchase() {

        // Creo un array vacío (que representa un vuelto nulo)
        ArrayList<String> al_receivedChangeEmpty = new ArrayList<String>();

        // Guardo los datos para mandarlo al próximo Fragment
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(getString(R.string.received_change),al_receivedChangeEmpty);
        bundle.putString(getString(R.string.tag_total_value),totalPurchase);

        // Llamo al listener y le envío los datos del fragment a llamar y los datos en el bundle
        listener.changeFragment(SHOP_FINALIZE_PURCHASE_FRAGMENT_ID, bundle);

    }


    // TODO: Ver si se usa
//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_PayPurchase),getString(R.string.help_text_pay_purchase),10);
//    }


}
