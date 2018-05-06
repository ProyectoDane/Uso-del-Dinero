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

    // TODO: Tengo que recibir el valor total de la compra y obtener los billetes que tengo en la billetera

    // TODO: Aquí tengo que calcular el listado de billetes que uso para pagar
    // Por ejemplo si pago $90, calculo a partir de lo que tengo en la billetera y...
    // ...obtengo como resultado: $50, $20, $10, $10
    // Entonces creo un vector que tenga los ID que representen cada billete: [p50,p20,p10,p10]
    // Y luego dentro de getItem devuelvo el ID correspondiente según la posición en el vector que se encuentre


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
