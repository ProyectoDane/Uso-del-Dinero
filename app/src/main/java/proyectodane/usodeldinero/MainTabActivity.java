package proyectodane.usodeldinero;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
     */
    private static final int OFF_SCREEN_PAGE_LIMIT = 4;

    /**
     * Constantes para identificar a cada uno de los fragment
     */
    public static final int VIEW_WALLET_FRAGMENT_ID = 0;
    public static final int SHOP_FRAGMENT_ID = 1;
    public static final int WALLET_FRAGMENT_ID = 2;
    public static final int BASKET_FRAGMENT_ID = 10;
    public static final int ORDER_TOTAL_FRAGMENT_ID = 11;
    public static final int PAY_PURCHASE_FRAGMENT_ID = 12;
    public static final int CONTROL_CHANGE_FRAGMENT_ID = 13;
    public static final int FINALIZE_PURCHASE_FRAGMENT_ID = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        // Creo la barra de herramientas
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Establece la barra de herramientas como la barra de app de la actividad
        setSupportActionBar(toolbar);

        // Ver si hace falta. Activa el botón "up" (hay que setear el " android:parentActivityName" para usar esto)
        //ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

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

        // Seteo los Listener de del ViewPager y TabLayout cruzados, para que cuando uno se mueve, el otro lo siga
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
    // TODO: ...según el fragment en vista. Además se podría agregar un botón "Inicio" para que vaya
    // TODO: ...al sector inicial del tab en donde se encuentra parado (Esto sirve para reemplazar los botones de "Cancelar")
    /**
     * Cuando se elige una opción del menú, aquí se maneja el comportamiento
     * a tomar según la opción elegida
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Obtengo el ID del item elegido, Verifico el ID y acciono en consecuencia
        switch (item.getItemId()) {
            case R.id.action_version_info:
                // Ejecuto según el caso
                return true;

            // Mas casos...
            //case R.id.action_favorite:
            // En caso de tener una acción "favoritos", ejecuto según el caso
            //    return true;

            // Si llega acá, la acción del usuario no fue reconocida.
            // Invoca a la super clase para manejarlo
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        // No realiza acción, para obligar al usuario a tomar la decisión a través de los botones de la aplicación
    }

    // Implemento OnFragmentInteractionListener para accionar cuando un fragment avisa sobre cambios en la visa
    @Override
    public void updateFragments(int idFragmentCaller){
        mSectionsPagerAdapter.updateSections(idFragmentCaller);
    }

    // Implemento OnShopFragmentChangeListener para accionar cuando un fragment avisa sobre cambio de Fragment de shop
    @Override
    public void changeFragment(int idNewFragment, Bundle bundle){
        mSectionsPagerAdapter.changeFragmentInShopTab(idNewFragment,bundle);
    }

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
                        //
                        break;
                }
            }
        }

        // Es llamado para instanciar el fragmento de la posición dada.
        @Override
        public Fragment getItem(int position) { return fragments.get(position); }

        // Muestra la cantidad total de tabs en el Adapter.
        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }

        @Override
        public int getItemPosition(Object object){

            // Casos donde no necesito actualizar
            if (object instanceof ViewWalletFragment) return POSITION_UNCHANGED;
            if (object instanceof WalletFragment) return POSITION_UNCHANGED;

            // En teoría solo necesitaría "return POSITION_NONE" para el caso del tab shop, porque tiene que actualizar el objeto
            return POSITION_NONE;
        }


        // TODO: Agregar los casos cuando en la configuración (alta/baja de valores) se afecta la billetera (Podría usarse un nuevo valor)
        // Implemento reacción del OnFragmentInteractionListener para accionar cuando un fragment avisa
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

                default:
                    //
                    break;
            }


        }

        // Actualizo el fragment perteneciente al Tab shop, según el ID solicitado
        // Ciclo de compra:
        // Basket -> OrderTotal -> PayPurchase -> ControlChange -> FinalizePurchase -> Basket
        public void changeFragmentInShopTab(int idNewFragment, Bundle bundle) {

            switch (idNewFragment) {

                case BASKET_FRAGMENT_ID:
                    fragments.set(SHOP_FRAGMENT_ID,new BasketFragment());
                    break;

                case ORDER_TOTAL_FRAGMENT_ID:
                    Fragment newOrderTotalFragment = new OrderTotalFragment();
                    newOrderTotalFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newOrderTotalFragment);
                    break;

                case PAY_PURCHASE_FRAGMENT_ID:
                    Fragment newPayPurchaseFragment = new PayPurchaseFragment();
                    newPayPurchaseFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newPayPurchaseFragment);
                    break;

                case CONTROL_CHANGE_FRAGMENT_ID:
                    Fragment newControlChangeFragment = new ControlChangeFragment();
                    newControlChangeFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newControlChangeFragment);
                    break;

                case FINALIZE_PURCHASE_FRAGMENT_ID:
                    Fragment newFinalizePurchaseFragment = new FinalizePurchaseFragment();
                    newFinalizePurchaseFragment.setArguments(bundle);
                    fragments.set(SHOP_FRAGMENT_ID,newFinalizePurchaseFragment);
                    break;

                default:
                    //
                    break;
            }

            // Notifico el cambio para que luego se llame a getItemPosition()
            notifyDataSetChanged();

        }

    }


}


