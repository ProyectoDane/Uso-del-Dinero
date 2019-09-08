package proyectodane.usodeldinero;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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

    /**
     * ScreenSlidePageFragment actual seleccionado por el usuario
     */
    private ScreenSlidePageFragment actualPageFragment;


    public ImageSlideManager(Context activityContext, ViewPager viewPagerSelected, FragmentManager fragmentManagerSelected, ArrayList<String> listImageNames, LinearLayout linearLayoutSelected, final Drawable drawableActiveDot, final Drawable drawableNonActiveDot){

        // Cargo todas las imágenes en cada una de las estructuras
        setUpImages(activityContext,viewPagerSelected,fragmentManagerSelected,listImageNames,linearLayoutSelected,drawableActiveDot,drawableNonActiveDot);

        // Agrego un listener que será invocado cuando la imagen cambie y actualizará las imágenes de los puntos
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /* "Este método será invocado cuando la imagen actual es deslizada, aunque sea realizado
                 por código o con touch del usuario" */
            }

            @Override  // Cuando una nueva imagen es seleccionada, actualizo las imágenes de los puntos
            public void onPageSelected(int position) {

                // Actualizo las imágenes de los puntos inactivos
                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(drawableNonActiveDot);
                }

                // Actualizo la imagen del punto activo
                dots[position].setImageDrawable(drawableActiveDot);

                // Actualizo el fragmento actual al seleccionar uno nuevo
                actualPageFragment = (ScreenSlidePageFragment)((ScreenSlidePagerAdapter)pagerAdapter).getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /* Llamado cuando el Scroll cambia de posición (Sin uso actualmente) */
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

    /**
     * Obtengo el ID de la imagen seleccionada.
     * El mismo debe ser llamado solamente cuando el fragmento actual ya se encuentra en el view (en vista)
     * */
    public String getActualValueID(){
        return actualPageFragment.getValueID();
    }

    public void setUpImages(Context activityContext, ViewPager viewPagerSelected, FragmentManager fragmentManagerSelected, ArrayList<String> listImageNames, LinearLayout linearLayoutSelected, final Drawable drawableActiveDot, final Drawable drawableNonActiveDot){

        // Cargo todos los Fragment que alimentarán al PagerAdapter
        fragments = buildFragments(activityContext,listImageNames);

        // Instancio el ViewPager y el PagerAdapter, para deslizar las imágenes
        viewPager = viewPagerSelected;
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManagerSelected,fragments);
        viewPager.setAdapter(pagerAdapter);

        // En el estado inicial, la primer imagen será la seleccionada (Si al menos tiene una imagen). Elijo el fragment.
        if(pagerAdapter.getCount()>0) {
            actualPageFragment = (ScreenSlidePageFragment) ((ScreenSlidePagerAdapter) pagerAdapter).getItem(0);
        }

        // Instancio un LinearLayout, para representar los puntos debajo de las imágenes
        // Remueve datos pre existentes (si hubieran)
        sliderDotsPanel = linearLayoutSelected;
        sliderDotsPanel.removeAllViews();

        // Seteo la cantidad de puntos (imágenes) y el arreglo de ImageView para luego instanciar a cada uno de ellos
        dotsCount = pagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        // Cargo la imagen para cada punto en el estado inicial
        for(int i = 0; i < dotsCount; i++){

            // Instancia el ImageView y setea la imagen
            dots[i] = new ImageView(activityContext);
            dots[i].setImageDrawable(drawableNonActiveDot);

            // Prepara los parámetros de la imagen de los puntos
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            // Setea los parámetros de la imagen de los puntos
            sliderDotsPanel.addView(dots[i], params);
        }

        // En el estado inicial, el primer punto será el seleccionado (Si al menos tiene una imagen). Seteo la imagen.
        if(pagerAdapter.getCount()>0) {
            dots[0].setImageDrawable(drawableActiveDot);
        }

    }


    /**
     * Llevo el ViewPager al principio
     * */
    public void setFirstPage(){
        // Al inicio seteo la primer página del ViewPager
            viewPager.setCurrentItem(0);
    }


    /**
     * Actualizo todas las imágenes
     * */
    public void notifyDataChanged(){
        // Al inicio seteo la primer página del ViewPager
        pagerAdapter.notifyDataSetChanged();
    }

}
