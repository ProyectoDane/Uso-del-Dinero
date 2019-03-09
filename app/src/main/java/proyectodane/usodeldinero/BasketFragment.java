package proyectodane.usodeldinero;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import proyectodane.usodeldinero.WalletFragment.OnFragmentInteractionListener;


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
    private OnFragmentInteractionListener listener;

    public BasketFragment() { }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if( context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_basket, container, false);

        // Al iniciar, seteo el total en cero
        st_total_purchase = getString(R.string.value_0);

        // Obtiene el ID del EditText del valor ingresado
        et_productValue = (EditText) rootView.findViewById(R.id.editText);

        // Obtengo el saldo en billetera
        refreshTotalInWallet();

        // Actualizo el valor de compra total
        updateTotalPurchase();

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
                // Implementar
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
            Snackbar.make(rootView.findViewById(R.id.coordinatorLayout_Basquet),R.string.insufficient_funds,Snackbar.LENGTH_LONG).show();

        } else {

            // Si alcanza, agrego el importe al total de la compra
            st_total_purchase = st_newTotal;

            // Actualiza el valor total actual en la vista
            updateTotalPurchase();
        }

        // Blanquea el valor a ingresar en la vista
        resetEditTextValue();

        // Si el total es mayor a cero, habilito el botón para pagar
        Button payButton = (Button) rootView.findViewById(R.id.button3);
        if (WalletManager.getInstance().isGreaterThanValueZero(st_total_purchase)) {
            payButton.setEnabled(true);
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
    public void sendToOrderTotal(View view) {
        // En caso de que haya quedado un valor a ingresar, blanquea el valor al irse de la vista
        if (!(et_productValue.getText().toString().isEmpty())) et_productValue.setText(getString(R.string.empty_string));

        Intent intent = new Intent(getActivity(), OrderTotalActivity.class);
        intent.putExtra(getString(R.string.tag_total_value), String.valueOf(st_total_purchase));
        startActivity(intent);
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

    // TODO: Ver implementación
//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Basquet),getString(R.string.help_text_basket),7);
//    }

}
