/*
 * Copyright (C) 2007 The Android Open Source Project 
 * Copyright (C) 2013 Michael DiGiovanni Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
// Original code snipped from the Android Home SDK Sample app
package com.mikedg.android.glass.launchy;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppHelper {
    private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();

    private Activity mActivity;
    private IntentFilter mfilter;

    private ListView mListView;

    private Typeface mRobotoLight;
    private static ArrayList<ApplicationInfo> mApplications;

    private final ArrayList<String> mExcludedApps = new ArrayList<String>();

    public AppHelper(Activity activity) {
        mActivity = activity;

        mRobotoLight = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");

        setupExclusions();

        ((TextView) activity.findViewById(android.R.id.text1)).setTypeface(mRobotoLight);
    }

    private void setupExclusions() {
        mExcludedApps.add("com.google.glass.home");
        mExcludedApps.add(mActivity.getPackageName());
    }

    /**
     * Registers various intent receivers. The current implementation registers only a wallpaper
     * intent receiver to let other applications change the wallpaper.
     */
    public void registerIntentReceivers() {
        mfilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        mfilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mfilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        mfilter.addDataScheme("package");
        mActivity.registerReceiver(mApplicationsReceiver, mfilter);
    }

    /**
     * Creates a new appplications adapter for the grid view and registers it.
     */
    public void bindApplications() {
        if (mListView == null) {
            mListView = (ListView) mActivity.findViewById(android.R.id.list);
        }
        mListView.setAdapter(new ApplicationsAdapter(mActivity, mApplications));
        mListView.setSelection(0);

        mListView.setOnItemClickListener(new ApplicationLauncher());
    }

    /**
     * Loads the list of installed applications in mApplications.
     */
    public void loadApplications(boolean isLaunching) {
        if (isLaunching && mApplications != null) {
            return;
        }

        PackageManager manager = mActivity.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            if (mApplications == null) {
                mApplications = new ArrayList<ApplicationInfo>(count);
            }
            mApplications.clear();

            // Create a launcher for Glass Settings or we have no way to hit that
            createGlassSettingsAppInfo();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
                Log.d("Launchyi", info.activityInfo.applicationInfo.packageName);
                // Let's filter out this app
                if (!mExcludedApps.contains(info.activityInfo.applicationInfo.packageName)) {
                    application.title = info.loadLabel(manager);
                    application.setActivity(new ComponentName(
                            info.activityInfo.applicationInfo.packageName, info.activityInfo.name),
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    application.icon = info.activityInfo.loadIcon(manager);

                    mApplications.add(application);
                }
            }

            // FIXME: should make a way to clear defaults
            // PackageManagerclearPackagePreferredActivities in special case
            // This needs to always be last?
        }
    }

    private void createGlassSettingsAppInfo() {
        ApplicationInfo application = new ApplicationInfo();

        application.title = "Glass Settings";
        application.setActivity(new ComponentName("com.google.glass.home",
                "com.google.glass.home.settings.SettingsActivity"),
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // application.icon = info.activityInfo.loadIcon(manager);

        mApplications.add(application);
    }

    /**
     * Receives notifications when applications are added/removed.
     */
    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApplications(false);
            bindApplications();
        }
    }

    /**
     * GridView adapter to show the list of all installed applications.
     */
    private class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {
        private static final int TYPE_SPACE = 0;
        private static final int TYPE_ITEM = 1;
        //private Rect mOldBounds = new Rect();

        public ApplicationsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
            super(context, 0, apps);
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == TYPE_ITEM;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == TYPE_ITEM) {
                final ApplicationInfo info = mApplications.get(position);
                if (convertView == null) {
                    final LayoutInflater inflater = mActivity.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.item_app, parent, false);
                    ((TextView) convertView.findViewById(android.R.id.text1))
                            .setTypeface(mRobotoLight);
                }

                // Drawable icon = info.icon;

                if (!info.filtered) {
                    // final Resources resources = getContext().getResources();
                    // int width = 42;// (int)
                    // resources.getDimension(android.R.dimen.app_icon_size);
                    // int height = 42;// (int)
                    // resources.getDimension(android.R.dimen.app_icon_size);
                    //
                    // final int iconWidth = icon.getIntrinsicWidth();
                    // final int iconHeight = icon.getIntrinsicHeight();

                    // if (icon instanceof PaintDrawable) {
                    // PaintDrawable painter = (PaintDrawable) icon;
                    // painter.setIntrinsicWidth(width);
                    // painter.setIntrinsicHeight(height);
                    // }
                    //
                    // if (width > 0 && height > 0 && (width < iconWidth || height < iconHeight)) {
                    // final float ratio = (float) iconWidth / iconHeight;
                    //
                    // if (iconWidth > iconHeight) {
                    // height = (int) (width / ratio);
                    // } else if (iconHeight > iconWidth) {
                    // width = (int) (height * ratio);
                    // }
                    //
                    // final Bitmap.Config c =
                    // icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    // : Bitmap.Config.RGB_565;
                    // final Bitmap thumb = Bitmap.createBitmap(width, height, c);
                    // final Canvas canvas = new Canvas(thumb);
                    // canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));
                    // // Copy the old bounds to restore them later
                    // // If we were to do oldBounds = icon.getBounds(),
                    // // the call to setBounds() that follows would
                    // // change the same instance and we would lose the
                    // // old bounds
                    // mOldBounds.set(icon.getBounds());
                    // icon.setBounds(0, 0, width, height);
                    // icon.draw(canvas);
                    // icon.setBounds(mOldBounds);
                    // icon = info.icon = new BitmapDrawable(thumb);
                    // info.filtered = true;
                    // }
                }

                final TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
                // textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                textView.setText(info.title);
            } else {
                if (convertView == null) {
                    final LayoutInflater inflater = mActivity.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.item_empty, parent, false);
                }
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getCount() - 1) {
                return TYPE_SPACE;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2; // 1 is the standard, the second is just a holder
        }
    }

    /**
     * Starts the selected activity/application in the grid view.
     */
    private class ApplicationLauncher implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
            mActivity.startActivity(app.intent);
        }
    }

    public void onDestroy() {
        // Remove the callback for the cached drawables or we leak
        // the previous Home screen on orientation change
        // final int count = mApplications.size();
        // for (int i = 0; i < count; i++) {
        // mApplications.get(i).icon.setCallback(null);
        // }

        mActivity.unregisterReceiver(mApplicationsReceiver);
    }
}
