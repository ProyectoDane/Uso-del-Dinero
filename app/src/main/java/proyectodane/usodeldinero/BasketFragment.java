package proyectodane.usodeldinero;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import static proyectodane.usodeldinero.MainTabActivity.SHOP_ORDER_TOTAL_FRAGMENT_ID;


public class BasketFragment extends Fragment implements OnClickListener {

    /**
     * Valor total de la compra
     * */
    private String st_total_purchase;

    /**
     * Vista instanciada
     */
    private View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private OnShopFragmentChangeListener listener;


    /**
     * Cota superior de cantidad de dígitos del valor de un producto (incluye el separador de decimal)
     */
    private static final int MAX_CHARACTERS_IN_VALUE = 7;


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

        // Al iniciar, seteo los valores y los muestro e la vista
        resetValuesAndViews();

        // Asigno los botones a escuchar
        Button addProductButton = (Button) rootView.findViewById(R.id.button2);
        addProductButton.setOnClickListener(this);
        Button buyButton = (Button) rootView.findViewById(R.id.button3);
        buyButton.setOnClickListener(this);
        Button buttonNumber0 = (Button) rootView.findViewById(R.id.button_n0);
        buttonNumber0.setOnClickListener(this);
        Button buttonNumber1 = (Button) rootView.findViewById(R.id.button_n1);
        buttonNumber1.setOnClickListener(this);
        Button buttonNumber2 = (Button) rootView.findViewById(R.id.button_n2);
        buttonNumber2.setOnClickListener(this);
        Button buttonNumber3 = (Button) rootView.findViewById(R.id.button_n3);
        buttonNumber3.setOnClickListener(this);
        Button buttonNumber4 = (Button) rootView.findViewById(R.id.button_n4);
        buttonNumber4.setOnClickListener(this);
        Button buttonNumber5 = (Button) rootView.findViewById(R.id.button_n5);
        buttonNumber5.setOnClickListener(this);
        Button buttonNumber6 = (Button) rootView.findViewById(R.id.button_n6);
        buttonNumber6.setOnClickListener(this);
        Button buttonNumber7 = (Button) rootView.findViewById(R.id.button_n7);
        buttonNumber7.setOnClickListener(this);
        Button buttonNumber8 = (Button) rootView.findViewById(R.id.button_n8);
        buttonNumber8.setOnClickListener(this);
        Button buttonNumber9 = (Button) rootView.findViewById(R.id.button_n9);
        buttonNumber9.setOnClickListener(this);
        Button buttonDecimalSeparator = (Button) rootView.findViewById(R.id.button_decimal_separator);
        buttonDecimalSeparator.setOnClickListener(this);
        Button buttonErase = (Button) rootView.findViewById(R.id.button_erase);
        buttonErase.setOnClickListener(this);

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
                addToTotal();
                break;
            case R.id.button3:
                sendToOrderTotal();
                break;

            case R.id.button_n0:
                appendCharacterInValue(getString(R.string.number_0));
                break;

            case R.id.button_n1:
                appendCharacterInValue(getString(R.string.number_1));
                break;

            case R.id.button_n2:
                appendCharacterInValue(getString(R.string.number_2));
                break;

            case R.id.button_n3:
                appendCharacterInValue(getString(R.string.number_3));
                break;

            case R.id.button_n4:
                appendCharacterInValue(getString(R.string.number_4));
                break;

            case R.id.button_n5:
                appendCharacterInValue(getString(R.string.number_5));
                break;

            case R.id.button_n6:
                appendCharacterInValue(getString(R.string.number_6));
                break;

            case R.id.button_n7:
                appendCharacterInValue(getString(R.string.number_7));
                break;

            case R.id.button_n8:
                appendCharacterInValue(getString(R.string.number_8));
                break;

            case R.id.button_n9:
                appendCharacterInValue(getString(R.string.number_9));
                break;

            case R.id.button_decimal_separator:
                appendCharacterInValue(getString(R.string.decimal_separator));
                break;

            case R.id.button_erase:
                appendCharacterInValue(getString(R.string.erase_number));
                break;


        }
    }


    /**
     * Suma el valor ingresado al total
     * */
    private void addToTotal() {

        // Obtiene el nuevo valor ingresado
        String st_newValue = getProductValue();

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
    public void resetEditTextValue(){
        setProductValue(getString(R.string.empty_string));
    }


    /**
     * Escribe el valor del monto del producto con subrayado para indicar ingreso de datos
     **/
    public void setProductValue(String value) {
        TextView tv_productValue = rootView.findViewById(R.id.priceProductText);
        tv_productValue.setPaintFlags(tv_productValue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_productValue.setText(value);
    }


    /**
     * Obtiene el valor del monto del producto
     **/
    public String getProductValue() {
        TextView tv_productValue = rootView.findViewById(R.id.priceProductText);
        return tv_productValue.getText().toString();
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
    private void resetValuesAndViews(){

        // Seteo el valor del EditText en cero
        resetEditTextValue();

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
     * Recibo el valor enviado desde los botones y lo cargo en el EditText
     * */
    private void appendCharacterInValue(String character){

        // Si es el valor de borrar, vacío el EditText
        if (character.equals(getString(R.string.erase_number))){

            resetEditTextValue();

        }else { // Caso contrario, es un valor para ingresar

            // Si es el punto decimal y ya se ingresó antes, descarto esta entrada
            if( character.equals(getString(R.string.decimal_separator))
                    && (getProductValue().indexOf(getString(R.string.decimal_separator))) >= 0 ){
                 return;
            }

            // Si excede la longitud máxima determinada, descarto esta entrada
            if( getProductValue().length() >= MAX_CHARACTERS_IN_VALUE ) {
                return;
            }

            TextView et_productValue = (TextView) rootView.findViewById(R.id.priceProductText);
            et_productValue.append(character);
        }

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
