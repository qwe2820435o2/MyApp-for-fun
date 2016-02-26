package com.kri.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kri.base.BaseHolder;
import com.kri.bean.ItemInfoBean;
import com.kri.config.Constants;
import com.kri.utils.UIUtils;
import com.kris.app.R;
import com.squareup.picasso.Picasso;

public class ItemHolder extends BaseHolder<ItemInfoBean> {

	private ImageView mItemAppInfoIvIcon;
	private RatingBar mItemAppInfoRbStars;
	private TextView mItemAppInfoTvDes;
	private TextView mItemAppInfoTvSize;
	private TextView mItemAppInfoTvTitle;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_home_info, null);
		
		mItemAppInfoIvIcon = (ImageView) view.findViewById(R.id.item_appinfo_iv_icon);
		mItemAppInfoRbStars = (RatingBar) view.findViewById(R.id.item_appinfo_rb_stars);
		mItemAppInfoTvDes = (TextView) view.findViewById(R.id.item_appinfo_tv_des);
		mItemAppInfoTvSize = (TextView) view.findViewById(R.id.item_appinfo_tv_size);
		mItemAppInfoTvTitle = (TextView) view.findViewById(R.id.item_appinfo_tv_title);
		
		return view;
	}

	@Override
	public void refreshHolderView(ItemInfoBean data) {
		mItemAppInfoTvDes.setText(data.des);
		mItemAppInfoTvSize.setText(data.size+"");
		mItemAppInfoTvTitle.setText(data.name);
		
		mItemAppInfoRbStars.setRating(data.stars);
		Picasso.with(UIUtils.getContext())
				.load(Constants.URLS.IMGBASEURL + data.iconUrl).into(mItemAppInfoIvIcon);
	}

	

}
