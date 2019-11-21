/*
 * Copyright 2011 Feras Alnatsheh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.billthefarmer.markdown;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebView;

import org.markdownj.MarkdownProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * @author Feras Alnatsheh
 */
// MarkdownView
public class MarkdownView extends WebView
{
    private static final String TAG = "MarkdownView";

    private static final String ASSET = "file:///android_asset/";

    public final static String HTML_HEAD =
        "<!DOCTYPE html>\n<html lang='%s'>\n<head>\n" +
        "<meta charset='utf-8'>\n" +
        "<meta name='viewport' content='width=device-width, " +
        "initial-scale=1.0'>\n";
    public final static String HTML_BODY = "\n</head>\n<body>\n";
    public final static String HTML_TAIL = "\n</body>\n</html>\n";

    private static final String CSS =
        "<link rel='stylesheet' type='text/css' href='%s' />\n";
    private static final String JS =
        "<script type='text/javascript' src='%s'></script>\n";

    // MarkdownView
    public MarkdownView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    // MarkdownView
    public MarkdownView(Context context)
    {
        super(context);
    }

    /**
     * Loads the given Markdown text to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file. The given JS file will be included.
     *
     * @param baseUrl    - the URL to use as the page's base URL
     * @param text       - input in markdown format
     * @param cssFileUrl - a URL to css file. If the file is located in the
     *                   project assets folder then the URL should start
     *                   with "file:///android_asset/"
     * @param jsFileUrl  - a URL to js file. If the file is located in the
     *                   project assets folder then the URL should start
     *                   with "file:///android_asset/"
     */
    public void loadMarkdown(String baseUrl, String text,
                             String cssFileUrl, String jsFileUrl)
    {
        loadMarkdownToView(baseUrl, text, cssFileUrl, jsFileUrl);
    }

    /**
     * Loads the given Markdown text to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file.
     *
     * @param baseUrl    - the URL to use as the page's base URL
     * @param text       - input in markdown format
     * @param cssFileUrl - a URL to css File. If the file located in
     *                   the project assets folder then the URL should
     *                   start with "file:///android_asset/"
     */
    public void loadMarkdown(String baseUrl, String text, String cssFileUrl)
    {
        loadMarkdown(baseUrl, text, cssFileUrl, null);
    }

    /**
     * Loads the given Markdown text to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file.
     *
     * @param text       - input in markdown format
     * @param cssFileUrl - a URL to css File. If the file located in
     *                   the project assets folder then the URL should
     *                   start with "file:///android_asset/"
     */
    public void loadMarkdown(String text, String cssFileUrl)
    {
        loadMarkdown(null, text, cssFileUrl);
    }

    /**
     * Loads the given Markdown text to the view as rich formatted HTML.
     *
     * @param text - input in Markdown format
     */
    public void loadMarkdown(String text)
    {
        loadMarkdown(text, null);
    }

    /**
     * Loads the given Markdown file to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file.
     *
     * @param baseUrl    - the URL to use as the page's base URL
     * @param url        - a URL to the markdown file. If the file is located
     *                   in the project assets folder then the URL should
     *                   start with "file:///android_asset/"
     * @param cssFileUrl - a URL to css file. If the file is located in the
     *                   project assets folder then the URL should start
     *                   with "file:///android_asset/"
     * @param jsFileUrl  - a URL to js file. If the file is located in the
     *                   project assets folder then the URL should start
     *                   with "file:///android_asset/"
     */
    public void loadMarkdownFile(String baseUrl, String url,
                                 String cssFileUrl, String jsFileUrl)
    {
        new LoadMarkdownUrlTask(this).execute(baseUrl, url,
                                              cssFileUrl, jsFileUrl);
    }

    /**
     * Loads the given Markdown file to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file.
     *
     * @param baseUrl    - the URL to use as the page's base URL
     * @param url        - a URL to the Markdown file. If the file located
     *                   in the project assets folder then the URL
     *                   should start with "file:///android_asset/"
     * @param cssFileUrl - a URL to css File. If the file located in
     *                   the project assets folder then the URL should
     *                   start with "file:///android_asset/"
     */
    public void loadMarkdownFile(String baseUrl, String url, String cssFileUrl)
    {
        loadMarkdownFile(baseUrl, url, cssFileUrl, null);
    }

