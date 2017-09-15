package data;

/**
 * Created by pc on 8/11/2016.
 */
public class Award {

    int title_res;
    int subTitle_res;
    int icon_res;
    int type;

    public Award() {
    }

    public Award(int title_res, int subTitle_res, int icon_res, int type) {
        this.title_res = title_res;
        this.subTitle_res = subTitle_res;
        this.icon_res = icon_res;
        this.type = type;
    }

    public int getTitle_res() {
        return title_res;
    }

    public void setTitle_res(int title_res) {
        this.title_res = title_res;
    }

    public int getSubTitle_res() {
        return subTitle_res;
    }

    public void setSubTitle_res(int subTitle_res) {
        this.subTitle_res = subTitle_res;
    }

    public int getIcon_res() {
        return icon_res;
    }

    public void setIcon_res(int icon_res) {
        this.icon_res = icon_res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
