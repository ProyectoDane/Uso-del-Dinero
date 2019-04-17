package proyectodane.usodeldinero;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import static proyectodane.usodeldinero.MainTabActivity.SHOP_ORDER_TOTAL_FRAGMENT_ID;


public class BasketFragment extends Fragment implements OnClickListener {

    /**
     * Valor total de la compra
     * */
    private String st_total_purchase;

    /**
     * EditText que registra los valores ingresados por producto
     * */
    private EditText et_productValue;

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private OnShopFragmentChangeListener listener;


    public BasketFragment() { }


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

        rootView = inflater.inflate(R.layout.fragment_basket, container, false);

        // Obtiene el ID del EditText del valor ingresado
        et_productValue = (EditText) rootView.findViewById(R.id.editText);

        // Al iniciar, seteo los valores y los muestro e la vista
        resetValuesAndViews();

        // Asigno los botones a escuchar
        Button addProductButton = (Button) rootView.findViewById(R.id.button2);
        addProductButton.setOnClickListener(this);
        Button buyButton = (Button) rootView.findViewById(R.id.button3);
        buyButton.setOnClickListener(this);

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
            case R.id.button2:
                addToTotal(view);
                break;
            case R.id.button3:
                sendToOrderTotal();
                break;
        }
    }


    /**
     * Suma el valor ingresado al total
     * */
    public void addToTotal(View view) {

        // Obtiene el nuevo valor ingresado
        String st_newValue = et_productValue.getText().toString();

        // Verifico si tiene formato numérico inválido
        if ( !(WalletManager.getInstance().isFloatFormatValid(st_newValue)) ) {
            resetEditTextValue();
            return;
        }

        // Suma el nuevo valor al total (redondeando [FLOOR] para obtener hasta 2 decimales)
        String st_newTotal = WalletManager.getInstance().addValues(st_total_purchase,st_newValue);

        // Si el total en la billetera no alcanza para pagar la compra total...
        if ( WalletManager.getInstance().isGreaterThanTotalWallet(getActivity(),st_newTotal) ) {

            // Aviso que el dinero es insuficiente y descarto la suma
            Snackbar.make(rootView.findViewById(R.id.coordinatorLayout_Basket),R.string.insufficient_funds,Snackbar.LENGTH_LONG).show();

        } else {

            // Si alcanza, agrego el importe al total de la compra
            st_total_purchase = st_newTotal;

            // Actualiza el valor total actual en la vista
            updateTotalPurchase();
        }

        // Blanquea el valor a ingresar en la vista
        resetEditTextValue();

        // Si el total es mayor a cero, habilito el botón para pagar
        if (WalletManager.getInstance().isGreaterThanValueZero(st_total_purchase)) {
            payButtonSetEnabled(true);
        }

    }


    /**
     * Actualiza la vista de "compra total"
     * */
    private void updateTotalPurchase() {
        TextView textView = rootView.findViewById(R.id.textView);
        textView.setText(getString(R.string.total_purchase) + st_total_purchase);
    }


    /**
     * Envía a la pantalla de confirmación de compra
     * */
    public void sendToOrderTotal() {

        // Guardo los datos para mandarlo al próximo Fragment
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.tag_total_value), String.valueOf(st_total_purchase));

        // Llamo al listener y le envío los datos del fragment a llamar y los datos en el bundle
        listener.changeFragment(SHOP_ORDER_TOTAL_FRAGMENT_ID, bundle);

    }


    /**
     * Blanquea el valor a ingresar en la vista del EditText
     **/
    private void resetEditTextValue(){
        et_productValue.setText(getString(R.string.empty_string));
    }


    /**
     * Actualiza el valor total ahorrado en la billetera
     **/
    public void refreshTotalInWallet() {
        TextView textView = rootView.findViewById(R.id.textView14);
        String newText = getString(R.string.saved_money_pesos) + WalletManager.getInstance().obtainTotalCreditInWallet(getActivity());
        textView.setText(newText);
    }


    /**
     * Interface para cambiar de fragmento en el tab de compra
     * */
    private void payButtonSetEnabled(boolean status){
        Button payButton = (Button) rootView.findViewById(R.id.button3);
        payButton.setEnabled(status);
    }


    /**
     * Actualizo los valores y la vista de los mismos
     */
    public void resetValuesAndViews(){

        // Seteo el total de compra en cero
        st_total_purchase = getString(R.string.value_0);

        // Obtengo el saldo en billetera y lo muestro actualizado en la vista
        refreshTotalInWallet();

        // Actualizo el valor de compra total en la vista
        updateTotalPurchase();
    }


    /**
     * Declaración de Interface para cambiar de fragmento en el tab de compra
     * */
    public interface OnShopFragmentChangeListener {
        void changeFragment(int idNewFragment, Bundle bundle);
    }


    /**
     * Muestra el texto de ayuda para este fragment
     **/
    public void showHelp() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.basket_fragment_title_help))
                .setMessage(R.string.basket_fragment_help)
                .setPositiveButton(getString(android.R.string.ok),null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
