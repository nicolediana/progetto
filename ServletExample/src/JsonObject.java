import com.google.gson.Gson;


/**
 * Created by pcnicole on 30/10/2014.
 */

public abstract class JsonObject {

  public String toJson() {
    Gson gson = new Gson();

    return gson.toJson(this);
  }

  /*public static<Child> Child fromJson(Class<Child> childClass, String json) {
    Gson gson = new Gson();

    return gson.fromJson(json, childClass);
  }*/

}
