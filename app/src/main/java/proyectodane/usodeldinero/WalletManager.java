package proyectodane.usodeldinero;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
     * Key: ID moneda (String)  Value: Cantidad de unidades de esa moneda (String).
     * */
    private SharedPreferences currencyInWallet;

    // Uso singleton
    private WalletManager() { }
    public static WalletManager getInstance() {
        return ourInstance;
    }



    //***************************************************
    //* Métodos de guardado, borrado y lectura privados *
    //***************************************************


    /**
     * Obtengo un Map con los valores de los billetes/monedas vigentes en circulación.
     * */
    private Map <String,String> getValidCurrency(Context context) {
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        return (Map<String,String>)validCurrency.getAll();
    }

    /**
     * Persisto en el archivo con un nuevo valor de billete/moneda vigente en circulación.
     * */
    private void setValidCurrency(Context context,String currencyID, String currencyValue){
        String validCurrencyFileName = context.getString(R.string.valid_currency_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = validCurrency.edit();

        // Agrego el contenido en el archivo
        editor.putString(currencyID,currencyValue);
        editor.apply();

    }

    //TODO: Implementar baja de valores en circulación (Release 5)

    /**
     * Consulto si es la primera vez que se inicializa el archivo de valores.
     * Devuelve false si ya fue consultado anteriormente, y true si nunca fue consultado
     * Si devuelve true, se guarda en el archivo el uso por primera vez
     * */
    private boolean isThisTheFirstTimeAccess(Context context){
        String validCurrencyFileName = context.getString(R.string.settings_shared_preferences_file_name);
        validCurrency = context.getSharedPreferences(validCurrencyFileName,0);

        if(validCurrency.getBoolean(context.getString(R.string.first_time_access), true)) {
            SharedPreferences.Editor editor = validCurrency.edit();
            editor.putBoolean(context.getString(R.string.first_time_access), false);
            editor.apply();
            return true;
        }

        return false;
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
     * Persisto en el archivo con un nuevo valor de billete/moneda guardado en billetera. (Aumento el contador del ID en 1)
     * */
    private void setCurrencyInWallet(Context context, String currencyID){
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        SharedPreferences.Editor editor = currencyInWallet.edit();

        BigDecimal actualValue = new BigDecimal(obtainQuantityFromCurrencyInWallet(context,currencyID));
        BigDecimal newValue = actualValue.add(BigDecimal.ONE);

        // Agrego el contenido en el archivo
        editor.putString(currencyID,newValue.toPlainString());
        editor.apply();
    }

    /**
     * Modifico en el archivo con una nueva cantidad de valores de billete/moneda guardado en billetera (Disminuyo el contador del ID en 1)
     * */
    private void removeCurrencyInWallet(Context context, String currencyID){
        String currencyInWalletFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(currencyInWalletFileName,0);
        SharedPreferences.Editor editor = currencyInWallet.edit();

        // Obtengo el valor actual y si corresponde, le resto uno a su contador
        BigDecimal actualValue = new BigDecimal(obtainQuantityFromCurrencyInWallet(context,currencyID));
        BigDecimal newValue = new BigDecimal(actualValue.toPlainString());
        if( isGreaterThanValueZero(actualValue.toPlainString()) ){
            newValue = actualValue.subtract(BigDecimal.ONE);
        }

        // Agrego el contenido en el archivo
        editor.putString(currencyID,newValue.toPlainString());
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


    /**
     * Ordeno un ArrayList de valores de billetes/monedas en forma descendente
     * */
    private void orderListOfValuesDescending(ArrayList <String> listOfValues){

        Collections.sort(listOfValues, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {

                // Obtengo los valores en dinero a formato BigDecimal
                BigDecimal bd_value_s1 = new BigDecimal(s1);
                BigDecimal bd_value_s2 = new BigDecimal(s2);

                // Invierto el orden a descendente, cambiando el resultado de la comparación
                return (-1) * bd_value_s1.compareTo(bd_value_s2);
            }
        });

    }


    /**
     * Devuelvo la cantidad de veces que aparece un valor dado un ID de moneda, en base a lo
     * guardado en el archivo de billetes/monedas en la billetera
     * */
    private String obtainQuantityFromCurrencyInWallet(Context context, String currencyID){
        Map <String,String> currencyInWalletMap = getCurrencyInWallet(context);

        for (Map.Entry<String,String> entry : currencyInWalletMap.entrySet()) {
            if (currencyID.equals(entry.getKey())) {
                return entry.getValue();
            }
        }

        return BigDecimal.ZERO.toPlainString();
    }


    /**
     * Creo la lista de los valores a usar para el pago
     * a partir de todos billetes/monedas guardados en la billetera y del valor del importe a pagar.
     * Los ArrayList <valuesIDInWallet> y <valuesInWallet> deben ser instanciados antes de ser pasados como parámetro, luego
     * los mismos quedarán cargados con los valores correspondientes a el ID y Valores.
     * */
    private ArrayList<String> obtainMoneyValuesOfPayment(Context context, String valueToPay, ArrayList<String> valuesIDInWallet, ArrayList<String> valuesInWallet){

        // Agrego la lista de ID de los billetes/monedas en la billetera
        valuesIDInWallet.clear();
        valuesIDInWallet.addAll(obtainMoneyValueNamesInWallet(context));

        // Cargo un ArrayList con los valores de los billetes/monedas en la billetera
        valuesInWallet.clear();
        for(String currentID : valuesIDInWallet) {
            valuesInWallet.add(obtainValueFormID(context,currentID));
        }

        // Calculo el pago a partir de los valores en la billetera y del monto a pagar, en un nuevo ArrayList
        ArrayList<String> valuesInWalletCopy = new ArrayList<String>(valuesInWallet);
        orderListOfValuesDescending(valuesInWalletCopy);
        ArrayList<String> valuesToPay = new ArrayList<String>();
        PayManager pm = new PayManager();
        pm.obtainPayment(valueToPay,valuesInWalletCopy,valuesToPay);

        return valuesToPay;
    }


    // TODO: (Ver si es necesario) Implementar Método: Sacar todo de la billetera.
    /**
     * Quito todos los valores existentes en la billetera, la vacío.
     * */
    private void removeAllCurrencyFromWallet(){

    }


    /**
     * Carga en un ArrayList todos los ID correspondientes a los valores de la billetera que se usarán para el pago
     * */
    private ArrayList<String> loadValueToIDInWallet(ArrayList<String> valuesIDInWallet, ArrayList<String> valuesInWallet, ArrayList<String> valuesToPay){

        // Creo el listado de ID resultantes
        ArrayList<String> valueIDToPay = new ArrayList<String>();

        // Preparo un Array de pares para trabajar con el ID y su valor
        ArrayList< Pair <String,String> > pairWallet = new ArrayList<Pair <String,String>>();
        for (int i = 0; i < valuesIDInWallet.size(); i++){
            Pair <String,String> pair = new Pair <String,String>(valuesIDInWallet.get(i),valuesInWallet.get(i));
            pairWallet.add(pair);
        }

        // Recorro todos los valores a usar para el pago y obtengo su ID
		for(String currentValueToPay : valuesToPay) {
            String result = popFromStringPair(pairWallet,currentValueToPay);
            if( !result.equals("") ){
                valueIDToPay.add(result);
            }
		}

        return valueIDToPay;
    }


    /**
     * Busca y devuelve un valor String de un array de Pair según el <value>, si lo encuentra lo quita del mismo (Saca el Pair)
     * */
    private String popFromStringPair(ArrayList< Pair <String,String> > pairWallet, String value){

        for (int i = 0; i < pairWallet.size(); i++) {
            if( value.equals(pairWallet.get(i).second) ){
                String result = pairWallet.get(i).first;
                pairWallet.remove(i);
                return result;
            }
        }

        return "";
    }


    //****************************************
    //* Métodos de carga y guardado públicos *
    //****************************************


    // *** Escritura de datos - Funcionales a las activities***


    // * Alta y Baja de nuevos valores *

    /**
     * Guardo un nuevo billete/moneda vigente para ser usado luego como moneda nueva
     * de la billetera
     * */
    public void addNewCurrency(Context context, String idCurrency, String currencyValue){
        setValidCurrency(context,idCurrency,currencyValue);
    }


    // TODO: Implementar Método en Release 5: Borrar billete/moneda actual (Input: ID_moneda (Str))
    /**
     * Borro un billete/moneda vigente, el cual dejará de ser usado como moneda de pago
     * de la billetera
     * */
    public void deleteExistingCurrency(String idCurrency){
        // Usar el método privado
    }



    // * Manejo de los valores actuales en la billetera *


    /**
     * Guardo en la billetera la lista de nombres de las imágenes (ID)
     * de los valores recibidos como vuelto
     * */
    public void saveChangeInWallet(Context context, ArrayList<String> listOfID){

        for(String currentID : listOfID) {
            setCurrencyInWallet(context,currentID);
        }

    }


    /**
     * Agrego un billete/moneda a la billetera
     * */
    public void addCurrencyInWallet(Context context, String idCurrency){
        setCurrencyInWallet(context,idCurrency);
    }


    /**
     * Quito un billete/moneda de la billetera
     * */
    public void removeFromWalletCurrencyUsedToPay(Context context, String totalPurchaseValue){

        //Cargo la lista con los ID usados para el pago
        ArrayList<String> listOfValueID = obtainMoneyValueNamesOfPayment(context,totalPurchaseValue);

        // Remuevo todos los valores de la billetera
        for(String currentValueID : listOfValueID) {
            removeCurrencyInWallet(context,currentValueID);
        }

    }


    /**
     * Consulto si es la primera vez que se inicializa el archivo de valores.
     * Si es la primera vez, cargo los valores de los billetes/monedas vigentes en circulación por defecto
     * */
    public void checkFirstRun(Context context){
        if(isThisTheFirstTimeAccess(context)){
            initializeValidCurrencyManually(context);
            initializeFilesOfValidCurrencyManually(context);
        }
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
    public boolean isValueAGreaterThanValueB(String valueA, String valueB) {

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
    public boolean isGreaterThanTotalWallet(Context context, String val) {
        return isValueAGreaterThanValueB(val,obtainTotalCreditInWallet(context));
    }


    /**
     * Verdadero si el valueA (con formato numérico de importe) es igual o mayor
     * que el valueB (con formato numérico de importe)
     * */
    public boolean isValueAGreaterOrEqualThanValueB(String valueA, String valueB) {

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
    public boolean isGreaterThanValueZero(String value) {
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


    /**
     * Resto dos valores: val_1 y val_2 (con formato numérico de importe)
     * Y devuelvo un string con el resultado (con formato numérico de importe), el cual
     * tiene como formato establecido un máximo de 2 dígitos decimales,
     * redondeando hacia abajo (truncado) en los casos donde sea necesario
     * */
    public String subtractValues(String val_1, String val_2){

        // Instancio los BigDecimal
        BigDecimal bd_v1 = new BigDecimal(val_1);
        BigDecimal bd_v2 = new BigDecimal(val_2);

        // Formato: Dejo (a lo sumo) dos decimales del número ingresado, truncando el resto
        bd_v1 = bd_v1.setScale(2, RoundingMode.FLOOR);
        bd_v2 = bd_v2.setScale(2, RoundingMode.FLOOR);

        // Sumo los valores
        BigDecimal result = bd_v1.subtract(bd_v2);

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


    /**
     * Creo la lista de ID de imágenes de los valores a usar para el pago
     * a partir de todos billetes/monedas guardados en la billetera y del valor del importe a pagar
     * */
    public ArrayList<String> obtainMoneyValueNamesOfPayment(Context context, String valueToPay){

        // Instancio los ArrayList necesarios para el procesamiento
        ArrayList<String> valuesIDInWallet = new ArrayList<String>();
        ArrayList<String> valuesInWallet = new ArrayList<String>();

        // Obtengo los valores a usar para el pago
        ArrayList<String> valuesToPay = obtainMoneyValuesOfPayment(context,valueToPay,valuesIDInWallet,valuesInWallet);

        // Obtengo y devuelvo el ID de cada uno de los valores a usar para pagar
        return loadValueToIDInWallet(valuesIDInWallet,valuesInWallet,valuesToPay);
    }


    /**
     * Creo la lista de nombres de las imágenes de los valores actualmente en la billetera
     * a partir de todos billetes/monedas guardados en la billetera
     * */
    public ArrayList<String> obtainMoneyValueNamesInWallet(Context context){

        // Obtengo el Map con ID y cantidad de veces que aparece el ID en la billetera
        Map <String,String> mapIdQuantity = getCurrencyInWallet(context);

        // Creo y cargo un Map con ID y valor correspondiente, para luego ser ordenado
        Map <String,String> mapIdValue = new HashMap <String,String>();
        for (Map.Entry<String,String> entry : mapIdQuantity.entrySet()) {
            mapIdValue.put(entry.getKey(),obtainValueFormID(context,entry.getKey()));
        }

        // Obtengo los ID ordenados
        ArrayList<String> listOfID = orderMapOfValues(mapIdValue);

        // Creo y cargo un ArrayList con todos los ID y la cantidad de veces que aparecen en la billetera
        ArrayList<String> listInWallet = new ArrayList<String>();
        for(String currentID : listOfID) {

            // Asigno la cantidad de veces que aparece un ID
            String quantity = BigDecimal.ZERO.toPlainString();
            for (Map.Entry<String,String> entry : mapIdQuantity.entrySet()) {
                if (currentID.equals(entry.getKey())) {
                    quantity = entry.getValue();
                }
            }

            // Cargo el ArrayList de acuerdo a la cantidad de veces que aparece un ID
            BigDecimal bdQuantity = new BigDecimal(quantity);
            int intQuantity = bdQuantity.intValue();
            for(int i=0; i<intQuantity; i++) {
                listInWallet.add(currentID);
            }

        }

        return listInWallet;
    }


    /**
     * Obtengo el saldo total actual en la billetera
     * */
    public String obtainTotalCreditInWallet(Context context){

        String total = BigDecimal.ZERO.toPlainString();

        // Recorro el ArrayList sumando todos los valores de cada billete/moneda
        ArrayList<String> idInWallet = obtainMoneyValueNamesInWallet(context);
        for(String currentID : idInWallet) {
            String newValue = obtainValueFormID(context,currentID);
            total = addValues(total,newValue);
        }

        return total;
    }


    /**
     * Obtengo el valor de un billete/moneda a partir de su ID.
     * Si el ID no existe, devuelve valor cero
     * */
    public String obtainValueFormID(Context context, String valueID){

        Map <String,String> validCurrency = getValidCurrency(context);

        for (Map.Entry<String,String> entry : validCurrency.entrySet()) {
            if (valueID.equals(entry.getKey())) {
                return entry.getValue();
            }
        }

        return BigDecimal.ZERO.toPlainString();

    }


    /**
     * Devuelvo el importe de vuelto que debería recibir, en base al importe que se tiene que pagar
     * y en base a los billetes/monedas que serán usados para el pago (guardados en la billetera)
     * */
    public String expectedChangeValue(Context context,String exactValueToPay){


        // Instancio los ArrayList necesarios para el procesamiento de valores de pago
        ArrayList<String> valuesIDInWallet = new ArrayList<String>();
        ArrayList<String> valuesInWallet = new ArrayList<String>();

        // Obtengo los valores a usar para el pago
        ArrayList<String> valuesToPay = obtainMoneyValuesOfPayment(context,exactValueToPay,valuesIDInWallet,valuesInWallet);

        // Calculo el importe correspondiente el pago con valores a realizar
        String totalPayment = BigDecimal.ZERO.toPlainString();
        for(String currentValueToPay : valuesToPay) {
            totalPayment = addValues(totalPayment,currentValueToPay);
        }

        // Devuelvo la resta entre el pago con billetes/monedas que voy a realizar, y el pago exacto de la compra
        return subtractValues(totalPayment,exactValueToPay);
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
     * y en base al cambio esperado
     * */
    public boolean isChangeExpected(Context context, String exactValueToPay){
        return (isGreaterThanValueZero(expectedChangeValue(context,exactValueToPay)));
    }



    // *** TEMPORALES *** //TODO: Ver si tienen utilidad o se borran definitivamente


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

    /**
     * Guardo las imágenes por defecto en el almacenamiento interno
     * */
    public void initializeFilesOfValidCurrencyManually(Context context){

        ImageManager im = new ImageManager();
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p5));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p5b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p10));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p10b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p20));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p20b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p50));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p50b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p100));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p100b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p200));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p500));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p1000));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_c5));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_c10));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p5));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_c25));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_c50));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p1));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p1_b));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p2));
        im.saveDefaultDrawableImageJPEGToInternalStorage(context,context.getString(R.string.tag_p5_b));
    }

    /**
     * Guardo los valores en billetera "a mano", en el archivo pertinente
     * */
    public void initializeWalletManually(Context context) {
        deleteAllCurrencyInWallet(context);
        setCurrencyInWallet(context,context.getString(R.string.tag_p20));

    }

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
     * Borro el contenido entero del archivo de valores en la billetera.
     * */
    private void deleteAllCurrencyInWallet(Context context){
        String validCurrencyFileName = context.getString(R.string.currency_in_wallet_shared_preferences_file_name);
        currencyInWallet = context.getSharedPreferences(validCurrencyFileName,0);
        SharedPreferences.Editor editor = currencyInWallet.edit();

        // Borro contenido anterior
        editor.clear();
        editor.apply();

    }

}

