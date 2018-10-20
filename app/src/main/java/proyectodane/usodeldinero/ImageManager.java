package proyectodane.usodeldinero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManager {


    public ImageManager(){

    }

    /**
     * Guarda una imagen JPEG ubicada en <res/drawable>, en el almacenamiento interno.
     */
    public void saveDefaultDrawableImageJPEGToInternalStorage(Context context,String fileName){

        int id = context.getResources().getIdentifier(fileName,context.getString(R.string.tag_drawable),context.getPackageName());

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),id);

        saveBitmapToFile(context,fileName,bm,Bitmap.CompressFormat.JPEG,100);
    }

    /**
     * Guarda una imagen (En formato Bitmap) en el almacenamiento interno
     */
    private boolean saveBitmapToFile(Context context, String fileName, Bitmap bm, Bitmap.CompressFormat format, int quality) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bm.compress(format,quality,fos);
            fos.close();
            return true;
        }catch (IOException e) {
            Log.e("app",e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Guarda una imagen (En formato JPEG) en el almacenamiento interno
     */
    public boolean saveBitmapJPEGToFile(Context context, String fileName, Bitmap bm, int quality) {
        return saveBitmapToFile(context,fileName,bm,Bitmap.CompressFormat.JPEG,quality);
    }

}
