package proyectodane.usodeldinero;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Un PagerAdapter que representa todos los objetos ScreenSlidePageFragment en secuencia,
 * los cuales identifican a cada uno de las imágenes de billetes/monedas a usar para
 * pagar el importe total.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    /**
     * Número de páginas (imágenes) a mostrar
     */
    int numberOfPages = 5; // TODO: Luego este valor variará según el importe

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);

    }

    // TODO: Tengo que recibir el listado (ya calculado) de los billetes/monedas a mostrar

    // TODO: Tengo cargar un arraylist con todos los fragmentos (ya creados y con los bundle cargados) que voy a usar.
    // Luego, cada vez que se llama el getItem() se le brinda el fragment desde ese arrayList

    @Override
    public Fragment getItem(int position) {

        // Creo un fragmento y le paso un valor para que internamente se pueda modificar
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

        // Creo un bundle para pasarle el ID del billete como argumento
        Bundle args = new Bundle();
        args.putString("index", String.valueOf(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfPages; // TODO: Será el tamaño del vector calculado anteriormente
    }
}
