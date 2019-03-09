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


public class MainTabActivity extends AppCompatActivity implements WalletFragment.OnFragmentInteractionListener {

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


    // TODO: Implementar
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

    // Implemento OnFragmentInteractionListener para accionar cuando un fragment avisa sobre cambios
    @Override
    public void updateFragments(int idFragmentCaller){
        mSectionsPagerAdapter.updateSections(idFragmentCaller);
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
                    case 0:
                        fragments.add(new ViewWalletFragment());
                    case 1:
                        fragments.add(new TabTwoFragment());
                    case 2:
                        fragments.add(new WalletFragment());
                    default:
                        //
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
            // TODO: Agregar un "if(...isinstance)..." para actuar en cada uno de los 3 casos de los tabs
            // En teoría solo necesitaría "return POSITION_NONE" para el caso del tab 2, porque tiene que actualizar el objeto
            return POSITION_NONE;
        }


        // TODO: Implementar. Según el idFragmentCaller (el fragment que se modificó) hago actualizaciones
        // Implemento reacción del OnFragmentInteractionListener para accionar cuando un fragment avisa
        public void updateSections(int idFragmentCaller){

            switch (idFragmentCaller) {
                case 0:
                    // No puede realizar cambios que afecten a los demás
                case 1:
                    ((ViewWalletFragment)fragments.get(0)).updateView();
                    ((WalletFragment)fragments.get(2)).updateView();
                case 2:
                    ((ViewWalletFragment)fragments.get(0)).updateView();
                // TODO: Agregar los casos cuando en la configuración (alta/baja de valores) se afecta la billetera
                default:
                    //
            }


        }


        // TODO: Implementar nuevo update pero para reemplazar un fragment por otro
        // TODO: ... Para el caso del tab de compra, sirve para instanciar nuevos fragment
        // TODO: ... Y luego de instanciado se reemplaza en el array de fragment
        // TODO: ... Luego de todo eso, se usará notifyDataSetChanged(); para que llame a getItemPosition()



    }



    /**
     *****************************************************************************************************
     *****************************************************************************************************
     *****************************************************************************************************
     */


    // Todo: Clases temporales. Luego de crear las definitivas, borrarlas

    public static class TabTwoFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format));
            return rootView;
        }

    }


}


