package proyectodane.usodeldinero;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
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
                        fragments.add(new ViewWalletFragment());
                    case 1:
                        fragments.add(new TabTwoFragment());
                    case 2:
                        fragments.add(new WalletFragment());
                    default:
                        fragments.add(null);
                }
            }
        }

        // Es llamado para instanciar el fragmento de la posición dada.
        // Devuelve un fragmento WalletFragment (Definido anteriormente).
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        // Muestra la cantidad total de tabs en el Adapter.
        @Override
        public int getCount() {
            return 3; // Todo: Pasar el valor "3" al archivo strings.xml
        }


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
            textView.setText(getString(R.string.section_format, 2));
            return rootView;
        }

    }


}
