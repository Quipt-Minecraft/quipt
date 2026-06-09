package live.qsmc.quipt.core.charity;

public class CharityPlatform {

    public final String id;
    public final String name;

    public CharityPlatform(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String name(){
        return name;
    }

    public String id(){
        return id;
    }

}
