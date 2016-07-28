package vip.chengchao.tools.mypwnode.model;

/**
 * Created by chengchao on 16/7/28.
 */
public class FileDesc {
    private int type;
    private String pathName;

    public FileDesc(int type, String pathName) {
        this.type = type;
        this.pathName = pathName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
