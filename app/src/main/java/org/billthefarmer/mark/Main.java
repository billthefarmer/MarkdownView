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
import android.widget.EditText;
import android.widget.ImageButton;

import org.billthefarmer.markdown.MarkdownView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main extends Activity {
    public final static String TAG = "Main";

    public final static String FILE = "test.md";
    public final static String BASE = "file:///android_asset/";
    public final static String TEST = "file:///android_asset/test.md";
    public final static String STYLES = "file:///android_asset/styles.css";

    private MarkdownView markdownView;
    private EditText textView;

    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize MarkdownView from layout
        markdownView = findViewById(R.id.markdown);
        textView = findViewById(R.id.text);
        ImageButton accept = findViewById(R.id.accept);

        if (markdownView != null) {
            markdownView.loadMarkdownFile(BASE, TEST, STYLES);

            WebSettings settings = markdownView.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        }

        if (textView != null) {
            String text = readAssetFile();
            textView.setText(text);
        }

        if (accept != null) {
            // onClick
            accept.setOnClickListener(view -> {
                String text = textView.getText().toString();
                markdownView.loadMarkdown(BASE, text, STYLES);
            });
        }
    }

    // readAssetFile
    private String readAssetFile() {
        try {
            // Open asset file
            try (InputStream input = getResources().getAssets().open(Main.FILE)) {
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(input));
                StringBuilder content =
                        new StringBuilder(input.available());
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                    content.append(System.getProperty("line.separator"));
                }

                return content.toString();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while reading file from assets", e);
            return null;
        }
    }
}
