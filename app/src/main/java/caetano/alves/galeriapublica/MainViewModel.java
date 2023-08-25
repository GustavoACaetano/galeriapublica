package caetano.alves.galeriapublica;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import kotlinx.coroutines.CoroutineScope;

// Classe herda de AndroidViewModel para poder acessar o contexto da aplicacao
public class MainViewModel extends AndroidViewModel {

    LiveData<PagingData<ImageData>> pageLv;

    // Inteiro que identifica se o selecionado é grid ou list
    int navigationOpSelected = R.id.gridViewOp;

    // Construtor
    public MainViewModel(@NonNull Application application){
        super(application);
        GalleryRepository galleryRepository = new GalleryRepository(application);
        GalleryPagingSource galleryPagingSource = new GalleryPagingSource(galleryRepository);
        Pager<Integer, ImageData> pager = new Pager(new PagingConfig(10), () -> galleryPagingSource);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public LiveData<PagingData<ImageData>> getPageLv() {
        return pageLv;
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
