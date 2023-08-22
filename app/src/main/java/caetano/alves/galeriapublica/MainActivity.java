 package caetano.alves.galeriapublica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.Manifest;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    static int RESULT_REQUEST_PERMISSION = 2;

    @Override
     protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

        bottomNavigationView = findViewById(R.id.btNav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                vm.setNavigationOpSelected(item.getItemId());
                switch (item.getItemId()){
                    case R.id.gridViewOp:
                        GridViewFragment gridViewFragment = GridViewFragment.newInstance();
                        setFragment(gridViewFragment);
                        break;
                    case R.id.listViewOp:
                        ListViewFragment listViewFragment = ListViewFragment.newInstance();
                        setFragment(listViewFragment);
                        break;
                }
                return true;
            }
        });
    }

    void setFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
     protected void onResume() {
        super.onResume();
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        checkForPermissions(permissions);
    }

     private void checkForPermissions(List<String> permissions) {
         // Lista de persmissões que o aplicativo não tem
         List<String> permissionsNotGranted = new ArrayList<>();

         // Para cada permisão que o app precisa
         for(String permission : permissions) {
             // Checa se tem a permissão
             if(!hasPermission(permission)) {
                 // Adiciona na lista de que não tem permissão
                 permissionsNotGranted.add(permission);
             }
         }

         // Verifica se a versão do kit de desenvolver é igual ou superior a versão marshmallow do android
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             // Se existirem permissões que não foram autorizadas
             if(permissionsNotGranted.size() > 0) {
                 // Solicita as permissões
                 requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]),RESULT_REQUEST_PERMISSION);
             }
             else {
                 MainViewModel  vm = new ViewModelProvider(this).get(MainViewModel.class);
                 int navigationOpSelected = vm.getNavigationOpSelected();
                 bottomNavigationView.setSelectedItemId(navigationOpSelected);
             }
         }
     }

     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         // Lista de permissões rejeitadas
         final List<String> permissionsRejected = new ArrayList<>();

         if(requestCode == RESULT_REQUEST_PERMISSION) {
             // Verifica se as permissões estão negadas
             for(String permission : permissions) {
                 if(!hasPermission(permission)) {
                     permissionsRejected.add(permission);
                 }
             }
         }

         // Se ainda estiver alguma permissão rejeitada
         if (permissionsRejected.size() > 0){
             // Verifica se a versão do kit de desenvolver é igual ou superior a versão marshmallow do android
             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                     // Alerta para pedir a permissão necessária
                     new AlertDialog.Builder(MainActivity.this).setMessage("Para usar essa app é preciso conceder essas permissões").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         // Pede as permissões mais uma vez
                         public void onClick(DialogInterface dialogInterface, int i) {
                             requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                         }
                     }).create().show();
                 }
             }
         }
         else {
             MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);
             int navigationOpSelected = vm.getNavigationOpSelected();
             bottomNavigationView.setSelectedItemId(navigationOpSelected);
         }
     }

     private boolean hasPermission(String permission) {
         // Verifica se a versão do kit de desenvolver é igual ou superior a versão marshmallow do android
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             // Checa se o aplicativo tem a permissão
             return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
         }
         return false;
     }
}