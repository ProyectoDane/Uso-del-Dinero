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


/**
 * Clase que representa un slide de imágenes con un indicador de cantidad dibujado por puntos
 */
public class ImageSlideManager {

    /**
     * El widget pager, maneja la animación y permite deslizar horizontalmente para acceder
     * a las imágenes anteriores y siguientes.
     */
    private ViewPager viewPager;

    /**
     * El pager adapter, provee las páginas al ViewPager.
     */
    private PagerAdapter pagerAdapter;

    /**
     * Un LinearLayout que representa la fila de puntos la cual indica la posición relativa de
     * la imagen y la cantidad total de imágenes del ViewPager.
     */
    private LinearLayout sliderDotsPanel;
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
        viewPager = viewPagerSelected;
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManagerSelected,fragments);
        viewPager.setAdapter(pagerAdapter);

        // Instancio un LinearLayout, para representar los puntos debajo de las imágenes
        sliderDotsPanel = linearLayoutSelected;

        // Seteo la cantidad de puntos (imágenes) y el arreglo de ImageView para luego instanciar a cada uno de ellos
        dotsCount = pagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        // Cargo la imagen para cada punto en el estado inicial
        for(int i = 0; i < dotsCount; i++){

            // Instancia el ImageView y setea la imagen
            dots[i] = new ImageView(activityContext);
            dots[i].setImageDrawable(drawableNonActiveDot);

            // Prepara los parámetros de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            // Setea los parámetros de la imagen
            sliderDotsPanel.addView(dots[i], params);
        }

        // En el estado inicial, el primer punto será el seleccionado. Seteo la imagen.
        dots[0].setImageDrawable(drawableActiveDot);

        // Agrego un listener que será invocado cuando la imagen cambie y actualizará las imágenes de los puntos
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    /**
     * Comportamiento por defecto cuando se presiona "back"
     * */
    public void defaultOnBackPressed(){
        if (viewPager.getCurrentItem() == 0) {
            // Si el usuario se encuentra en la primer imagen y presiona el botón "back"
            // no permito que se vaya a otra pantalla, para evitar el error
            // de presionar ese botón por equivocación.
        } else {
            // Si es otra imagen que no sea la primera uso el "back" para volver atrás una imagen
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
    }

}
