package proyectodane.usodeldinero;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WalletManager {

    // Uso singleton
    private static final WalletManager ourInstance = new WalletManager();

    /**
     * Archivo que contiene:
     * Valores de los billetes/monedas vigentes en circulación.
     * Las imágenes relacionadas con cada billete/moneda tendrán el mismo nombre que su ID.
     * Key: ID moneda (String)  Value: Valor de la moneda (String)
     * */
    private SharedPreferences validCurrency;

    /**
     * Archivo que contiene:
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



    //****************************************
    //* Métodos de carga y guardado privados *
    //****************************************


    /**
     * Obtengo un Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    private Map <String,String> getValidCurrency(Context context) {
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        return (Map<String,String>)validCurrency.getAll();
    }

    /**
     * Persisto el Map con un nuevo valor de billete/moneda vigente en circulación.
     * */
    private void setValidCurrency(Context context,String currencyID, String currencyValue){
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = validCurrency.edit();

        // Agrego el contenido en el archivo
        editor.putString(currencyID,currencyValue);
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

    //TODO: Revisar junto con "setValidCurrency()"
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


    // *** Auxiliares privados***


    /**
     * A partir de un map con ID y valores de billetes/monedas, devuelvo en un ArrayList los ID ordenados
     * por su valor correspondiente
     * */
    private ArrayList<String> orderMapOfValues(Map <String,String> mapIdValue){

        // Paso los pares de valores a un ArrayList, para ser ordenados
        ArrayList< Pair <String,String> > pairList = new ArrayList<Pair <String,String>>();
        for (Map.Entry<String,String> entry : mapIdValue.entrySet()) {
            Pair <String,String> pair = new Pair <String,String>(entry.getKey(),entry.getValue());
            pairList.add(pair);

        }

        // Ordeno los pares de la lista, primero tipo (billete o moneda) y luego por valor
        Collections.sort(pairList, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair <String,String> p1, Pair <String,String> p2) {

                // Obtengo los ID de los valores
                String st_id_p1 = p1.first;
                String st_id_p2 = p2.first;

                // Obtengo los valores en dinero a formato BigDecimal
                BigDecimal bd_value_p1 = new BigDecimal(p1.second);
                BigDecimal bd_value_p2 = new BigDecimal(p2.second);

                // Ordeno primero por tipo y luego por valor
                int idTypeComparision = (st_id_p1.substring(0,2)).compareTo(st_id_p2.substring(0,2));
                if( idTypeComparision != 0 ){
                  return idTypeComparision;
                } else {
                  return bd_value_p1.compareTo(bd_value_p2);
                }

            }
        });

        // Obtengo la lista de los ID, ya ordenados
        ArrayList<String> orderedList = new ArrayList<String>();
        for (Pair <String,String> item : pairList) {
            orderedList.add(item.first);
        }

        return orderedList;
    }



    //****************************************
    //* Métodos de carga y guardado públicos *
    //****************************************


    // *** Alta y Baja de nuevos valores ***

    // TODO: Implementar Método: Agregar nuevo billete/moneda (Input: ID_moneda (Str), Valor en $ (Str) )
    /**
     * Guardo un nuevo billete/moneda vigente para ser usado luego como moneda nueva
     * de la billetera
     * */
    public void addNewCurrency(String idCurrency, String currencyValue){

    }


    // TODO: Implementar Método: Borrar billete/moneda actual (Input: ID_moneda (Str))
    /**
     * Borro un billete/moneda vigente, el cual dejará de ser usado como moneda de pago
     * de la billetera
     * */
    public void deleteExistingCurrency(String idCurrency){

    }



    // *** Manejo de los valores actuales en la billetera ***


    // TODO: Implementar Método: Guardar un billete/moneda en billetera (Input: ID_moneda (Str))
    /**
     * Agrego un billete/moneda a la billetera
     * */
    public void addCurrencyInWallet(String idCurrency){

    }


    // TODO: Implementar Método: Sacar un billete/moneda de billetera (Input: ID_moneda (Str))
    /**
     * Quito un billete/moneda de la billetera
     * */
    public void removeCurrencyFromWallet(String idCurrency){

    }


    // TODO: Implementar Método: Sacar todo de la billetera
    /**
     * Quito todos los valores existentes en la billetera, la vacío.
     * */
    private void removeAllCurrencyFromWallet(){

    }



    // *** Formato de valores en String ***


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



    // *** Comparación de valores ***


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
     * Verdadero si el val (con formato numérico de importe) es mas grande
     * que el valor de importe total actual de la billetera
     * */
    public boolean isGreaterThanTotalWallet(String val) {
        return isValueAGreaterThanValueB(val,obtainTotalCreditInWallet());
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


    // *** Operaciones matemáticas con valores ***

    /**
     * Sumo dos valores: val_1 y val_2 (con formato numérico de importe)
     * Y devuelvo un string con el resultado (con formato numérico de importe), el cual
     * tiene como formato establecido un máximo de 2 dígitos decimales,
     * redondeando hacia abajo (truncado) en los casos donde sea necesario
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



    // *** Lectura de datos - Funcionales a las activities***


    /**
     * Creo la lista de nombres de las imágenes de los valores a partir de todos billetes/monedas existentes,
     * ordenados por valor en dinero y luego por ID
     * */
    public ArrayList<String> obtainMoneyValueNamesOfValidCurrency(Context context){
        return orderMapOfValues(getValidCurrency(context));
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

        return valueNames;
    }


    //TODO: Implementar: Recorrer todos los valores en la billetera y sumar sus valores, luego devolver el valor total
    /**
     * Obtengo el saldo total actual en la billetera
     * */
    public String obtainTotalCreditInWallet(){

        String st_total = "20.00";

        return st_total;
    }

    //TODO: Implementar: Buscar con el ID entre todos los valores existentes y devolver el valor del mismo
    /**
     * Obtengo el saldo total actual en la billetera
     * */
    public String obtainValueFormID(String valueID){

        String st_total = "10.00";

        return st_total;
    }



    // TODO: Implementar Método: Devolver el importe de vuelto esperado
    /**
     * Devuelvo el importe de vuelto que debería recibir, en base al importe que se tiene que pagar
     * y en base a los billetes/monedas que serán usados para el pago (guardados en la billetera)
     * */
    public String expectedChangeValue(String valueToPay, Context context){

        String expectedChange = context.getString(R.string.value_10);

        return expectedChange;
    }


    /**
     * Devuelvo true si el valor del vuelto recibido es igual o mayor al vuelto esperado,
     * en base al vuelto recibido y al vuelto esperado
     * */
    public boolean isTotalChangeReceivedOk(String changeReceived, String changeExpected){
        return isValueAGreaterOrEqualThanValueB(changeReceived,changeExpected);
    }


    // TODO: Implementar Método: Devolver si corresponde recibir vuelto
    /**
     * Devuelvo true si corresponde recibir vuelto, en base al importe que se tiene que pagar
     * y en base a los billetes/monedas que serán usados para el pago (guardados en la billetera)
     * */
    public boolean isChangeExpected(String valueToPay){

        boolean isChangeExpected = true;

        return isChangeExpected;
    }



    // *** Escritura de datos - Funcionales a las activities***


    // TODO: Implementar: Guardar ArrayList de billetes/monedas en billetera, dado un vuelto recibido
    /**
     * Guardo en la billetera la lista de nombres de las imágenes
     * de los valores recibidos como vuelto
     * */
    public void saveChangeInWallet(ArrayList<String> listOfNames){


    }



    // *** TEMPORALES *** //TODO: Ver si tienen utilidad o se borran definitivamente


    /**
     * Borro el contenido entero del archivo de valores vigentes en circulación.
     * */
    private void deleteAllValidCurrency(Context context){
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = validCurrency.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

    }

    /**
     * Guardo los valores vigentes en circulación a mano, en el archivo pertinente
     * */
    public void initializeValidCurrencyManually(Context context){

        deleteAllValidCurrency(context);
        setValidCurrency(context,context.getString(R.string.tag_p5),"5");
        setValidCurrency(context,context.getString(R.string.tag_p5b),"5");
        setValidCurrency(context,context.getString(R.string.tag_p10),"10");
        setValidCurrency(context,context.getString(R.string.tag_p10b),"10");
        setValidCurrency(context,context.getString(R.string.tag_p20),"20");
        setValidCurrency(context,context.getString(R.string.tag_p20b),"20");
        setValidCurrency(context,context.getString(R.string.tag_p50),"50");
        setValidCurrency(context,context.getString(R.string.tag_p50b),"50");
        setValidCurrency(context,context.getString(R.string.tag_p100),"100");
        setValidCurrency(context,context.getString(R.string.tag_p100b),"100");
        setValidCurrency(context,context.getString(R.string.tag_p200),"200");
        setValidCurrency(context,context.getString(R.string.tag_p500),"500");
        setValidCurrency(context,context.getString(R.string.tag_p1000),"1000");
        setValidCurrency(context,context.getString(R.string.tag_c5),"0.05");
        setValidCurrency(context,context.getString(R.string.tag_c10),"0.10");
        setValidCurrency(context,context.getString(R.string.tag_c25),"0.25");
        setValidCurrency(context,context.getString(R.string.tag_c50),"0.50");
        setValidCurrency(context,context.getString(R.string.tag_p1),"1");
        setValidCurrency(context,context.getString(R.string.tag_p1_b),"1");
        setValidCurrency(context,context.getString(R.string.tag_p2),"2");
        setValidCurrency(context,context.getString(R.string.tag_p5_b),"5");
    }





}

