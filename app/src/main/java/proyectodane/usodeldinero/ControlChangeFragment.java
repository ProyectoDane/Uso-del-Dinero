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

import static proyectodane.usodeldinero.MainTabActivity.FINALIZE_PURCHASE_FRAGMENT_ID;


public class ControlChangeFragment extends Fragment implements OnClickListener{


    /**
     * Valor total de la compra
     */
    private String st_totalPurchase;

    /**
     * Vuelto total de la compra, esperado
     */
    private String st_changeExpected;

    /**
     * Vuelto recibido de la compra (importe)
     */
    private String st_receivedChangeValue;

    /**
     * Vuelto recibido de la compra (todos los ID de cada uno de los valores)
     */
    private ArrayList<String> al_receivedChange;

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


    public ControlChangeFragment() { }

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

        rootView = inflater.inflate(R.layout.fragment_control_change, container, false);

        // Obtengo los valores de los argumentos, extraigo el valor del pago total e inicio las variables del vuelto
        Bundle bundle = getArguments();
        st_totalPurchase = bundle.getString(getString(R.string.tag_total_value));
        st_changeExpected = WalletManager.getInstance().expectedChangeValue(getActivity(),st_totalPurchase);
        st_receivedChangeValue = getString(R.string.value_0);
        al_receivedChange = new ArrayList<String>();

        // Actualizo el valor del vuelto total a recibir
        TextView tv_totalChange = rootView.findViewById(R.id.textView15);
        String st_totalChange = getString(R.string.total_change) + st_changeExpected;
        tv_totalChange.setText(st_totalChange);


        // ArrayList con todos los valores de billetes/monedas existentes. Calculo todos los valores a usar para pagar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().getInstance().obtainMoneyValueNamesOfValidCurrency(getActivity());

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_change),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_change),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

        // Asigno los botones a escuchar
        Button addToChangeButton = (Button) rootView.findViewById(R.id.button9);
        addToChangeButton.setOnClickListener(this);
        Button acceptChangeButton = (Button) rootView.findViewById(R.id.button10);
        acceptChangeButton.setOnClickListener(this);

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
            case R.id.button9:
                addToChange();
                break;
            case R.id.button10:
                sendToFinalizePurchase();
                break;
        }
    }


    /**
     *  Envía a la pantalla de finalización de la compra
     **/
    public void sendToFinalizePurchase() {

        // Guardo los datos para mandarlo al próximo Fragment
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(getString(R.string.received_change),al_receivedChange);
        bundle.putString(getString(R.string.tag_total_value),st_totalPurchase);

        // Llamo al listener y le envío los datos del fragment a llamar y los datos en el bundle
        listener.changeFragment(FINALIZE_PURCHASE_FRAGMENT_ID, bundle);
    }


    /**
     *  Actualizo el valor del vuelto recibido
     **/
    public void addToChange(){

        // Obtengo el ID del valor elegido y lo guardo en el ArrayList
        String st_valueID = imageSlideManager.getActualValueID();
        al_receivedChange.add(st_valueID);

        // Obtengo el valor monetario a partir del ID y lo sumo al vuelto actual
        String st_value = WalletManager.getInstance().obtainValueFormID(getActivity(),st_valueID);
        st_receivedChangeValue = WalletManager.getInstance().addValues(st_receivedChangeValue,st_value);

        // Actualizo el texto del importe recibido
        TextView textView = rootView.findViewById(R.id.textView5);
        String st_textViewValue = getString(R.string.change_received) + st_receivedChangeValue;

        // Si el vuelto es el total, lo informo
        if (isChangeOK()) {
            st_textViewValue = st_textViewValue + " " + getString(R.string.change_OK);
        }

        // Reflejo el cambio en el TextView
        textView.setText(st_textViewValue);

    }

    /**
     *  Verifico si el vuelto recibido es igual al vuelto total
     *  y actualizo la habilitación de botones en base al resultado
     **/
    private boolean isChangeOK(){

        Button acceptChangeButton = (Button) rootView.findViewById(R.id.button10);
        Button addToChangeButton = (Button) rootView.findViewById(R.id.button9);
        boolean changeOk = WalletManager.getInstance().isTotalChangeReceivedOk(st_receivedChangeValue, st_changeExpected);

        if (changeOk) {
            acceptChangeButton.setEnabled(true);
            addToChangeButton.setEnabled(false);
        }

        return changeOk;
    }

//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_ControlChange),getString(R.string.help_text_control_change),10);
//    }


}
