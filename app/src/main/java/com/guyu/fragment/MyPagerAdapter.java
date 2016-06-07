package com.guyu.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class MyPagerAdapter extends PagerAdapter {
	/*
	 * 此处是把 MyPagerAdapter类，另外定义一个单独的类。在HomeActivity中调用。使得HomeActivity里的代码不显得臃肿
	 * 只需要在下面写上这个语句 获取下面代码中所需要的 imageList的数据就行了
	 */

	private ArrayList<ImageView> imageList;

	public MyPagerAdapter(ArrayList<ImageView> imageList) {
		this.imageList = imageList;

	}

	/*
	 * 获得页面的总数
	 */
	@Override
	public int getCount() {
		// return imageList.size();

		// 页面数高达21亿
		return Integer.MAX_VALUE;
	}

	/*
	 * 获得相应位置的view container view的容器，其实就是viewpager本身 position 相应的位置
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 给container添加view
		// container.addView(imageList.get(position));
		// 取余循环
		container.addView(imageList.get(position % imageList.size()));
		return imageList.get(position % imageList.size());
	}

	/* 判断view和object的对应关系 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		if (view == object) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 销毁对应位置上的object
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// System.out.println("destroyItem  ::"+position);

		container.removeView((View) object);
		object = null;
	}
}
