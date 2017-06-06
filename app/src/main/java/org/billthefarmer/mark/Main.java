/*
 * Copyright (c) 2017 Bill Farmer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.billthefarmer.mark;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.billthefarmer.markdown.MarkdownView;

public class Main extends Activity
{
    public final static String TAG = "Main";

    public final static String BASE = "file:///android_asset/";
    public final static String TEST = "file:///android_asset/test.md";
    public final static String STYLES = "file:///android_asset/styles.css";

    private MarkdownView markdownView;

    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize MarkdownView from layout
        markdownView = (MarkdownView) findViewById(R.id.markdown);

        if (markdownView != null)
        {
            markdownView.loadMarkdownFile(BASE, TEST, STYLES);

            WebSettings settings = markdownView.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        }
    }
}
