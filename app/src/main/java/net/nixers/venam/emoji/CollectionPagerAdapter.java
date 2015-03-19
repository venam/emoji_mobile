package net.nixers.venam.emoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Choreographer;

public class CollectionPagerAdapter extends FragmentPagerAdapter {
    public Fragment emojiListFragment;
    public Fragment mainFragment;
    public MainActivity parent;

    public Fragment getEmojiListFragment() {
        return  emojiListFragment;
    }
    public Fragment getMainFragment(){
        return mainFragment;
    }

    public CollectionPagerAdapter(FragmentManager fm, MainActivity parent) {
        super(fm);
        this.parent = parent;
        emojiListFragment = new EmojiListFragment((MainActivity) parent);
        mainFragment = new MainFragment((EmojiListFragment)emojiListFragment);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        return (i==0?mainFragment: emojiListFragment);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

}
