package proyectodane.usodeldinero;

import java.util.ArrayList;

public class PayManager {

    /**
     * Instancia de WalletManager
     */
    private static final WalletManager wm = WalletManager.getInstance();

    /**
     * Calcula los valores a utilizar como pago, para un importe de pago dado. A partir de los valores en la billetera.
     * Todos los valores String son tomados como importes en formato decimal.
     * El ArrayList <wallet> debe estar ordenado en forma ascendente
     * Se modifica <wallet> y <payment> en la operación.
     ***/
    public void obtainPayment(String paymentValue, ArrayList<String> wallet, ArrayList<String> payment){

        // Compruebo que la billetera no se encuentre vacía
        if(wallet.isEmpty()) return;

        // Inicializo el vector de pago
        payment.clear();

        // Calculo el pago de forma recursiva
        recursivePay(paymentValue,wallet,payment);

    }

    /**
     * Calcula de forma recursiva los valores a utilizar para un importe de pago dado.
     * El mismo trata de usar la menor cantidad de billetes posibles, entre los disponibles.
     * */
    private void recursivePay(String paymentValue, ArrayList<String> wallet, ArrayList<String> payment){

        // Obtengo el valor "óptimo" de pago
        String optimal = optimalValue(paymentValue,wallet);

        // Saco el valor óptimo de la billetera y lo paso al pago que se va a realizar
        wallet.remove(optimal);
        payment.add(optimal);

        // Si el óptimo no alcanza para pagar, busco otro valor mas para el pago, de forma recursiva
        if ( wm.isValueAGreaterThanValueB(paymentValue,optimal) ){
            paymentValue = wm.subtractValues(paymentValue,optimal);
            recursivePay(paymentValue,wallet,payment);
        }else{
            // Si el óptimo alcanza para el pago, finalizo el cálculo del mismo
            return;
        }
    }

    /**
    * Devuelve el valor que pueda usarse para pagar, de denominación mas bajo, o en su defecto
    * el valor mas grande de la lista.
    * Siempre recibe wallet con al menos un valor
    * */
    private String optimalValue(String payment, ArrayList<String> wallet){
        String previous = wallet.get(0);
        String current = wallet.get(0);

        for(String value : wallet) {
            current = value;

            // Si el valor de pago es mayor o igual al del valor actual, voy a cortar el ciclo...
            if ( wm.isValueAGreaterOrEqualThanValueB(payment,current) ){

                // Si son iguales, devuelvo el valor actual
                if ( wm.isValueAGreaterOrEqualThanValueB(current,payment)) {
                    return current;
                } else {
                    // Si el valor de pago es mayor al del actual, asigno el anterior
                    current = previous;
                    break;
                }
            }

            previous = current;
        }

        return current;

    }

}
