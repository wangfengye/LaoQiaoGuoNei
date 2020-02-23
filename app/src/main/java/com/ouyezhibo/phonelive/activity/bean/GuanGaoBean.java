package com.ouyezhibo.phonelive.activity.bean;

import java.util.List;

public class GuanGaoBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":{"list":[{"slide_id":"25","slide_cid":"1","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9ab319801.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"},{"slide_id":"22","slide_cid":"2","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9a992a797.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"},{"slide_id":"34","slide_cid":"2","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9a35766f8.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"}]}}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : {"list":[{"slide_id":"25","slide_cid":"1","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9ab319801.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"},{"slide_id":"22","slide_cid":"2","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9a992a797.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"},{"slide_id":"34","slide_cid":"2","slide_name":"欧夜","slide_pic":"http://qiniu.kulehu.com/20190502/5cca9a35766f8.jpg","slide_url":"https://fir.im/5wy3","slide_des":"","slide_content":"","slide_status":"1","listorder":"0"}]}
         */

        private int code;
        private String msg;
        private InfoBean info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            private List<ListBean> list;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * slide_id : 25
                 * slide_cid : 1
                 * slide_name : 欧夜
                 * slide_pic : http://qiniu.kulehu.com/20190502/5cca9ab319801.jpg
                 * slide_url : https://fir.im/5wy3
                 * slide_des :
                 * slide_content :
                 * slide_status : 1
                 * listorder : 0
                 */

                private String slide_id;
                private String slide_cid;
                private String slide_name;
                private String slide_pic;
                private String slide_url;
                private String slide_des;
                private String slide_content;
                private String slide_status;
                private String listorder;

                public String getSlide_id() {
                    return slide_id;
                }

                public void setSlide_id(String slide_id) {
                    this.slide_id = slide_id;
                }

                public String getSlide_cid() {
                    return slide_cid;
                }

                public void setSlide_cid(String slide_cid) {
                    this.slide_cid = slide_cid;
                }

                public String getSlide_name() {
                    return slide_name;
                }

                public void setSlide_name(String slide_name) {
                    this.slide_name = slide_name;
                }

                public String getSlide_pic() {
                    return slide_pic;
                }

                public void setSlide_pic(String slide_pic) {
                    this.slide_pic = slide_pic;
                }

                public String getSlide_url() {
                    return slide_url;
                }

                public void setSlide_url(String slide_url) {
                    this.slide_url = slide_url;
                }

                public String getSlide_des() {
                    return slide_des;
                }

                public void setSlide_des(String slide_des) {
                    this.slide_des = slide_des;
                }

                public String getSlide_content() {
                    return slide_content;
                }

                public void setSlide_content(String slide_content) {
                    this.slide_content = slide_content;
                }

                public String getSlide_status() {
                    return slide_status;
                }

                public void setSlide_status(String slide_status) {
                    this.slide_status = slide_status;
                }

                public String getListorder() {
                    return listorder;
                }

                public void setListorder(String listorder) {
                    this.listorder = listorder;
                }
            }
        }
    }
}
