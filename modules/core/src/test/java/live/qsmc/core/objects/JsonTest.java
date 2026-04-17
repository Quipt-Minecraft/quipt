package live.qsmc.core.objects;


import live.qsmc.core.config.ConfigObject;

public class JsonTest extends ConfigObject {

    public String name;
    public int age;


    public JsonTest() {
    }

    public JsonTest(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}