    /**
     * Loads the given Markdown file to the view as rich formatted
     * HTML. The HTML output will be styled based on the given CSS
     * file.
     *
     * @param url        - a URL to the Markdown file. If the file located
     *                   in the project assets folder then the URL
     *                   should start with "file:///android_asset/"
     * @param cssFileUrl - a URL to css File. If the file located in
     *                   the project assets folder then the URL should
     *                   start with "file:///android_asset/"
     */
    public void loadMarkdownFile(String url, String cssFileUrl)
    {
        loadMarkdownFile(null, url, cssFileUrl);
    }

    /**
     * Loads the given Markdown file to the view as rich formatted
     * HTML.
     *
     * @param url - a URL to the Markdown file. If the file located in
     *            the project assets folder then the URL should start
     *            with "file:///android_asset/"
     */
    public void loadMarkdownFile(String url)
    {
        loadMarkdownFile(url, null);
    }

    // readFileFromAsset
    private String readFileFromAsset(String fileName)
    {
        try
        {
            try (InputStream input = getContext().getAssets().open(fileName))
            {
                BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(input));
                StringBuilder content = new StringBuilder(input.available());
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line);
                    content.append(System.getProperty("line.separator"));
                }
                return content.toString();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while reading file from assets", e);
            return null;
        }
    }

    // loadMarkdownToView
    private void loadMarkdownToView(String baseUrl, String text,
                                    String cssFileUrl, String jsFileUrl)
    {
        MarkdownProcessor mark = new MarkdownProcessor();
        Locale locale = Locale.getDefault();

        // Header
        String header = String.format(HTML_HEAD, locale.getLanguage());
        StringBuilder html = new StringBuilder(header);

        // Add styles
        if (cssFileUrl != null)
            html.append(String.format(CSS, cssFileUrl));

        // Add javascript
        if (jsFileUrl != null)
            html.append(String.format(JS, jsFileUrl));

        // Markdown
        html.append(HTML_BODY);
        html.append(mark.markdown(text));
        html.append(HTML_TAIL);
        loadDataWithBaseURL(baseUrl, html.toString(),
                            "text/html", "UTF-8", null);
    }

    // LoadMarkdownUrlTask
    private static class LoadMarkdownUrlTask
        extends AsyncTask<String, Integer, String>
    {
        private WeakReference<MarkdownView> viewWeakReference;
        private String baseUrl;
        private String cssFileUrl;
        private String jsFileUrl;

        // LoadMarkdownUrlTask
        public LoadMarkdownUrlTask(MarkdownView markdownView)
        {
            viewWeakReference = new WeakReference<>(markdownView);
        }

        // doInBackground
        @Override
        protected String doInBackground(String... params)
        {
            final MarkdownView markdownView = viewWeakReference.get();
            if (markdownView == null)
                return null;

            try
            {
                String markdown;
                baseUrl = params[0];
                String url = params[1];
                cssFileUrl = params[2];
                jsFileUrl = params[3];

                if (URLUtil.isNetworkUrl(url))
                {
                    markdown = HttpHelper.get(url).getResponseMessage();
                }
                else if (URLUtil.isAssetUrl(url))
                {
                    markdown = markdownView
                        .readFileFromAsset(url.substring(ASSET.length(),
                                                         url.length()));
                }
                else
                {
                    throw new IllegalArgumentException
                    ("The URL provided is not a network or asset URL");
                }

                return markdown;
            }
            catch (Exception e)
            {
                Log.e(TAG, "Error Loading Markdown File", e);
                return null;
            }
        }

        // onPostExecute
        @Override
        protected void onPostExecute(String result)
        {
            final MarkdownView markdownView = viewWeakReference.get();
            if (markdownView == null)
                return;

            if (result != null)
            {
                markdownView.loadMarkdownToView(baseUrl, result,
                                                cssFileUrl, jsFileUrl);
            }
            else
            {
                markdownView.loadUrl("about:blank");
            }
        }
    }
}
