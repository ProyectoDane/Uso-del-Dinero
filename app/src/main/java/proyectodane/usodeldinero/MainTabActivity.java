package proyectodane.usodeldinero;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class MainTabActivity extends AppCompatActivity {

    /**
     * Es el PageAdapter que proveerá Fragments por cada una de las secciones (tabs).
     * Es un derivado de FragmentPagerAdapter el cual conservará cada Fragment en memoria.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * El ViewPager que contendrá al Fragment obtenido
     */
    private ViewPager mViewPager;

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

        // Crea el SectionsPagerAdapter que devolverá un Fragment por cada una de las secciones
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Seteo el ViewPager con el PagerAdapter obtenido
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        // Identifico el Layout del Tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Seteo los Listener
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    // Llena el menú, agrega items al Actionbar si se encuentra presente
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }


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





    /**
     *****************************************************************************************************
     *****************************************************************************************************
     *****************************************************************************************************
     */









    /**
     * Una Clase Fragment conteniendo una vista
     */
    public static class WalletFragment extends Fragment {



        // ******* 1111 Prueba con la activity Wallet *******
        // ******* 1111 Prueba con la activity Wallet *******
        // ******* 1111 Prueba con la activity Wallet *******

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


        // ******* 1111 Prueba con la activity Wallet *******
        // ******* 1111 Prueba con la activity Wallet *******
        // ******* 1111 Prueba con la activity Wallet *******



        View rootView;
        Context myContext;
        AppCompatActivity myActivity;
        static FragmentManager myFragmentManager;

        /**
         * Nombre que identifica al número de sección para el fragmento
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

//        public WalletFragment() { }
//
//        /**
//         * Devuelve una nueva instancia del Fragment, dado un número de sección
//         */
//        public static WalletFragment newInstance(int sectionNumber) {
//            WalletFragment fragment = new WalletFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }


        // is called on initial creation of the fragment.
        // You do your non graphical initializations here.
        // It finishes even before the layout is inflated and the fragment is visible.
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            myActivity = (AppCompatActivity) getActivity();
            myContext = myActivity.getApplicationContext();
            myFragmentManager = myActivity.getSupportFragmentManager();
        }


        // is called to inflate the layout of the fragment
        // i.e graphical initialization usually takes place here.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            // Nuevo: 2222 Prueba con la activity Wallet
            rootView = inflater.inflate(R.layout.activity_wallet, container, false);
            // Nuevo: 2222 Prueba con la activity Wallet

            return rootView;
        }


        // ******* 3333 Prueba con la activity Wallet *******
        // ******* 3333 Prueba con la activity Wallet *******
        // ******* 3333 Prueba con la activity Wallet *******

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Inicio la lista de valores a cargar en la billetera
            newLoadMoneyValueNames = new ArrayList<String>();

            // Actualizo el valor del total, inicio el subtotal en cero y muestro en pantalla
            st_subtotal = getString(R.string.value_0);
            st_total = WalletManager.getInstance().obtainTotalCreditInWallet(myActivity);
            refreshSubtotalAndTotal();

            // Inicio el SnackBarManager para luego crear mensajes emergentes
            sb = new SnackBarManager();

            // Obtengo todos los valores a mostrar para la carga de la billetera
            ArrayList<String> moneyValueNames = WalletManager.getInstance().obtainMoneyValueNamesOfValidCurrency(myActivity);

            // Cargo el slide de imágenes y puntos indicadores
            // Parámetros:  + (1)Contexto
            //              + (2)ViewPager con su (3)FragmentManager y sus (4)moneyValueNames (nombres de las imágenes)
            //              + (5)LinearLayout y sus (6)(7)imágenes representando al punto
            imageSlideManager = new ImageSlideManager(myActivity,
                    (ViewPager) rootView.findViewById(R.id.pager_wallet),
                    myFragmentManager,
                    moneyValueNames,
                    (LinearLayout) rootView.findViewById(R.id.SliderDots_wallet),
                    ContextCompat.getDrawable(myContext, R.drawable.active_dot),
                    ContextCompat.getDrawable(myContext, R.drawable.nonactive_dot));

        }



        /**
         * Cancela la carga en la billetera
         * */
        public void cancelLoad(View view) {
            sendToMain(view);
        }

        /**
         * Envía a la pantalla principal
         * */
        public void sendToMain(View view) {
            Intent intent = new Intent(myActivity, MainActivity.class);
            startActivity(intent);
        }

        /**
         * Muestra el texto de ayuda para este activity
         **/
        public void showHelp(View view) {
            sb.showTextIndefiniteOnClickActionDisabled(rootView.findViewById(R.id.coordinatorLayout_Wallet),getString(R.string.help_text_wallet),10);
        }

        /**
         * Actualiza el valor de la carga y del total
         **/
        public void refreshSubtotalAndTotal() {
            TextView textView = rootView.findViewById(R.id.textView6);
            textView.setText(getString(R.string.load_cash_sign) + st_subtotal + " - " + getString(R.string.total_cash_sign) + st_total);
        }

        /**
         * Sumo el valor seleccionado al subtotal de carga en billetera
         **/
        public void addValueToSubtotalTAB (View view) {

            // Obtengo el ID del valor elegido
            String st_valueID = imageSlideManager.getActualValueID();

            // Obtengo el valor monetario a partir del ID
            String st_value = WalletManager.getInstance().obtainValueFormID(myActivity,st_valueID);

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
         * Agrega la carga de dinero seleccionada en la billetera y luego envía a la pantalla principal
         * */
        public void addValuesToWallet (View view){

            // Guardo todos los valores seleccionados
            for(String currentNewLoadMoneyValueName : newLoadMoneyValueNames) {
                WalletManager.getInstance().addCurrencyInWallet(myActivity,currentNewLoadMoneyValueName);
            }

            sendToMain(view);
        }

        /**
         * Envía a la pantalla de configuración
         * */
        public void sendToConfiguration(View view) {
            Intent intent = new Intent(myActivity, ConfigurationActivity.class);
            startActivity(intent);
        }

        // ******* 3333 Prueba con la activity Wallet *******
        // ******* 3333 Prueba con la activity Wallet *******
        // ******* 3333 Prueba con la activity Wallet *******

    }


    public static class TabTwoFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, 2));
            return rootView;
        }

    }


    public static class TabThreeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, 3));
            return rootView;
        }

    }


    /**
     * Una clase FragmentPagerAdapter que representa cada uno de los tabs dentro de un Fragment
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new ArrayList<>();
            for (int i = 0; i < 3; i++){    // Todo: Pasar el valor "3" al archivo strings.xml
                switch (i) {
                    case 0:
                        fragments.add(new WalletFragment());
                    case 1:
                        fragments.add(new TabTwoFragment());
                    case 2:
                        fragments.add(new TabThreeFragment());
                    default:
                        fragments.add(null);
                }
            }
        }

        // Es llamado para instanciar el fragmento de la posición dada.
        // Devuelve un fragmento WalletFragment (Definido anteriormente).
        @Override
        public Fragment getItem(int position) {
            Log.d("MainTabActivity","newInstance: |"+ (position+1) + "|");

            return fragments.get(position);
        }

        // Muestra la cantidad total de tabs en el Adapter.
        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }
}
