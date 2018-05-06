package proyectodane.usodeldinero;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // TODO: Acá en vez de ser in textView, se cambiaría por una imagen, la cual cambiaria su nombre según diga el bundle
        // Le cambio los datos al textView según el parámetro recibido en el Bundle
        View tv = rootView.findViewById(R.id.textView11);
        String text = getActivity().getResources().getString(R.string.im_an_image_no);
        ((TextView)tv).setText( text + index);

        return rootView;
    }
}