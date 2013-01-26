package com.nhs.easyprocedurelogger;

import roboguice.inject.InjectResource;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	@InjectResource (R.string.title_log) String titleLog;
	@InjectResource (R.string.title_tag) String titleTag;
	@InjectResource (R.string.title_stats) String titleStats;
	private Context context;
	
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        LogSection logSection = new LogSection();
        return logSection;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return this.context.getString(R.string.title_log);
            case 1:
                return this.context.getString(R.string.title_tag);
            case 2:
                return this.context.getString(R.string.title_stats);
        }
        return null;
    }
}