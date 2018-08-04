package proyectodane.usodeldinero;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WalletManager {

    // Uso singleton
    private static final WalletManager ourInstance = new WalletManager();

    /**
     * Valores de los billetes/monedas vigentes en circulación.
     * Las imágenes relacionadas con cada billete/moneda tendrán el mismo nombre que su ID.
     * Key: ID moneda (String)  Value: Valor de la moneda (String)
     * */
    private SharedPreferences validCurrency;

    /**
     * Cada uno de los billetes/monedas guardados en la billetera.
     * Las imágenes relacionadas con cada billete/moneda tendrán el mismo nombre que su ID.
     * Key: ID moneda (String)  Value: Valor de la moneda (String).
     * */
    private SharedPreferences currencyInWallet;


    // Uso singleton
    private WalletManager() { }
    public static WalletManager getInstance() {
        return ourInstance;
    }

    /**
     * Obtengo un Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    private Map <String,String> getValidCurrency(Context context) {
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        return (Map<String,String>)validCurrency.getAll();
    }

    /**
     * Persisto el Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    private void setValidCurrency(Context context, Map <String,String> validCurrencyMap){
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = validCurrency.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

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
    private Map <String,String> getCurrencyInWallet(Context context) {
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        return (Map<String,String>)currencyInWallet.getAll();
    }

    /**
     * Persisto el Map con cada uno de los billetes/monedas guardados en la billetera.
     * */
    private void setCurrencyInWallet(Context context, Map <String,String> currencyInWalletMap){
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        SharedPreferences.Editor editor = currencyInWallet.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

        // Agrego el contenido del Map
        for (Map.Entry<String,String> entry : currencyInWalletMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            editor.putString(key,value);
        }
        editor.apply();
    }

    /**
     * Verdadero si el String tiene formato numérico de importe válido
     * */
    public boolean isFloatFormatValid(String val) {

        boolean isValid = true;

        try {
            BigDecimal bd_val = new BigDecimal(val);
        } catch (NumberFormatException e) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Verdadero si el val (con formato numérico de importe) es mas grande
     * que el valor de importe total actual de la billetera
     * */
    public boolean isGreaterThanTotalWallet(String val) {

        boolean isGreater = false;

        //TODO: Cargar el valor real de la billetera
        String st_totalWallet = "20.00";

        // Instancio los BigDecimal
        BigDecimal bd_value = new BigDecimal(val);
        BigDecimal bd_total = new BigDecimal(st_totalWallet);

        //Comparo los valores: Devuelve 1 si "bd_value" > "bd_total" )
        if (bd_value.compareTo(bd_total) == 1) {
            isGreater = true;
        }

        return isGreater;
    }

    /**
     * Verdadero si el valueA (con formato numérico de importe) es mas grande
     * que el valueB (con formato numérico de importe)
     * */
    private boolean isValueAGreaterThanValueB(String valueA, String valueB) {

        boolean isGreater = false;

        // Instancio los BigDecimal
        BigDecimal bd_value_a = new BigDecimal(valueA);
        BigDecimal bd_value_b = new BigDecimal(valueB);

        //Comparo los valores: Devuelve 1 si "valueA" > "valueB" )
        if (bd_value_a.compareTo(bd_value_b) == 1) {
            isGreater = true;
        }

        return isGreater;
    }

    /**
     * Verdadero si el valueA (con formato numérico de importe) es igual o mayor
     * que el valueB (con formato numérico de importe)
     * */
    private boolean isValueAGreaterOrEqualThanValueB(String valueA, String valueB) {

        boolean isGreaterOrEqual = false;

        // Instancio los BigDecimal
        BigDecimal bd_value_a = new BigDecimal(valueA);
        BigDecimal bd_value_b = new BigDecimal(valueB);

        //Comparo los valores: Devuelve 1 si "valueA" > "valueB" )
        if ( (bd_value_a.compareTo(bd_value_b) == 0) || (bd_value_a.compareTo(bd_value_b) == 1) ) {
            isGreaterOrEqual = true;
        }

        return isGreaterOrEqual;
    }

    /**
     * Verdadero si el value (con formato numérico de importe) es mas grande
     * que el valor *Cero* (con formato numérico de importe)
     * */
    public boolean isGreaterThanValueCero(String value) {
        return isValueAGreaterThanValueB(value,BigDecimal.ZERO.toPlainString());
    }

    /**
     * Sumo dos valores: val_1 y val_2 (con formato numérico de importe)
     * Y devuelvo un string con el resultado (con formato numérico de importe), el cual
     * tiene como formato establecido un máximo de 2 dígitos decimales,
     * redondeando hacia abajo (truncando) en los casos donde sea necesario
     * */
    public String addValues(String val_1, String val_2){

        // Instancio los BigDecimal
        BigDecimal bd_v1 = new BigDecimal(val_1);
        BigDecimal bd_v2 = new BigDecimal(val_2);

        // Formato: Dejo (a lo sumo) dos decimales del número ingresado, truncando el resto
        bd_v1 = bd_v1.setScale(2, RoundingMode.FLOOR);
        bd_v2 = bd_v2.setScale(2, RoundingMode.FLOOR);

        // Sumo los valores
        BigDecimal result = bd_v1.add(bd_v2);

        // devuelvo el resultado en un String
        return result.toPlainString();
    }


    //TODO: Sacar el context como parámetro solicitado, una vez implementada la forma definitiva
    /**
     * Creo la lista de nombres de las imágenes de los valores
     * a partir de todos billetes/monedas existentes
     * */
    public ArrayList<String> obtainMoneyValueNamesOfValidCurrency(Context context){

        // Todo: Reemplazar esta forma de carga con la definitiva

        // Creo un Map con valores a guardar en el WalletManager
        Map<String,String> tempMapSave = new HashMap<String,String>();
        tempMapSave.put(context.getString(R.string.tag_p5),"5");
        tempMapSave.put(context.getString(R.string.tag_p5b),"5");
        tempMapSave.put(context.getString(R.string.tag_p10),"10");
        tempMapSave.put(context.getString(R.string.tag_p10b),"10");
        tempMapSave.put(context.getString(R.string.tag_p20),"20");
        tempMapSave.put(context.getString(R.string.tag_p20b),"20");
        tempMapSave.put(context.getString(R.string.tag_p50),"50");
        tempMapSave.put(context.getString(R.string.tag_p50b),"50");
        tempMapSave.put(context.getString(R.string.tag_p100),"100");
        tempMapSave.put(context.getString(R.string.tag_p100b),"100");
        tempMapSave.put(context.getString(R.string.tag_p200),"200");
        tempMapSave.put(context.getString(R.string.tag_p500),"500");
        tempMapSave.put(context.getString(R.string.tag_p1000),"1000");

        // Guardo los valores del Map
        setValidCurrency(context,tempMapSave);

        // Cargo los valores recién guardados, en otro Map
        Map<String,String> tempMapLoad = getValidCurrency(context);

        // Paso los valores cargados del Map a un ArrayList
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String,String> entry : tempMapLoad.entrySet()) {
            list.add(entry.getKey());
        }

        // Pruebo ordenar los elementos de la lista
        Collections.sort(list); // TODO: Implementar el ordenado en base a los valores de los billetes/monedas

        return list;
    }

    //TODO: Reemplazar por implementación definitiva
    //TODO: Sacar el context como parámetro solicitado, una vez implementada la forma definitiva
    /**
     * Creo la lista de nombres de las imágenes de los valores a usar para el pago
     * a partir de todos billetes/monedas guardados en la billetera y del valor del importe a pagar
     * */
    public ArrayList<String> obtainMoneyValueNamesOfPayment(Context context, String valueToPay){

        // Instancio la lista de valores
        ArrayList<String> valueNames = new ArrayList<String>();


        // TODO: Aquí tengo que calcular el listado de billetes que uso para pagar y su respectivo vuelto
        // TODO: Por ejemplo si pago $90, calculo a partir de lo que tengo en la billetera y...
        // TODO: ...obtengo como resultado: $50, $20, $10, $10. Entonces creo un vector...
        // TODO: ...que tenga los ID que representen cada billete: [img_07_p50,img_05_p20,img_03_p10,img_03_p10]
        // Cargo la lista de valores
        valueNames.add(context.getString(R.string.tag_p20));
        valueNames.add(context.getString(R.string.tag_p20));

        return valueNames;
    }

    //TODO: Implementar: Devolver los billetes/moneda que hay en billetera (CurrencyInWallet)
    /**
     * Creo la lista de nombres de las imágenes de los valores actualmente en la billetera
     * a partir de todos billetes/monedas guardados en la billetera
     * */
    public ArrayList<String> obtainMoneyValueNamesInWallet(Context context){

        // Instancio la lista de valores
        ArrayList<String> valueNames = new ArrayList<String>();


        // Cargo la lista de valores
        valueNames.add(context.getString(R.string.tag_p20));
        valueNames.add(context.getString(R.string.tag_p20));

        return valueNames;
    }



    /**
     * Obtengo el saldo total actual en la billetera
     * */
    public String obtainTotalCreditInWallet(Context context){

        // Instancio la lista de valores
        String st_total = context.getString(R.string.value_40);

        //TODO: Implementar: Recorrer todos los valores en la billetera y sumar sus valores, luego devolver el valor total

        return st_total;
    }

    /**
     * Guardo en la billetera la lista de nombres de las imágenes
     * de los valores recibidos como vuelto
     * */
    public void saveChangeInWallet(ArrayList<String> listOfNames){

    // TODO: Implementar: Guardar ArrayList de billetes/monedas en billetera, dado un vuelto recibido

    }


    /**
     * Devuelvo el importe de vuelto que debería recibir, en base al importe que se tiene que pagar
     * y en base a los billetes/monedas que serán usados para el pago (guardados en la billetera)
     * */
    public String expectedChangeValue(String valueToPay, Context context){

        String expectedChange = context.getString(R.string.value_10);

        // TODO: Implementar Método: Devolver el importe de vuelto esperado

        return expectedChange;
    }


    /**
     * Devuelvo true si el valor del vuelto recibido es igual o mayor al vuelto esperado,
     * en base al vuelto recibido y al vuelto esperado
     * */
    public boolean isTotalChangeReceivedOk(String changeReceived, String changeExpected){
        return isValueAGreaterOrEqualThanValueB(changeReceived,changeExpected);
    }


    /**
     * Devuelvo true si corresponde recibir vuelto, en base al importe que se tiene que pagar
     * y en base a los billetes/monedas que serán usados para el pago (guardados en la billetera)
     * */
    public boolean isChangeExpected(String valueToPay){

        boolean isChangeExpected = true;

        // TODO: Implementar Método: Devolver si corresponde recibir vuelto

        return isChangeExpected;
    }


    /**
     * Guardo un nuevo billete/moneda vigente para ser usado luego como moneda nueva
     * de la billetera
     * */
    public void addNewCurrency(String idCurrency, String currencyValue){

        // TODO: Implementar Método: Agregar nuevo billete/moneda (Input: ID_moneda (Str), Valor en $ (Str) )

    }

    /**
     * Borro un billete/moneda vigente, el cual dejará de ser usado como moneda de pago
     * de la billetera
     * */
    public void deleteExistingCurrency(String idCurrency){

        // TODO: Implementar Método: Borrar billete/moneda actual (Input: ID_moneda (Str))

    }

    /**
     * Agrego un billete/moneda a la billetera
     * */
    public void addCurrencyInWallet(String idCurrency){

        // TODO: Implementar Método: Guardar un billete/moneda en billetera (Input: ID_moneda (Str))

    }

    /**
     * Quito un billete/moneda de la billetera
     * */
    public void removeCurrencyFromWallet(String idCurrency){

        // TODO: Implementar Método: Sacar un billete/moneda de billetera (Input: ID_moneda (Str))

    }


}

