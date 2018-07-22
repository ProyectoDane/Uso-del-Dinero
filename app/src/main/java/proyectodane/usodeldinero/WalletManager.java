package proyectodane.usodeldinero;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

public class WalletManager {

    // Uso singleton
    private static final WalletManager ourInstance = new WalletManager();

    /**
     * Valores de los billetes/monedas vigentes en circulación.
     * Las imágenes relacionadas con cada billete/moneda tendrán el mismo nombre que su ID.
     * Key: ID moneda (String)  Value: Valor de la moneda (String)
     * */
    SharedPreferences validCurrency;

    /**
     * Cada uno de los billetes/monedas guardados en la billetera.
     * Las imágenes relacionadas con cada billete/moneda tendrán el mismo nombre que su ID.
     * Key: ID moneda (String)  Value: Valor de la moneda (String).
     * */
    SharedPreferences currencyInWallet;


    // Uso singleton
    private WalletManager() { }
    public static WalletManager getInstance() {
        return ourInstance;
    }

    /**
     * Obtengo un Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    public Map <String,String> getValidCurrency(Context context) {
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        return (Map<String,String>)validCurrency.getAll();
    }

    /**
     * Persisto el Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    public void setValidCurrency(Context context, Map <String,String> validCurrencyMap){
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = validCurrency.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

        // TODO: Recorrer el Map entero y cargar todos los datos con editor (con "editor.putString()")
        // Agrego el contenido del Map
        for (Map.Entry<String,String> entry : validCurrencyMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            editor.putString(key,value);
        }
        editor.apply();
    }

    /**
     * Obtengo un Map con cada uno de los billetes/monedas guardados en la billetera.
     * */
    public Map <String,String> getCurrencyInWallet(Context context) {
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        return (Map<String,String>)currencyInWallet.getAll();
    }

    /**
     * Persisto el Map con cada uno de los billetes/monedas guardados en la billetera.
     * */
    public void setCurrencyInWallet(Context context, Map <String,String> currencyInWalletMap){
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        SharedPreferences.Editor editor = currencyInWallet.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

        // TODO: Recorrer el Map entero y cargar todos los datos con editor (con "editor.putString()")
        // Agrego el contenido del Map
        for (Map.Entry<String,String> entry : currencyInWalletMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            editor.putString(key,value);
        }
        editor.apply();
    }

}

