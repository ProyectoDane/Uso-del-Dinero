package proyectodane.usodeldinero;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import proyectodane.usodeldinero.WalletFragment.OnFragmentInteractionListener;
import proyectodane.usodeldinero.BasketFragment.OnShopFragmentChangeListener;


public class MainTabActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnShopFragmentChangeListener {

    /**
     * Es el PageAdapter que proveerá Fragments por cada una de las secciones (tabs).
     * Es un derivado de FragmentPagerAdapter el cual conservará cada Fragment en memoria.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * El ViewPager que contendrá al Fragment obtenido
     */
    private ViewPager mViewPager;

    /**
     * El TabLayout que contendrá los tabs generados
     */
    TabLayout tabLayout;

    /**
     * Cantidad de Tabs que tiene la activity
     */
    private static final int NUMBER_OF_TABS = 3;

    /**
     * Cantidad de páginas fuera de de la vista de la pantalla que quedan cargadas
     * Con (NUMBER_OF_TABS - 1) todas las pantallas restantes no vistas, quedan cargadas
     */
    private static final int OFF_SCREEN_PAGE_LIMIT = NUMBER_OF_TABS - 1;

    /**
     * Constantes para identificar a cada uno de los fragment
     */
    public static final int VIEW_WALLET_FRAGMENT_ID = 0;
    public static final int SHOP_FRAGMENT_ID = 1;
    public static final int WALLET_FRAGMENT_ID = 2;
    public static final int SHOP_BASKET_FRAGMENT_ID = 10;
    public static final int SHOP_ORDER_TOTAL_FRAGMENT_ID = 11;
    public static final int SHOP_PAY_PURCHASE_FRAGMENT_ID = 12;
    public static final int SHOP_CONTROL_CHANGE_FRAGMENT_ID = 13;
    public static final int SHOP_FINALIZE_PURCHASE_FRAGMENT_ID = 14;
    public static final int EXTERNAL_TO_TAB_ID = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        // Creo la barra de herramientas
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Establece la barra de herramientas como la barra de app de la actividad
        setSupportActionBar(toolbar);

        // Identifico el Layout del ViewPager
        mViewPager = (ViewPager) findViewById(R.id.container);

