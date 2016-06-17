package br.com.appviral.abastece;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;

@SuppressWarnings("deprecation")
public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //adiconaAtalho();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_abastecimentos).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<Abastecimento> listaApresentar = (new AbastecimentoDAO(this)).listarAbastecimentos();
        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(this, listaApresentar);
        mRecyclerView.setAdapter(adaptadorAbastecimento);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public void abreRegistraAbastecimento(View view) {
        Intent intent = new Intent(this, AbastecerActivity.class);
        intent.putExtra("OPERACAO", Abastecimento.INSERIR);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.abastercer:
                abreRegistraAbastecimento(null);
                return true;
            case R.id.action_sobre:
                startActivity(new Intent(getApplicationContext(), SobreActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_abastecimentos:
                //criaAbreFragmento("nav_abastecimentos", AdaptadorAbastecimento.COM_CLICK);
                break;
            case R.id.nav_abastecer:
                abreRegistraAbastecimento(null);
                break;
            case R.id.nav_calculo_flex:
                startActivity(new Intent(getApplicationContext(), CalculoFlexActivity.class));
                break;
            case R.id.nav_sobre:
                startActivity(new Intent(getApplicationContext(), SobreActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void adiconaAtalho() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("br.com.appviral.abastece.PREFERENCE_FILE", Context.MODE_PRIVATE);
        boolean atalho = sharedPref.getBoolean("ATALHO", false);

        if (!atalho) {
            Intent atalhoIntent = new Intent(getApplicationContext(), PrincipalActivity.class);
            atalhoIntent.setAction(Intent.ACTION_MAIN);
            Intent adicionaIntent = new Intent();
            adicionaIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, atalhoIntent);
            adicionaIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Abastece");
//            adicionaIntent.putExtra("duplicate", false);
            adicionaIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
            adicionaIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            sendBroadcast(adicionaIntent);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("ATALHO", true);
            editor.commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AdaptadorAbastecimento) mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Principal Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://br.com.appviral.abastece/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Principal Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://br.com.appviral.abastece/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
