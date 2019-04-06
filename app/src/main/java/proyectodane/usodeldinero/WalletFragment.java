package proyectodane.usodeldinero;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import static proyectodane.usodeldinero.MainTabActivity.WALLET_FRAGMENT_ID;


public class WalletFragment extends Fragment implements OnClickListener {

    /**
     * Importe total a de la billetera, con la suma de la carga
     */
    private String st_total;

    /**
     * Importe a cargar
     */
    private String st_subtotal;

    /**
     * ArrayList con todos los valores de billetes/monedas a cargar en la billetera
     */
    private ArrayList<String> newLoadMoneyValueNames;

    /**
     * Clase que se encarga de manejar lo referido al slide de imágenes y puntos
     */
    private ImageSlideManager imageSlideManager;

    /**
     * Clase que se encarga de manejar los mensajes emergentes
     */
    private SnackBarManager sb;

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Instancia del observador OnFragmentInteractionListener
     */
    private OnFragmentInteractionListener listener;


    public WalletFragment() { }


    // Agrego el Listener
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if( context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }

    }


    // Llamado en la parte inicial de la creación del Fragment.
    // Se inicializan objetos no gráficos.
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    // Llamado para usar el inflate con el layout del Fragment.
    // Se inicializa objetos gráficos
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);

        // Asigno los botones a escuchar
        Button addButton = (Button) rootView.findViewById(R.id.button14);
        addButton.setOnClickListener(this);
        Button saveButton = (Button) rootView.findViewById(R.id.button13);
        saveButton.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inicio la lista de valores a cargar en la billetera
        newLoadMoneyValueNames = new ArrayList<String>();

        // Inicio el SnackBarManager para luego crear mensajes emergentes
        sb = new SnackBarManager();

        // Actualizo el valor del total, inicio el subtotal en cero y muestro en pantalla
        initializeSubtotalAndLoadTotal();

        // Cargo las imágenes de los billetes y la línea de puntos
        loadNewImages();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button14:
                addValueToSubtotalTAB(view);
                break;
            case R.id.button13:
                if(addValuesToWallet(view)){ listener.updateFragments(WALLET_FRAGMENT_ID); }
                break;
        }
    }


    /**
     * Actualizo los componentes visuales del fragment
     */
    public void updateView(){

        // Actualizo el texto de valor del subtotal y total en billetera
        initializeSubtotalAndLoadTotal();

        // Actualizo todas las imágenes dentro del Slide
        updateImages();

    }


    /**
     * Cargo por primera vez todas la imágenes a mostrar, instanciando el imageSlideManager
     **/
    private void loadNewImages(){

        // Obtengo todos los valores a mostrar para la carga de la billetera
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesOfValidCurrency(getActivity());

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_wallet),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_wallet),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

    }


    /**
     * Actualizo todas la imágenes a mostrar del imageSlideManager creado
     **/
    private void updateImages() {

        // Obtengo todos los valores a mostrar para la carga de la billetera
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesOfValidCurrency(getActivity());

        // Cargo el slide de imágenes y puntos indicadores
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager.setUpImages(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_wallet),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_wallet),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

    }

    /**
     * Actualiza en la vista el valor de la carga y del total
     **/
    private void refreshSubtotalAndTotal() {
        TextView textView = rootView.findViewById(R.id.textView6);
        textView.setText(getString(R.string.load_cash_sign) + st_subtotal + " - " + getString(R.string.total_cash_sign) + st_total);
    }


    /**
     * Setea los valores en su estado inicial
     **/
    private void initializeSubtotalAndLoadTotal(){
        newLoadMoneyValueNames.clear();
        st_subtotal = getString(R.string.value_0);
        st_total = WalletManager.getInstance().obtainTotalCreditInWallet(getActivity());
        refreshSubtotalAndTotal();
    }


    /**
     * Sumo el valor seleccionado al subtotal de carga en billetera
     **/
    private void addValueToSubtotalTAB (View view) {

        // Obtengo el ID del valor elegido
        String st_valueID = imageSlideManager.getActualValueID();

        // Obtengo el valor monetario a partir del ID
        String st_value = WalletManager.getInstance().obtainValueFormID(getActivity(),st_valueID);

        // Agrego el ID a la lista para la futura carga, sumo el valor al subtotal y total para luego mostrarlo
        newLoadMoneyValueNames.add(st_valueID);
        st_subtotal = WalletManager.getInstance().addValues(st_value,st_subtotal);
        st_total = WalletManager.getInstance().addValues(st_value,st_total);
        refreshSubtotalAndTotal();

        // Creo el mensaje para notificar el valor seleccionado a sumar a la billetera y lo muestro
        String st_snackBarText = getString(R.string.value_selected_for_load) + st_value;
        sb.showTextShortOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Wallet),st_snackBarText,2);
    }


    /**
     * Agrega la carga de dinero seleccionada en la billetera
     * */
    private boolean addValuesToWallet (View view){

        if( !newLoadMoneyValueNames.isEmpty() ){

            // Guardo todos los valores seleccionados
            for(String currentNewLoadMoneyValueName : newLoadMoneyValueNames) {
                WalletManager.getInstance().addCurrencyInWallet(getActivity(),currentNewLoadMoneyValueName);
            }

            // Creo el mensaje para notificar valores guardados
            sb.showTextShortOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Wallet),getString(R.string.value_saved),2);

            // Vuelvo al estado inicial
            initializeSubtotalAndLoadTotal();

            return true;
        }

        return false;
    }


    /**
     * Declaración de Interface para actualizar las vistas de los fragmentos
     * */
    public interface OnFragmentInteractionListener {
        void updateFragments(int idFragmentCaller);
    }


//    //TODO: Confirmar borrado
//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        sb.showTextIndefiniteOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Wallet),getString(R.string.help_text_wallet),10);
//    }


}

