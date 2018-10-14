package proyectodane.usodeldinero;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

public class ScreenSlidePageFragment extends Fragment {

    /**
     * Guarda el valor del ID de la imagen correspondiente al Fragment.
     * */
    private String st_money_value_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtengo el valor pasado a través de Bundle
        Bundle args = getArguments();
        st_money_value_name = args.getString(getString(R.string.tag_money_value_name), getString(R.string.tag_question_mark));

        // Asigno el View
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        // TODO: Nuevo código para leer desde almacenamiento interno, habilitar cuando las imágenes ya se encuentren guardadas en almacenamiento interno. getFilesDir() devuelve: "/data/user/0/proyectodane.usodeldinero/files"
        // Cambio los datos al ImageView para que cargue la imagen nueva
        View iv = rootView.findViewById(R.id.imageView11);
        String pathToImage = getContext().getFilesDir() + "/" + st_money_value_name;
        ((ImageView)iv).setImageBitmap(BitmapFactory.decodeFile(pathToImage));

        /* TODO: Borrar una vez usado
        BitmapFactory.Options options = new BitmapFactory.Options();
        ((ImageView)iv).setImageBitmap(BitmapFactory.decodeFile(pathToImage,options));
        Integer ancho = new Integer(options.outWidth);
        Integer alto = new Integer(options.outHeight);
        Log.i("INF","Nombre: "+st_money_value_name+" Ancho: "+ ancho.toString() + " Alto: " + alto.toString() ); */

        return rootView;
    }

    /**
     * Obtengo el ID de la imagen seleccionada.
     * El mismo debe ser llamado solamente cuando el fragmento ya se encuentra en el view
     * */
    public String getValueID(){
        return st_money_value_name;
    }

}