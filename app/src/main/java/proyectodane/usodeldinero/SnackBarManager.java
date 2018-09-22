package proyectodane.usodeldinero;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class SnackBarManager {

    public SnackBarManager(){ }

    /**
     * Muestra el texto de ayuda en un Snackbar para este activity
     * Con duración determinada por parámetro y con botón el cual no tiene acción asociada
     **/
    private void showTextOnClickActionDisabled(View snackbarView, String textMsg, int maxLines, int duration) {

        // Creo el Snackbar, el cual se mostrará indefinidamente en el tiempo
        Snackbar snackbar = Snackbar.make(snackbarView,textMsg,duration);

        // Agrego un botón dentro del Snackbar
        snackbar.setAction("Listo",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acá se inicia la acción al presionar el botón del Snackbar (No hace nada)
            }
        });

        // Obtengo el view del Snackbar para luego setear la cantidad máxima de líneas de texto permitidas
        View snackBarView = snackbar.getView();
        TextView snackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setMaxLines(maxLines);

        // Muestro el Snackbar
        snackbar.show();
    }

    /**
     * Muestra el texto de ayuda en un Snackbar para este activity
     * Con duración ilimitada y con botón el cual no tiene acción asociada
     **/
    public void showTextIndefiniteOnClickActionDisabled(View snackbarView, String textMsg, int maxLines) {
        showTextOnClickActionDisabled(snackbarView,textMsg,maxLines,Snackbar.LENGTH_INDEFINITE);
    }

    /**
     * Muestra el texto de ayuda en un Snackbar para este activity
     * Con duración corta de tiempo y con botón el cual no tiene acción asociada
     **/
    public void showTextShortOnClickActionDisabled(View snackbarView, String textMsg, int maxLines) {
        showTextOnClickActionDisabled(snackbarView,textMsg,maxLines,Snackbar.LENGTH_SHORT);
    }

}
