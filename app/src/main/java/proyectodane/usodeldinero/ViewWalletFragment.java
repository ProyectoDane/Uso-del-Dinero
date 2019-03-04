package proyectodane.usodeldinero;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class ViewWalletFragment extends Fragment implements ViewPager.OnPageChangeListener {

    /**
     * Cantidad de Tabs que tiene la activity
     */
    private static final int VIEW_WALLET_FRAGMENT_POSITION = 0;

    /**
     * Vista instanciada
     */
    View rootView;

    public ViewWalletFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_wallet, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Actualizo el texto de valor del total en billetera
        refreshTotalText();

        // Cargo las imágenes de ls billetes
        loadImages();

    }


    // TODo: Ver si se puede hacer que actualice solo cuando efectivamente cambiaron los datos
    @Override // Cuando se muestra este tab, actualizo la vista con los últimos datos
    public void onPageSelected(final int position) {
        if(position==VIEW_WALLET_FRAGMENT_POSITION){

            // Actualizo el texto de valor del total en billetera
            refreshTotalText();

            // Cargo las imágenes actualizadas de los billetes
            //loadImages(); TODO: Buscar alternativa a esto, ya que los billetes se van acumulando, no los sobre escribe

        }
    }

    @Override // Para cumplir con ViewPager.OnPageChangeListener
    public void onPageScrolled(final int position, final float positionOffset,
                               final int positionOffsetPixels) {
        // No se usa: Llama múltiples veces cuando hago un solo scroll
    }

    @Override // Para cumplir con ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(final int state) {
        // No se usa: Llama múltiples veces y no tiene la posición
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

    private void loadImages(){

        // Calculo todos los valores en la billetera a mostrar
        ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesInWallet(getActivity());

        // Clase que se encarga de manejar lo referido al slide de imágenes y puntos
        // Parámetros:  + (1)Contexto
        //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
        //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
        ImageSlideManager imageSlideManager = new ImageSlideManager(getActivity(),
                (ViewPager) rootView.findViewById(R.id.pager_main),
                getActivity().getSupportFragmentManager(),
                moneyValueNames,
                (LinearLayout) rootView.findViewById(R.id.SliderDots_main),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot),
                ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

    }


//    TODO: Ver como se re implementa
//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Main),getString(R.string.help_text_main),7);
//    }

}
