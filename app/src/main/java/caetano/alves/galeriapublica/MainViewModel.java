package caetano.alves.galeriapublica;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

// Classe herda de AndroidViewModel para poder acessar o contexto da aplicacao
public class MainViewModel extends AndroidViewModel {

    // Inteiro que identifica se o selecionado é grid ou list
    int navigationOpSelected = R.id.gridViewOp;

    // Construtor
    public MainViewModel(@NonNull Application application){
        super(application);
    }

    // Metodo para pegar o inteiro de identificacao
    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    // Metodo para mudar o inteiro de identificacao
    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
}
