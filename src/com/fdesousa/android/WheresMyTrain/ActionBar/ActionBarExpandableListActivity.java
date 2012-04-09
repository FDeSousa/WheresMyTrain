package com.fdesousa.android.WheresMyTrain.ActionBar;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * A base EnpandableListActivity that defers common functionality between
 * activities to and {@link ActionBarHelper}.
 * <p>
 * com.fdesousa.android.WheresMyTrain.ActionBar<br/>
 * ActionBarExpandableListActivity.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public abstract class ActionBarExpandableListActivity extends ExpandableListActivity {
	final ActionBarHelper mActionBarHelper;

	protected ActionBarExpandableListActivity() {
		mActionBarHelper = ActionBarHelper.createInstance(this);
	}
	
	protected ActionBarHelper getActionBarHelper() {
		return this.mActionBarHelper;
	}
	
	@Override
	public MenuInflater getMenuInflater() {
		return super.getMenuInflater();
	}
	
	/**{@inheritDoc}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBarHelper.onCreate(savedInstanceState);
    }

    /**{@inheritDoc}*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarHelper.onPostCreate(savedInstanceState);
    }

    /**
     * Base action bar-aware implementation for
     * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
     *
     * Note: marking menu items as invisible/visible is not currently supported.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = false;
        retValue |= mActionBarHelper.onCreateOptionsMenu(menu);
        retValue |= super.onCreateOptionsMenu(menu);
        return retValue;
    }

    /**{@inheritDoc}*/
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        mActionBarHelper.onTitleChanged(title, color);
        super.onTitleChanged(title, color);
    }

}
