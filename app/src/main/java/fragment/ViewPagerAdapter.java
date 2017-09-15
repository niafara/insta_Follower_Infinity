package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    FragmentManager fragmentManager;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        fragmentManager=manager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();

        Bundle b = new Bundle();
        fragment.setArguments(b);
        notifyDataSetChanged();

    }

    public void clearTabs(){

//       MyLog.i("231fragmentManager1", fragmentManager+"");
//        if ( mFragmentList != null ) {
//            for ( Fragment fragment : mFragmentList ) {
//               MyLog.i("231fragmentManager100 ", fragment+"");
////                fragmentManager.beginTransaction().remove(fragment).commit();
//            }
//           MyLog.i("231fragmentManager2", fragmentManager+"");
//            mFragmentList.clear();
//           MyLog.i("231fragmentManager3", fragmentManager + "");
//            mFragmentTitleList.clear();
//            notifyDataSetChanged();
//        }
//       MyLog.i("231fragmentManager4", fragmentManager + "");

        if ( mFragmentList != null ) {
            if (!mFragmentList.isEmpty()){
                mFragmentTitleList.clear();
                mFragmentTitleList.clear();
                notifyDataSetChanged();
            }
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
