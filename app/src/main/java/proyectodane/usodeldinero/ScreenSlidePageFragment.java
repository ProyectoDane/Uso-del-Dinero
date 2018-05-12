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
        String index = args.getString("index", "0");

        // Asigno el View
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        // Le cambio los datos al ImageView según el parámetro recibido en el Bundle
        View iv = rootView.findViewById(R.id.imageView11);
        ((ImageView)iv).setBackgroundResource(R.drawable.p20f);

        return rootView;
    }
}