package proyectodane.usodeldinero;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class NewCurrencyActivity extends AppCompatActivity {

    /**
     * Código para el intent
     ***/
    private static int RESULT_LOAD_IMAGE = 1;

    /**
     * Encargado de guardar la imagen
     ***/
    ImageManager im;

    /**
     * Estado de la imagen guardada
     ***/
    Boolean imageLoaded;

    /**
     * Carga de datos de la imagen seleccionada
     ***/
    Bitmap selectedImage;

    /**
     * Ancho de la imagen cargada
     ***/
    final int DIP_IMAGE_SIZE_WIDTH = 500;

    /**
     * Ancho de la imagen cargada
     ***/
    final int DIP_IMAGE_SIZE_HEIGHT = 210;

    /**
     * Ancho de la vista previa de la imagen cargada
     ***/
    final int THUMBNAIL_SIZE_WIDTH = 200;

    /**
     * Alto de la vista previa de la imagen cargada
     ***/
    final int THUMBNAIL_SIZE_HEIGHT = 84;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_currency);
        im = new ImageManager();
        imageLoaded = false;
        selectedImage = null;
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            //Muestro la flecha atrás en el actionbar
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Cargo la imagen seleccionada por el usuario
     * */
    public void loadImage(View view) {
        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        loadImageIntent.setType("image/");
        startActivityForResult(loadImageIntent.createChooser(loadImageIntent,getString(R.string.select_app)),RESULT_LOAD_IMAGE);
    }

    /**
     * Guardo la imagen en un archivo del almacenamiento interno
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                // Cargo la imagen
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap originalImage = BitmapFactory.decodeStream(imageStream);
                selectedImage = Bitmap.createScaledBitmap(originalImage,(int)dipToPixels(this,DIP_IMAGE_SIZE_WIDTH),(int)dipToPixels(this,DIP_IMAGE_SIZE_HEIGHT),false);
                imageLoaded = true;

                // Cargo una miniatura para que el usuario pueda verificar la imagen seleccionada
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(selectedImage,THUMBNAIL_SIZE_WIDTH, THUMBNAIL_SIZE_HEIGHT);
                ImageView imageView;
                imageView = (ImageView) findViewById(R.id.imageViewThumb);
                imageView.setImageBitmap(ThumbImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                SnackBarManager sb = new SnackBarManager();
                sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_img_not_found),5);
            }

        }else{
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_img_not_selected),5);
        }
    }

    /**
     * Guardo la imagen previamente seleccionada y cargada
     * */
    public void saveNewValue(View view) {

        // Verifico si se encuentran los datos necesarios listos para guardar
        if(!imageLoaded) {
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_img_not_selected),5);
            return;
        }

        // Obtengo el valor ingresado y verifico si tiene formato numérico válido
        EditText et_newValue = (EditText) findViewById(R.id.editText2);
        String st_newValue = et_newValue.getText().toString();
        if ( !(WalletManager.getInstance().isFloatFormatValid(st_newValue)) ) {
            SnackBarManager sb = new SnackBarManager();
            sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.error_value_not_selected),5);
            return;
        }

        // Obtengo la selección de tipo de valor (Billete o Moneda) para registrarlo en el ID
        RadioButton rb_bill = (RadioButton) findViewById(R.id.radioButtonBill);
        String fileName = getString(R.string.type_1);
        if(!rb_bill.isChecked()){ fileName = getString(R.string.type_2); }

        // Genero el ID final, para asignar al nuevo valor y guardo en archivo la imagen
        fileName = fileName + generateNewID();
        im.saveBitmapJPEGToFile(this,fileName,selectedImage,100);
        selectedImage.recycle();

        // Guardo el nuevo valor en el registro de la billetera. Uso la suma de valores con "0" para asegurar el formato
        WalletManager.getInstance().addNewCurrency(this,fileName,WalletManager.getInstance().addValues(st_newValue,"0"));

        // Inhabilito el botón para guardar
        Button exitButton = (Button) findViewById(R.id.button24);
        exitButton.setEnabled(false);

        // Envío mensaje de información
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_value_saved))

                // Especifico un Listener para permitir llevar a cabo acciones cuando se acepta
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);
                        startActivity(intent);

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .show();

    }

    /**
     * Cargo la imagen seleccionada por el usuario
     * */
    private String generateNewID(){

        // Obtengo fecha y hora, le doy formato y lo guardo en un string
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat(".yyyy.MM.dd.HH.mm.ss");
        return simpleDateFormat.format(date);
    }


    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, displayMetrics);
    }

//    /**
//     * Muestra el texto de ayuda para este activity
//     **/
//    public void showHelp(View view) {
//        SnackBarManager sb = new SnackBarManager();
//        sb.showTextIndefiniteOnClickActionDisabled(findViewById(R.id.coordinatorLayout_newCurrency),getString(R.string.help_text_new_currency),10);
//    }

}
