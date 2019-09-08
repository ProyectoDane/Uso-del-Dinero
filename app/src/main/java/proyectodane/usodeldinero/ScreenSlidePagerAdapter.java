package proyectodane.usodeldinero;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

/**
 * Un PagerAdapter que representa todos los objetos ScreenSlidePageFragment en secuencia,
 * los cuales identifican a cada uno de las imágenes de billetes/monedas a usar para
 * pagar el importe total.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    /**
     * ArrayList con todos los fragmentos (ya creados y con los bundle cargados) que voy a usar
     */
    private ArrayList<ScreenSlidePageFragment> fragments;

    public ScreenSlidePagerAdapter(FragmentManager fm,ArrayList<ScreenSlidePageFragment> frags) {
        super(fm);
        fragments = frags;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

}
