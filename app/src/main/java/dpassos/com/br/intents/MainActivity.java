package dpassos.com.br.intents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Uri fotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btContatos = (Button)findViewById(R.id.btContatos);
        btContatos.setOnClickListener(this);

        Button btFotos = (Button)findViewById(R.id.btFotos);
        btFotos.setOnClickListener(this);

        Button btMapas = (Button)findViewById(R.id.btMapas);
        btMapas.setOnClickListener(this);

        Button btRestaurantes = (Button)findViewById(R.id.btRestaurantes);
        btRestaurantes.setOnClickListener(this);

        Button btRota = (Button)findViewById(R.id.btRota);
        btRota.setOnClickListener(this);

        Button btIntent = (Button)findViewById(R.id.btIntent);
        btIntent.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int actionCode = v.getId();
        switch (actionCode){
            case R.id.btContatos:
                exibirContatos();
                break;
            case R.id.btFotos:
                tirarFoto();
                break;
            case R.id.btMapas:
                exibirMapa();
                break;
            case R.id.btRestaurantes:
                exibirRestaurantes();
                break;
            case R.id.btRota:
                exibirRota();
                break;
            case R.id.btIntent:
                abrirIntent();
                break;
        }
    }

    private void abrirIntent() {
        Intent it = new Intent("MINHA_INTENT");
        startActivity(it);
    }

    private void exibirRota() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=Recife+Jardim+São+Paulo+Leandro+Barreto,+Recife+Avenida+Conde+Boa+Vista");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void exibirMapa() {
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void exibirRestaurantes(){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fotoUri = getFotoUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);

        // start the image capture Intent
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1){
            ImageView image = new ImageView(this);
            image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            image.setImageBitmap(BitmapFactory.decodeFile(fotoUri.getPath()));

            Toast t = new Toast(this);

            t.setView(image);
            t.setDuration(Toast.LENGTH_LONG);
            t.show();
        }
    }

    private void exibirContatos() {
        Uri uri = Uri.parse("content://com.android.contacts/contacts/");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }


    private Uri getFotoUri(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(getString(R.string.app_name), "Falha ao criar diretorio");
                return null;
            }
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return Uri.fromFile(mediaFile);
    }
}