        // Identifico el Layout del Tab
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Crea el SectionsPagerAdapter que devolverá un Fragment por cada una de las secciones
        // También se ingresan como Listener del ViewPager y TabLayout a cada uno de los fragment que lo necesiten
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Seteo en el ViewPager al PagerAdapter
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Configuro las páginas fuera de pantalla que deben quedar en memoria,
        // para que no queden en blanco cuando me voy y vuelvo
        mViewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);

        // Seteo los Listener de del ViewPager y TabLayout cruzados, para que cuando uno se mueve el otro lo siga
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    // Llena el menú, agrega items al Actionbar si se encuentra presente
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }


    // TODO: Implementar. Acá se podría Implementar un único botón de Ayuda que reacciona...
    // TODO: ...según el fragment en vista (Si es necesario, sino directamente no agregarlo).
    // TODO: También se debería agregar un botón de "salir de la aplicación" para finalizarla
    /**
     * Cuando se elige una opción del menú, aquí se maneja el comportamiento
     * a tomar según la opción elegida
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Obtengo el ID del item elegido, Verifico el ID y acciono en consecuencia
        switch (item.getItemId()) {

            case R.id.action_home:

                // Llamo al fragment Basket, dentro del Tab shop. Agrego un bundle vacío, solo para cumplir con ChangeFragment()
                changeFragment(SHOP_BASKET_FRAGMENT_ID, new Bundle());

                // Actualizo todos los fragments de cada tab
                updateFragments(EXTERNAL_TO_TAB_ID);

                return true;

            case R.id.action_help:

                // Muestro la ayuda según la posición actual del ViewPager
                mSectionsPagerAdapter.showHelp(mViewPager.getCurrentItem());
                return true;

            case R.id.action_empty_wallet:

                // Inicio el proceso de vaciado de billetera
                emptyWallet();
                return true;

            case R.id.action_configuration:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_version_info:
                showVersionInfo();
                return true;

            // Si llega acá, la acción del usuario no fue reconocida. Invoca a la super clase para manejarlo
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        // No realiza acción, para obligar al usuario a tomar la decisión a través de los botones de la aplicación
    }


    // Implemento OnFragmentInteractionListener para accionar cuando un fragment...
    // ...avisa sobre cambios en la vista
    @Override
    public void updateFragments(int idFragmentCaller){
        mSectionsPagerAdapter.updateSections(idFragmentCaller);
    }


    // Implemento OnShopFragmentChangeListener para accionar cuando un
    // ...fragment avisa sobre cambios en el tab de compra
    @Override
    public void changeFragment(int idNewFragment, Bundle bundle){
        mSectionsPagerAdapter.changeFragmentInShopTab(idNewFragment,bundle);
    }


    /**
     * AlertDialog que muestra la versión de la aplicación
     */
    private void showVersionInfo(){
        new AlertDialog.Builder(this)
                .setView(getLayoutInflater().inflate(R.layout.layout_version_info,null))
                .show();
    }


    /**
     * AlertDialog usado para vaciar la billetera, previa confirmación.
     */
    private void emptyWallet(){

        final Context context = this;

        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.msg_question_empty_wallet))
                .setMessage(getString(R.string.msg_confirm_wallet_empty))

                // Especifico un Listener para permitir llevar a cabo acciones cuando se acepta
                .setPositiveButton(getString(R.string.msg_empty_wallet), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Vacío la billetera
                        WalletManager.getInstance().removeAllCurrencyFromWallet(context);

                        // Luego de vaciar, actualizo la vista de todos los fragment
                        updateFragments(EXTERNAL_TO_TAB_ID);

                        // Envío mensaje de información
                        new AlertDialog.Builder(context)
                                .setTitle(context.getString(R.string.msg_wallet_emptied))
                                .setPositiveButton(android.R.string.ok,null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }





    /**
     * ********************************************
     * ********* FragmentStatePageAdapter *********
     * ********************************************
     */

    /**
     * Una clase FragmentStatePagerAdapter que representa cada uno de los tabs dentro de un Fragment
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragments;

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new ArrayList<>();

            for (int i = 0; i < NUMBER_OF_TABS; i++){
                switch (i) {
                    case VIEW_WALLET_FRAGMENT_ID:
                        fragments.add(new ViewWalletFragment());
                        break;
                    case SHOP_FRAGMENT_ID:
                        fragments.add(new BasketFragment());
                        break;
                    case WALLET_FRAGMENT_ID:
                        fragments.add(new WalletFragment());
                        break;
                    default:

                        break;
                }
            }
        }


        // Es llamado para instanciar el fragmento de la posición dada.
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        // Muestra la cantidad total de tabs en el Adapter.
        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }


        // Usado para identificar los casos donde se debe actualizar las vistas de los tab,
        // según el fragment dado. getItemPosition() es llamado por notifyDataSetChanged()
        @Override
        public int getItemPosition(Object object){

            // Casos donde no necesito actualizar la vista
            if (object instanceof ViewWalletFragment) return POSITION_UNCHANGED;
            if (object instanceof WalletFragment) return POSITION_UNCHANGED;

            // Solo necesito "return POSITION_NONE" para el caso del tab shop, porque tiene que actualizar el fragment
            return POSITION_NONE;
        }


        // Implemento reacción del OnFragmentInteractionListener para accionar cuando un fragment avisa sobre cambios
        public void updateSections(int idFragmentCaller){

            switch (idFragmentCaller) {

                case VIEW_WALLET_FRAGMENT_ID:
                    // No realiza cambios que afecten a los demás.
                    break;

                case SHOP_FRAGMENT_ID:

                    ((ViewWalletFragment)fragments.get(VIEW_WALLET_FRAGMENT_ID)).updateView();
                    ((WalletFragment)fragments.get(WALLET_FRAGMENT_ID)).updateView();
                    break;

                case WALLET_FRAGMENT_ID:

                    ((ViewWalletFragment)fragments.get(VIEW_WALLET_FRAGMENT_ID)).updateView();

                    // En vez de actualizar la vista en este Tab, directamente instancio desde el principio
                    fragments.set(SHOP_FRAGMENT_ID,new BasketFragment());
                    notifyDataSetChanged();
                    break;


                case EXTERNAL_TO_TAB_ID:

                    ((ViewWalletFragment)fragments.get(VIEW_WALLET_FRAGMENT_ID)).updateView();
                    ((WalletFragment)fragments.get(WALLET_FRAGMENT_ID)).updateView();

                    // En vez de actualizar la vista en este Tab, directamente instancio desde el principio
                    fragments.set(SHOP_FRAGMENT_ID,new BasketFragment());
                    notifyDataSetChanged();
                    break;

                default:

                    break;
            }


        }


        // Actualizo el fragment perteneciente al Tab shop, según el ID solicitado
        // Ciclo de compra:
        // Basket -> OrderTotal -> PayPurchase -> ControlChange -> FinalizePurchase -> Basket
        public void changeFragmentInShopTab(int idNewFragment, Bundle bundle) {

            switch (idNewFragment) {

                case SHOP_BASKET_FRAGMENT_ID:
                    fragments.set(SHOP_FRAGMENT_ID,new BasketFragment());
                    break;

                case SHOP_ORDER_TOTAL_FRAGMENT_ID:
                    Fragment newOrderTotalFragment = new OrderTotalFragment();
                    newOrderTotalFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newOrderTotalFragment);
                    break;

                case SHOP_PAY_PURCHASE_FRAGMENT_ID:
                    Fragment newPayPurchaseFragment = new PayPurchaseFragment();
                    newPayPurchaseFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newPayPurchaseFragment);
                    break;

                case SHOP_CONTROL_CHANGE_FRAGMENT_ID:
                    Fragment newControlChangeFragment = new ControlChangeFragment();
                    newControlChangeFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newControlChangeFragment);
                    break;

                case SHOP_FINALIZE_PURCHASE_FRAGMENT_ID:
                    Fragment newFinalizePurchaseFragment = new FinalizePurchaseFragment();
                    newFinalizePurchaseFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newFinalizePurchaseFragment);
                    break;

                default:

                    break;
            }

            // Notifico el cambio para que luego se llame a getItemPosition()
            notifyDataSetChanged();

        }


        public void showHelp(int position){

            Fragment fragment = fragments.get(position);

            switch (position) {

                case VIEW_WALLET_FRAGMENT_ID:
                    ((ViewWalletFragment)fragment).showHelp();
                    break;

                case SHOP_FRAGMENT_ID:

                    if (fragment instanceof BasketFragment) ((BasketFragment)fragment).showHelp();
                    if (fragment instanceof OrderTotalFragment) ((OrderTotalFragment)fragment).showHelp();
                    if (fragment instanceof PayPurchaseFragment) ((PayPurchaseFragment)fragment).showHelp();
                    if (fragment instanceof ControlChangeFragment) ((ControlChangeFragment)fragment).showHelp();
                    if (fragment instanceof FinalizePurchaseFragment) ((FinalizePurchaseFragment)fragment).showHelp();
                    break;

                case WALLET_FRAGMENT_ID:
                    ((WalletFragment)fragment).showHelp();
                    break;

                default:
                    break;
            }


        }


    }


}


