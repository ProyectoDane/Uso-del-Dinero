package proyectodane.usodeldinero;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtengo el valor pasado a través de Bundle
        Bundle args = getArguments();
        // TODO: Ver nombre definitivo del valor por defecto, luego quitar esta línea
        String st_money_value_name = args.getString(getString(R.string.tag_money_value_name), getString(R.string.tag_p20f));

        // Asigno el View
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        // Preparo el ID correspondiente a la imagen que quiero cargar en el ImageView
        int resId = getResources().getIdentifier(st_money_value_name,getString(R.string.tag_drawable),getActivity().getPackageName());

        // Cambio los datos al ImageView para que cargue la imagen nueva
        View iv = rootView.findViewById(R.id.imageView11);
        ((ImageView)iv).setBackgroundResource(resId);

        return rootView;
    }
}