package proyectodane.usodeldinero;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class ImageSlideManager {

    /**
     * El widget pager, maneja la animación y permite deslizar horizontalmente para acceder
     * a las imágenes anteriores y siguientes.
     */
    private ViewPager mPager;

    /**
     * El pager adapter, provee las páginas al ViewPager.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Un LinearLayout que representa la fila de puntos la cual indica la posición relativa de
     * la imagen y la cantidad total de imágenes del ViewPager.
     */
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;

    /**
     * ArrayList con todos los Fragment instanciados, de cada imagen
     */
    private ArrayList<ScreenSlidePageFragment> fragments;


    public ImageSlideManager(Context activityContext, ViewPager viewPagerSelected, FragmentManager fragmentManagerSelected, ArrayList<String> listImageNames, LinearLayout linearLayoutSelected, final Drawable drawableActiveDot, final Drawable drawableNonActiveDot){

        // Cargo todos los Fragment que alimentarán al PagerAdapter
        fragments = buildFragments(activityContext,listImageNames);

        // Instancio el ViewPager y el PagerAdapter, para deslizar las imágenes
        mPager = viewPagerSelected; //TODO: El que llame al constructor debe poner (Ej. de WalletActivity): (ViewPager) findViewById(R.id.pager_wallet);
        mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManagerSelected,fragments); //TODO: El que llame al constructor debe poner "getSupportFragmentManager()"
        mPager.setAdapter(mPagerAdapter);

        // Instancio un LinearLayout, para representar los puntos debajo de las imágenes
        sliderDotsPanel = linearLayoutSelected; //TODO: El que llame al constructor debe poner (Ej. de WalletActivity): (LinearLayout) findViewById(R.id.SliderDots_wallet);

        // Seteo la cantidad de puntos (imágenes) y el arreglo de ImageView para luego instanciar a cada uno de ellos
        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        // Cargo la imagen para cada punto en el estado inicial
        for(int i = 0; i < dotsCount; i++){

            // Instancia el ImageView y setea la imagen
            dots[i] = new ImageView(activityContext);//TODO: El que llame al constructor debe poner "this"
            dots[i].setImageDrawable(drawableNonActiveDot); //TODO: El que llame al constructor debe poner "ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot)"

            // Prepara los parámetros de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            // Setea los parámetros de la imagen
            sliderDotsPanel.addView(dots[i], params);
        }

        // En el estado inicial, el primer punto será el seleccionado. Seteo la imagen.
        dots[0].setImageDrawable(drawableActiveDot); //TODO: El que llame al constructor debe poner "ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot)"

        // Agrego un listener que será invocado cuando la imagen cambie y actualizará las imágenes de los puntos
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /* "This method will be invoked when the current page is scrolled, either as part of a
                programmatically initiated smooth scroll or a user initiated touch scroll." */
            }

            @Override  // Cuando una nueva imagen es seleccionada, actualizo las imágenes de los puntos
            public void onPageSelected(int position) {

                // Actualizo las imágenes de los puntos inactivos
                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(drawableNonActiveDot);
                }

                // Actualizo la imagen del punto activo
                dots[position].setImageDrawable(drawableActiveDot);
                                                //variable is accessed from within inner class, needs to be declared final
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /* "Called when the scroll state changes" */
            }
        });


    }


    /**
     * Creo la lista de Fragment a partir de todos los valores pasados en el argumento
     * */
    private ArrayList<ScreenSlidePageFragment> buildFragments(Context activityContext, ArrayList<String> listImageNames) {

        // Instancio la lista de Fragment
        ArrayList<ScreenSlidePageFragment> frags = new ArrayList<ScreenSlidePageFragment>();

        // Cargo la lista de Fragment
        for(int i = 0; i<listImageNames.size(); i++) {

            // Instancio el Fragment
            ScreenSlidePageFragment frag = new ScreenSlidePageFragment();

            // Creo un bundle para pasarle el ID del billete/moneda como argumento
            Bundle args = new Bundle();
            args.putString(activityContext.getString(R.string.tag_money_value_name),listImageNames.get(i));
            frag.setArguments(args);
            frags.add(frag);

        }

        return frags;
    }


}
