/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.ucomplex.ucomplex.Fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.lang.reflect.Field;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class AbsListViewBaseFragment extends BaseFragment {

	protected AbsListView listView;

	@Override
	public void onResume() {
		super.onResume();
		applyScrollListener();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	private void applyScrollListener() {

	}

	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
