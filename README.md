# ![Logo](app/src/main/res/drawable-hdpi/ic_launcher.png) MarkdownView [![.github/workflows/build.yml](https://github.com/billthefarmer/MarkdownView/workflows/.github/workflows/build.yml/badge.svg)](https://github.com/billthefarmer/MarkdownView/actions) [![](https://jitpack.io/v/billthefarmer/MarkdownView.svg)](https://jitpack.io/#billthefarmer/MarkdownView)

Android markdown view forked from https://github.com/falnatsheh/MarkdownView

![Markdown](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Markdown.png)

## About

MarkdownView is an Android library that helps you display Markdown
text or files (local/remote) as formatted HTML, and style the output
using CSS.

The MarkdownView itself extends Android WebView and adds the necessary
logic to parse Markdown (using Commonmark) and display the output HTML
on the view.

## Getting started

To add MarkdownView to your project, add the following to the
`build.gradle` file:
```gradle
allprojects {
    repositories {
        jcenter()
        google()
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.billthefarmer:MarkdownView:v1.09'
}
```

## Usage
Add MarkdownView to your layout:
```xml
  <org.billthefarmer.markdown.MarkdownView
    android:id="@+id/markdown"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

and reference it in your Activity/Fragment:
```java
MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdown);
markdownView.loadMarkdown("## Hello Markdown");
```

You could also create the view by code. Below an example of how to set
the whole activity to be a MarkdownView by adding the following to
your onCreate method:
```java
  MarkdownView markdownView = new MarkdownView(this);
  setContentView(markdownView);
  markdownView.loadMarkdown("## Hello Markdown");
```

To load markdown from the app assets folder, including styles,
javascript and base url:
```java
  markdownView.loadMarkdownFile("file:///android_assets/",
                                "file:///android_assets/markdown.md",
                                "file:///android_assets/styles.css",
                                "file:///android_assets/javascript.js");
```
## API
```java
  MarkdownView(Context context)
  MarkdownView(Context context, AttributeSet attrs)
```
**Parameters**
* context - Context
* attrs - AttributeSet
```java
  void loadMarkdown(String text)
  void loadMarkdown(String text, String cssFileUrl)
  void loadMarkdown(String baseUrl, String text, String cssFileUrl)
  void loadMarkdown(String baseUrl, String text, String cssFileUrl, String jsFileUrl)
```
**Parameters**
* text - markdown text
* cssFileUrl - url of styles css file
* baseUrl - base url to allow loading of images
```java
  void loadMarkdownFile(String url)
  void loadMarkdownFile(String url, String cssFileUrl)
  void loadMarkdownFile(String baseUrl, String url, String cssFileUrl)
  void loadMarkdownFile(String baseUrl, String url, String cssFileUrl, String jsFileUrl)
```
**Parameters**
* url - url of markdown text
* cssFileUrl - url of styles css file
* jsFileUrl - url of javascript file
* baseUrl - base url to allow loading of images
