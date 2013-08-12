/*
    This file is part of Project MAXS.

    MAXS and its modules is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MAXS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MAXS.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.projectmaxs.main.receivers;

import org.projectmaxs.main.ModuleRegistry;
import org.projectmaxs.shared.global.GlobalConstants;
import org.projectmaxs.shared.global.util.IntentUtil;
import org.projectmaxs.shared.global.util.Log;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class PackageReceiver extends BroadcastReceiver {

	private static final Log LOG = Log.getLog();
	private static final String[] sReceivers = new String[] { ".ModuleReceiver", ".TransportReceiver" };

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String packageName = IntentUtil.getOriginPackage(intent);
		if (action.equals(android.content.Intent.ACTION_PACKAGE_ADDED)
				|| action.equals(android.content.Intent.ACTION_PACKAGE_REPLACED)) {
			onInstalledOrReplaced(context, packageName);
		}
		else if (action.equals(android.content.Intent.ACTION_PACKAGE_REMOVED)) {
			onRemoved(context, packageName);
		}
	}

	private void onInstalledOrReplaced(Context context, String packageName) {
		LOG.d("onInstalledOrReplaced: packageName=" + packageName + " intent=" + GlobalConstants.ACTION_REGISTER);
		for (String receiver : sReceivers) {
			Intent intent = new Intent(GlobalConstants.ACTION_REGISTER);
			intent.setComponent(new ComponentName(packageName, packageName + receiver));
			context.sendBroadcast(intent);
		}
	}

	private void onRemoved(Context context, String packageName) {
		LOG.d("onRemoved: packageName=" + packageName);
		ModuleRegistry.getInstance(context).unregisterModule(packageName);
	}

}
