package com.yunbao.main.expand;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.yunbao.common.bean.CouponEntity;
import com.yunbao.common.bean.ShopResponse;
import com.yunbao.common.expand.ButterKnifeAbsViewHolder;
import com.yunbao.common.expand.RefreshView;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.ShopCallBack;
import com.yunbao.main.R;
import com.yunbao.main.R2;
import com.yunbao.main.dialog.QrGouponDialog;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MyCouponViewHolder extends ButterKnifeAbsViewHolder {
    @BindView(R2.id.refreshView)
    public RefreshView refreshView;


    private int type;
    private MyCouponAdapter adapter;
    private CouponLisnter couponLisnter;


    public MyCouponViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_my_coupon;
    }
    @Override
    public void initView() {
        super.initView();
        adapter=new MyCouponAdapter(null);
        refreshView.setAdapter(adapter);
        refreshView.defaultSetting();
        refreshView.setLoadMoreEnable(false);
        refreshView.setDataListner(new RefreshView.DataListner<CouponEntity>() {
            @Override
            public Observable<List<CouponEntity>> loadData(int p) {
                return request(p);
            }
            @Override
            public void compelete(List data) {

            }
            @Override
            public void error(Throwable e) {
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QrGouponDialog qrGouponDialog=new QrGouponDialog();
                qrGouponDialog.setCouponEntity((CouponEntity) adapter.getItem(position));
                FragmentActivity activity= (FragmentActivity)mContext;
                qrGouponDialog.show(activity.getSupportFragmentManager(),"QrGouponDialog");
                ;
            }
        });
    }

    public void initData(){
        refreshView.initData();
    }
    @Override
    public void onFirstVisibiby() {
        super.onFirstVisibiby();
        initData();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if(adapter!=null){
            adapter.setType(type);
        }
    }


    public Observable<List<CouponEntity>>request(final int p){
        return  Observable.create(new ObservableOnSubscribe<List<CouponEntity>>() {
           @Override
           public void subscribe(final ObservableEmitter<List<CouponEntity>> e) throws Exception {
                CommonHttpUtil.getUserCoupon(type, new ShopCallBack() {
                    @Override
                    public void success(ShopResponse shopResponse) {
                        if(shopResponse.getStatus()==1){
                            e.onNext(shopResponse.getCoupons());
                        }
                        if(couponLisnter!=null){
                            couponLisnter.coupon(shopResponse.getType1(),shopResponse.getType2(),shopResponse.getType3());
                        }
                        e.onComplete();
                    }
                    @Override
                     public void onError(Response<ShopResponse> response) {
                                super.onError(response);
                                e.onError(response.getException());
                            }
                        }
                );
           }
       });
    }

    public CouponLisnter getCouponLisnter() {
        return couponLisnter;
    }

    public void setCouponLisnter(CouponLisnter couponLisnter) {
        this.couponLisnter = couponLisnter;
    }
    public interface CouponLisnter{
        public void coupon(int size1,int size2,int size3);
    }
}
