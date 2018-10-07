package proyectodane.usodeldinero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;


public class NewCurrencyActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    ImageView imageViewTest; // TODO: Prueba, borrar al finalizar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_currency);

        imageViewTest = (ImageView) findViewById(R.id.imageViewTest); // TODO: Prueba, borrar al finalizar

    }

    /**
     * Cargo la imagen seleccionada por el usuario
     * */
    public void loadImage(View view) {
        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        loadImageIntent.setType("image/");
        startActivityForResult(loadImageIntent.createChooser(loadImageIntent,getString(R.string.select_app)),RESULT_LOAD_IMAGE);

        // Opción 2
        //startActivityForResult(loadImageIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewTest.setImageBitmap(selectedImage); // TODO: Prueba, borrar al finalizar
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // TODO: Habilitar al finalizar el layout
                //SnackBarManager sb = new SnackBarManager();
                //sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_img_not_found),5);
            }

        }else {
            // TODO: Habilitar al finalizar el layout
            //SnackBarManager sb = new SnackBarManager();
            //sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_img_not_selected),5);
        }
    }

}
