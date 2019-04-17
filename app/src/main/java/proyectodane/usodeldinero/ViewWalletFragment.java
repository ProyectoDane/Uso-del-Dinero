package proyectodane.usodeldinero;


import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class ViewWalletFragment extends Fragment {

    /**
     * Vista instanciada
     */
    View rootView;

    /**
     * Slide Manager dedicado a mostrar las imágenes de billetes y los puntos indicadores
     */
    ImageSlideManager imageSlideManager;


    public ViewWalletFragment(){ }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_wallet, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Actualizo los componentes visuales del fragment
        refreshTotalText();

        // Cargo las imágenes de los billetes y la línea de puntos
        loadNewImages();

    }


    /**
     * Actualizo los componentes visuales del fragment
     */
    public void updateView(){

        // Actualizo el texto de valor del total en billetera
        refreshTotalText();

        // Actualizo todas las imágenes dentro del Slide
        updateImages();

    }


    /**
     * Actualiza el valor de la carga y del total
     **/
    private void refreshTotalText() {
        String newTotal = WalletManager.getInstance().obtainTotalCreditInWallet(getActivity());
        TextView textView = rootView.findViewById(R.id.textView1);
        String newText = getString(R.string.saved_money_pesos) + newTotal;
        textView.setText(newText);
    }


    /**
     * Cargo por primera vez todas la imágenes a mostrar, instanciando el imageSlideManager
     **/
    private void loadNewImages(){

        // Calculo todos los valores en la billetera a mostrar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesInWallet(getActivity());

        // Clase que se encarga de manejar lo referido al slide de imágenes y puntos
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager = new ImageSlideManager(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_main),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_main),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

    }


    /**
     * Actualizo todas la imágenes a mostrar del imageSlideManager creado
     **/
    private void updateImages(){

        // Calculo todos los valores en la billetera a mostrar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesInWallet(getActivity());


        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        imageSlideManager.setUpImages(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_main),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_main),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

    }


    /**
     * Muestra el texto de ayuda para este fragment
     **/
    public void showHelp() {

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.view_wallet_fragment_title_help))
                .setMessage(R.string.view_wallet_fragment_help)
                .setPositiveButton(getString(android.R.string.ok),null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
