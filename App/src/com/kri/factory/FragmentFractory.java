package com.kri.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kri.base.BaseFragment;
import com.kri.base.SuperBaseFragment;
import com.kris.fragment.AppFragment;
import com.kris.fragment.CategoryFragment;
import com.kris.fragment.GameFragment;
import com.kris.fragment.HomeFragment;
import com.kris.fragment.SubjectFragment;

import android.support.v4.app.Fragment;

/**
 * 
 * TODO<Fragment工厂>
 * @author  LuLiLi
 * @data:  2016-1-24 上午9:57:36
 * @version:  V1.0
 */
public class FragmentFractory {
	
	
	public static final int FRAGMENT_HOME=0;
	public static final int FRAGMENT_APP=1;
	public static final int FRAGMENT_GAME=2;
	public static final int FRAGMENT_SUBJECT=3;
	public static final int FRAGMENT_CATEGORY=4;
	
	
	
	private static Map<Integer,SuperBaseFragment> mFragmentMap=new HashMap<Integer, SuperBaseFragment>();
	
	public static SuperBaseFragment creatFragment(int index){
		SuperBaseFragment fragment=null;
		
		if (mFragmentMap.containsKey(index)) {
			fragment=mFragmentMap.get(index);
			return fragment;
		}
		
		switch (index) {
		case FRAGMENT_HOME:
			fragment=new HomeFragment();
			break;
		case FRAGMENT_APP:
			fragment=new AppFragment();
			
			break;
		case FRAGMENT_GAME:
			fragment=new GameFragment();
			
			break;
		case FRAGMENT_SUBJECT:
			fragment=new SubjectFragment();
			
			break;
		case FRAGMENT_CATEGORY:
			fragment=new CategoryFragment();
			
			break;

		default:
			break;
		}
		
		//得到一个fragment就加入集合中
		mFragmentMap.put(index, fragment);
		
		return fragment;
	}
}
