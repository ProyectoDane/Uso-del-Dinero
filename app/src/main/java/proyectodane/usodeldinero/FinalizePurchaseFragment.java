package proyectodane.usodeldinero;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import static proyectodane.usodeldinero.MainTabActivity.BASKET_FRAGMENT_ID;
import static proyectodane.usodeldinero.MainTabActivity.SHOP_FRAGMENT_ID;


public class FinalizePurchaseFragment extends Fragment implements OnClickListener {

    /**
     * Valor total de la compra
     */
    private String st_totalPurchase;

    /**
     * Vuelto recibido de la compra, en forma de listado de String
     */
    private ArrayList<String> al_receivedChange;

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private BasketFragment.OnShopFragmentChangeListener shopFragmentChangeListener;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private WalletFragment.OnFragmentInteractionListener fragmentInteractionListener;

    public FinalizePurchaseFragment() { }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof BasketFragment.OnShopFragmentChangeListener) {
            shopFragmentChangeListener = (BasketFragment.OnShopFragmentChangeListener) context;
        }

        if(context instanceof WalletFragment.OnFragmentInteractionListener) {
            fragmentInteractionListener = (WalletFragment.OnFragmentInteractionListener) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_finalize_purchase, container, false);

        // Obtengo los valores de los argumentos,  extraigo el listado de los billetes recibidos (cambio) y el valor de la compra
        Bundle bundle = getArguments();
        al_receivedChange = bundle.getStringArrayList(getString(R.string.received_change));
        st_totalPurchase = bundle.getString(getString(R.string.tag_total_value));

        // Si no recibí vuelto, cambio el comportamiento de los botones
        if (al_receivedChange.isEmpty()) {

            // Dejo inhabilitado el botón de "Guardar vuelto" y aclaro que no hubo vuelto
            Button saveToWalletButton = (Button) rootView.findViewById(R.id.button13);
            saveToWalletButton.setEnabled(false);
            saveToWalletButton.setText(getString(R.string.no_change_received));

            // Pongo un nuevo texto para finalizar la compra
            Button doNotSaveToWalletButton = (Button) rootView.findViewById(R.id.button11);
            doNotSaveToWalletButton.setText(getString(R.string.finalize_purchase));

        }

        // Asigno los botones a escuchar
        Button saveToWalletButton = (Button) rootView.findViewById(R.id.button13);
        saveToWalletButton.setOnClickListener(this);
        Button doNotSaveToWalletButton = (Button) rootView.findViewById(R.id.button11);
        doNotSaveToWalletButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        shopFragmentChangeListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button13:
                saveToWallet();
                break;
            case R.id.button11:
                doNotSaveToWallet();
                break;
        }
    }

    /**
     * Concreta el pago, descartando los valores correspondientes de la billetera y regresa a la pantalla principal
     * */
    private void doNotSaveToWallet() {
        WalletManager.getInstance().removeFromWalletCurrencyUsedToPay(getActivity(),st_totalPurchase);
        sendToBasket();
    }

    /**
     * Concreta el pago, descartando los valores correspondientes de la billetera,
     * guarda todos los valores recibidos en concepto de vuelto y regresa a la pantalla principal
     * */
    private void saveToWallet() {
        WalletManager.getInstance().removeFromWalletCurrencyUsedToPay(getActivity(),st_totalPurchase);
        WalletManager.getInstance().saveChangeInWallet(getActivity(),al_receivedChange);
        sendToBasket();
    }

    /**
     * Envío cambio al fragment Basket
     * */
    private void sendToBasket(){

        // Genero un bundle vacío, solo para cumplir con ChangeFragment()
        Bundle emptyBundle = new Bundle();

        // Llamo al shopFragmentChangeListener y le envío los datos del fragment a llamar con el bundle vacío
        shopFragmentChangeListener.changeFragment(BASKET_FRAGMENT_ID, emptyBundle);

        // Llamo al shopFragmentChangeListener y le envío los datos del fragment a llamar con el bundle vacío
        fragmentInteractionListener.updateFragments(SHOP_FRAGMENT_ID);
    }

}
