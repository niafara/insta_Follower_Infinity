package data;

/**
 * Created by pc on 9/16/2016.
 */
public class AboutUsItem {

    private String name;
    private int icon;

    public AboutUsItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }


    public AboutUsItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
