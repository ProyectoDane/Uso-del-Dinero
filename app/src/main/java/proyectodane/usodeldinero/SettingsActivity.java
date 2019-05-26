package proyectodane.usodeldinero;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import androidx.appcompat.app.ActionBar;
import android.preference.PreferenceFragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import androidx.core.app.NavUtils;
import java.util.List;


/**
 * Un PreferenceActivity que representa un conjunto de opciones de aplicación.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Context context;

    /**
     * Un listener de cambio de valores que actualiza el resumen de preferencias con el nuevo valor
     */
    private static Preference.OnPreferenceClickListener sBindPreferenceClickListener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(final Preference preference) {

            if( preference.getKey().equals(context.getString(R.string.pref_key_add_value)) ){

                Intent intent = new Intent(context, NewCurrencyActivity.class);
                context.startActivity(intent);

            }
            else if( preference.getKey().equals(context.getString(R.string.pref_key_remove_value)) ){

                Intent intent = new Intent(context, DeleteCurrencyActivity.class);
                context.startActivity(intent);

            }

            return true;
        }
    };



    /**
     * Determina si un dispositivo tiene pantalla extra grande (por ejemplo 10")
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        context = this;
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Muestro la flecha atrás en el actionbar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }


    /**
     * Cargo los headers desde el archivo xml, luego desde aquí se cargan los fragment vinculados
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }


    /**
     * Verifica los fragment válidos
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || AddRemoveValuePreferenceFragment.class.getName().equals(fragmentName);
    }



    /**
     * PreferenceFragment dedicado al agregado o remoción de nuevos valores existentes (Billetes o Monedas)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AddRemoveValuePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_add_remove_value);
            setHasOptionsMenu(true);

            // Vinculo cada Preference que necesite ser escuchado cuando se realiza un click sobre el
            findPreference(context.getString(R.string.pref_key_add_value)).setOnPreferenceClickListener(sBindPreferenceClickListener);
            findPreference(context.getString(R.string.pref_key_remove_value)).setOnPreferenceClickListener(sBindPreferenceClickListener);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
