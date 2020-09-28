package ppd.com.mubler.arch;

public interface BasePresenter <V extends BaseView>{
    void onViewAttach(V v);

    void onViewDetach();

    void onDestroy();
}
