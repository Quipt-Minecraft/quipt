package live.qsmc.quipt.core.data;

public class Wrapper<T> {

    private final T data;

    public Wrapper(T data){
        this.data = data;
    }

    public T data(){
        return data;
    }
}
