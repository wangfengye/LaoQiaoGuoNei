package com.yunbao.live.bean;

import java.io.File;

public class LiveLinkEntity {
    private String liveuid;
    private String link;
    private String shopId;

    private String stream;
    private String filelink;
    private File file;

    public LiveLinkEntity(String liveuid, String link, String stream, String filelink) {
        this.liveuid = liveuid;
        this.link = link;
        this.stream = stream;
        this.filelink = filelink;
    }


    public LiveLinkEntity(){

    }

    public String getLiveuid() {
        return liveuid;
    }

    public void setLiveuid(String liveuid) {
        this.liveuid = liveuid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getFilelink() {
        return filelink;
    }

    public void setFilelink(String filelink) {
        this.filelink = filelink;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setHost(String host){
        if(file!=null)
         filelink=host+file.getName();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
