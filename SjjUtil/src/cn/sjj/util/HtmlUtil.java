//package cn.sjj.util;
//
///*
// * Copyright (C) 2007 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.HashMap;
//
//import org.ccil.cowan.tagsoup.HTMLSchema;
//import org.xml.sax.Attributes;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.InputSource;
//import org.xml.sax.Locator;
//import org.ccil.cowan.tagsoup.Parser;
//import org.xml.sax.SAXException;
//import org.xml.sax.XMLReader;
//
//import android.content.res.ColorStateList;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.style.BackgroundColorSpan;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.ImageSpan;
//import android.text.style.ParagraphStyle;
//import android.text.style.QuoteSpan;
//import android.text.style.RelativeSizeSpan;
//import android.text.style.StyleSpan;
//import android.text.style.SubscriptSpan;
//import android.text.style.SuperscriptSpan;
//import android.text.style.TextAppearanceSpan;
//import android.text.style.TypefaceSpan;
//import android.text.style.URLSpan;
//import android.text.style.UnderlineSpan;
//import android.widget.TextView;
//
//import com.android.internal.util.XmlUtils;
//
///**
// * This class processes HTML strings into displayable styled text. Not all HTML
// * tags are supported.
// */
//public class HtmlUtil {
//	/**
//	 * Retrieves images for HTML &lt;img&gt; tags.
//	 */
//	public static interface ImageGetter {
//		/**
//		 * This methos is called when the HTML parser encounters an &lt;img&gt;
//		 * tag. The <code>source</code> argument is the string from the "src"
//		 * attribute; the return value should be a Drawable representation of
//		 * the image or <code>null</code> for a generic replacement image. Make
//		 * sure you call setBounds() on your Drawable if it doesn't already have
//		 * its bounds set.
//		 */
//		public Drawable getDrawable(String source);
//	}
//
//	/**
//	 * Is notified when HTML tags are encountered that the parser does not know
//	 * how to interpret.
//	 */
//	public static interface TagHandler {
//		/**
//		 * This method will be called whenn the HTML parser encounters a tag
//		 * that it does not know how to interpret.
//		 */
//		public void handleTag(boolean opening, String tag, Editable output,
//				XMLReader xmlReader);
//	}
//
//	private HtmlUtil() {
//	}
//
//	/**
//	 * Returns displayable styled text from the provided HTML string. Any
//	 * &lt;img&gt; tags in the HTML will display as a generic replacement image
//	 * which your program can then go through and replace with real images.
//	 *
//	 * <p>
//	 * This uses TagSoup to handle real HTML, including all of the brokenness
//	 * found in the wild.
//	 */
//	public static Spanned fromHtml(String source) {
//		return fromHtml(source, null, null, null);
//	}
//
//	/**
//	 * Lazy initialization holder for HTML parser. This class will a) be
//	 * preloaded by the zygote, or b) not loaded until absolutely necessary.
//	 */
//	private static class HtmlParser {
//		private static final HTMLSchema schema = new HTMLSchema();
//	}
//
//	/**
//	 * Returns displayable styled text from the provided HTML string. Any
//	 * &lt;img&gt; tags in the HTML will use the specified ImageGetter to
//	 * request a representation of the image (use null if you don't want this)
//	 * and the specified TagHandler to handle unknown tags (specify null if you
//	 * don't want this).
//	 *
//	 * <p>
//	 * This uses TagSoup to handle real HTML, including all of the brokenness
//	 * found in the wild.
//	 */
//	public static Spanned fromHtml(String source, ImageGetter imageGetter,
//			TagHandler tagHandler, TextView tv) {
//		Parser parser = new org.ccil.cowan.tagsoup.Parser();
//		try {
//			parser.setProperty(Parser.schemaProperty, HtmlParser.schema);
//		} catch (org.xml.sax.SAXNotRecognizedException e) {
//			// Should not happen.
//			throw new RuntimeException(e);
//		} catch (org.xml.sax.SAXNotSupportedException e) {
//			// Should not happen.
//			throw new RuntimeException(e);
//		}
//
//		HtmlToSpannedConverter converter = new HtmlToSpannedConverter(source,
//				imageGetter, tagHandler, parser, tv);
//		return converter.convert();
//	}
//
//}
//
//class HtmlToSpannedConverter implements ContentHandler {
//
//	private static final float[] HEADER_SIZES = { 1.5f, 1.4f, 1.3f, 1.2f, 1.1f,
//			1f, };
//
//	private String mSource;
//	private XMLReader mReader;
//	private SpannableStringBuilder mSpannableStringBuilder;
//	private HtmlUtil.ImageGetter mImageGetter;
//	private HtmlUtil.TagHandler mTagHandler;
//	private TextView mTv;
//
//	public HtmlToSpannedConverter(String source,
//			HtmlUtil.ImageGetter imageGetter, HtmlUtil.TagHandler tagHandler,
//			Parser parser, TextView tv) {
//		mSource = source;
//		mSpannableStringBuilder = new SpannableStringBuilder();
//		mImageGetter = imageGetter;
//		mTagHandler = tagHandler;
//		mReader = parser;
//		if (tv != null) {
//			this.mTv = tv;
//		}
//	}
//
//	public Spanned convert() {
//		// System.out.println("length: " + mSpannableStringBuilder.length());
//		// mSpannableStringBuilder.setSpan(new ForegroundColorSpan(
//		// getHtmlColor("grey")), 0, mSpannableStringBuilder.length(),
//		// Spanned.SPAN_INCLUSIVE_INCLUSIVE);
////		mSpannableStringBuilder.setSpan(new BackgroundColorSpan(0xFF000000), 0, 0,
////				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//		mReader.setContentHandler(this);
//		try {
//			mReader.parse(new InputSource(new StringReader(mSource)));
//		} catch (IOException e) {
//			// We are reading from a string. There should not be IO problems.
//			throw new RuntimeException(e);
//		} catch (SAXException e) {
//			// TagSoup doesn't throw parse exceptions.
//			throw new RuntimeException(e);
//		}
//
//		// Fix flags and range for paragraph-type markup.
//		Object[] obj = mSpannableStringBuilder.getSpans(0,
//				mSpannableStringBuilder.length(), ParagraphStyle.class);
//		for (int i = 0; i < obj.length; i++) {
//			int start = mSpannableStringBuilder.getSpanStart(obj[i]);
//			int end = mSpannableStringBuilder.getSpanEnd(obj[i]);
//
//			// If the last line of the range is blank, back off by one.
//			if (end - 2 >= 0) {
//				if (mSpannableStringBuilder.charAt(end - 1) == '\n'
//						&& mSpannableStringBuilder.charAt(end - 2) == '\n') {
//					end--;
//				}
//			}
//
//			if (end == start) {
//				mSpannableStringBuilder.removeSpan(obj[i]);
//			} else {
//				mSpannableStringBuilder.setSpan(obj[i], start, end,
//						Spannable.SPAN_PARAGRAPH);
//			}
//		}
//
//		return mSpannableStringBuilder;
//	}
//
//	private void handleStartTag(String tag, Attributes attributes) {
//		if (tag.equalsIgnoreCase("body")) {
//			startBody(mSpannableStringBuilder, attributes);
//		} else if (tag.equalsIgnoreCase("br")) {
//			// We don't need to handle this. TagSoup will ensure that there's a
//			// </br> for each <br>
//			// so we can safely emite the linebreaks when we handle the close
//			// tag.
//		} else if (tag.equalsIgnoreCase("p")) {
//			handleP(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("div")) {
//			handleP(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("strong")) {
//			handleB(mSpannableStringBuilder, attributes);
//		} else if (tag.equalsIgnoreCase("b")) {
//			handleB(mSpannableStringBuilder, attributes);
//		} else if (tag.equalsIgnoreCase("em")) {
//			start(mSpannableStringBuilder, new Italic());
//		} else if (tag.equalsIgnoreCase("cite")) {
//			start(mSpannableStringBuilder, new Italic());
//		} else if (tag.equalsIgnoreCase("dfn")) {
//			start(mSpannableStringBuilder, new Italic());
//		} else if (tag.equalsIgnoreCase("i")) {
//			start(mSpannableStringBuilder, new Italic());
//		} else if (tag.equalsIgnoreCase("big")) {
//			start(mSpannableStringBuilder, new Big());
//		} else if (tag.equalsIgnoreCase("small")) {
//			start(mSpannableStringBuilder, new Small());
//		} else if (tag.equalsIgnoreCase("font")) {
//			startFont(mSpannableStringBuilder, attributes);
//		} else if (tag.equalsIgnoreCase("blockquote")) {
//			handleP(mSpannableStringBuilder);
//			start(mSpannableStringBuilder, new Blockquote());
//		} else if (tag.equalsIgnoreCase("tt")) {
//			start(mSpannableStringBuilder, new Monospace());
//		} else if (tag.equalsIgnoreCase("a")) {
//			startA(mSpannableStringBuilder, attributes);
//		} else if (tag.equalsIgnoreCase("u")) {
//			start(mSpannableStringBuilder, new Underline());
//		} else if (tag.equalsIgnoreCase("sup")) {
//			start(mSpannableStringBuilder, new Super());
//		} else if (tag.equalsIgnoreCase("sub")) {
//			start(mSpannableStringBuilder, new Sub());
//		} else if (tag.length() == 2
//				&& Character.toLowerCase(tag.charAt(0)) == 'h'
//				&& tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
//			handleP(mSpannableStringBuilder);
//			start(mSpannableStringBuilder, new Header(tag.charAt(1) - '1'));
//		} else if (tag.equalsIgnoreCase("img")) {
//			startImg(mSpannableStringBuilder, attributes, mImageGetter);
//		} else if (mTagHandler != null) {
//			mTagHandler.handleTag(true, tag, mSpannableStringBuilder, mReader);
//		}
//	}
//
//	private void handleB(SpannableStringBuilder mSpannableStringBuilder2,
//			Attributes attributes) {
//		String style = attributes.getValue("style");
//		start(mSpannableStringBuilder, new Bold(style));
//	}
//
//	private void handleEndTag(String tag) {
//		if (tag.equalsIgnoreCase("body")) {
//			endBody(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("br")) {
//			handleBr(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("p")) {
//			handleP(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("div")) {
//			handleP(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("strong")) {
//			endB();
//		} else if (tag.equalsIgnoreCase("b")) {
//			endB();
//		} else if (tag.equalsIgnoreCase("em")) {
//			end(mSpannableStringBuilder, Italic.class, new StyleSpan(
//					Typeface.ITALIC));
//		} else if (tag.equalsIgnoreCase("cite")) {
//			end(mSpannableStringBuilder, Italic.class, new StyleSpan(
//					Typeface.ITALIC));
//		} else if (tag.equalsIgnoreCase("dfn")) {
//			end(mSpannableStringBuilder, Italic.class, new StyleSpan(
//					Typeface.ITALIC));
//		} else if (tag.equalsIgnoreCase("i")) {
//			end(mSpannableStringBuilder, Italic.class, new StyleSpan(
//					Typeface.ITALIC));
//		} else if (tag.equalsIgnoreCase("big")) {
//			end(mSpannableStringBuilder, Big.class, new RelativeSizeSpan(1.25f));
//		} else if (tag.equalsIgnoreCase("small")) {
//			end(mSpannableStringBuilder, Small.class,
//					new RelativeSizeSpan(0.8f));
//		} else if (tag.equalsIgnoreCase("font")) {
//			endFont(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("blockquote")) {
//			handleP(mSpannableStringBuilder);
//			end(mSpannableStringBuilder, Blockquote.class, new QuoteSpan());
//		} else if (tag.equalsIgnoreCase("tt")) {
//			end(mSpannableStringBuilder, Monospace.class, new TypefaceSpan(
//					"monospace"));
//		} else if (tag.equalsIgnoreCase("a")) {
//			endA(mSpannableStringBuilder);
//		} else if (tag.equalsIgnoreCase("u")) {
//			end(mSpannableStringBuilder, Underline.class, new UnderlineSpan());
//		} else if (tag.equalsIgnoreCase("sup")) {
//			end(mSpannableStringBuilder, Super.class, new SuperscriptSpan());
//		} else if (tag.equalsIgnoreCase("sub")) {
//			end(mSpannableStringBuilder, Sub.class, new SubscriptSpan());
//		} else if (tag.length() == 2
//				&& Character.toLowerCase(tag.charAt(0)) == 'h'
//				&& tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
//			handleP(mSpannableStringBuilder);
//			endHeader(mSpannableStringBuilder);
//		} else if (mTagHandler != null) {
//			mTagHandler.handleTag(false, tag, mSpannableStringBuilder, mReader);
//		}
//	}
//
//	private void endB() {
//		SpannableStringBuilder text = mSpannableStringBuilder;
//		int len = text.length();
//		Object obj = getLast(text, Bold.class);
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		if (where != len) {
//			Bold bold = (Bold) obj;
//			if (!TextUtils.isEmpty(bold.style)) {
//				if (bold.style.contains("font-size: x-large;")) {
//					text.setSpan(new RelativeSizeSpan(1.5f), where, len,
//							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				}
//			}
//			text.setSpan(new StyleSpan(Typeface.BOLD), where, len,
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//	}
//
//	private static void startBody(SpannableStringBuilder text,
//			Attributes attributes) {
//		String style = attributes.getValue("style");
//		if (style == null) {
//			return;
//		}
//		String[] split = style.split(";");
//		String background = "";
//		String color = "";
//		for (String string : split) {
//			if (string.contains("background-color")) {
//				background = string.substring(string.indexOf(":") + 1);
//			} else if (string.contains("color")) {
//				color = string.substring(string.indexOf(":") + 1);
//			}
//		}
//		Body body = new Body(background, color);
//		int len = text.length();
//		text.setSpan(body, len, len, Spannable.SPAN_MARK_MARK);
//
//		if (!TextUtils.isEmpty(body.textColor)) {
//			if (body.textColor.startsWith("@")) {
//				Resources res = Resources.getSystem();
//				String name = body.textColor.substring(1);
//				int colorRes = res.getIdentifier(name, "color", "android");
//				if (colorRes != 0) {
//					ColorStateList colors = res.getColorStateList(colorRes);
//					text.setSpan(new TextAppearanceSpan(null, 0, 0, colors,
//							null), len, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				}
//				// } else if (body.textColor.startsWith("#")) {
//				// mTv.setTextColor(Color.parseColor(body.textColor));
//			} else {
//				int c = getHtmlColor(body.textColor);
//				// System.out.println("body.textColor: " + body.textColor
//				// + ", c: " + c);
//				if (c != -1) {
//					text.setSpan(new ForegroundColorSpan(c | 0xFF000000), len,
//							len, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//				}
//			}
//		}
//	}
//
//	private void endBody(SpannableStringBuilder text) {
//		int len = text.length();
//		Object obj = getLast(text, Body.class);
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		if (where != len && obj instanceof Body) {
//			Body body = (Body) obj;
//
//			if (!TextUtils.isEmpty(body.textColor)) {
//				if (body.textColor.startsWith("@")) {
//					Resources res = Resources.getSystem();
//					String name = body.textColor.substring(1);
//					int colorRes = res.getIdentifier(name, "color", "android");
//					if (colorRes != 0) {
//						ColorStateList colors = res.getColorStateList(colorRes);
//						text.setSpan(new TextAppearanceSpan(null, 0, 0, colors,
//								null), where, len,
//								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					}
//					// } else if (body.textColor.startsWith("#")) {
//					// mTv.setTextColor(Color.parseColor(body.textColor));
//				} else {
//					int c = getHtmlColor(body.textColor);
//					// System.out.println("body.textColor: " + body.textColor
//					// + ", c: " + c);
//					if (c != -1) {
//						// text.setSpan(new ForegroundColorSpan(c | 0xFF000000),
//						// where, len, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//					}
//				}
//			}
//			String background = body.background;
//			if (background.startsWith("@")) {
//				Resources res = Resources.getSystem();
//				String name = background.substring(1);
//				int colorRes = res.getIdentifier(name, "color", "android");
//				if (colorRes != 0) {
//					ColorStateList colors = res.getColorStateList(colorRes);
//					text.setSpan(new TextAppearanceSpan(null, 0, 0, colors,
//							null), where, len,
//							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				}
//			} else {
//				int c = getHtmlColor(background);
//				// System.out.println("background: " + background + ", c: " +
//				// c);
//				if (c != -1) {
//					// text.setSpan(new BackgroundColorSpan(c | 0xFF000000),
//					// where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					if (mTv != null) {
//						 mTv.setBackgroundColor(c);
//					}
//				}
//			}
//		}
//	}
//
//	private static void handleP(SpannableStringBuilder text) {
//		int len = text.length();
//
//		if (len >= 1 && text.charAt(len - 1) == '\n') {
//			if (len >= 2 && text.charAt(len - 2) == '\n') {
//				return;
//			}
//
//			text.append("\n");
//			return;
//		}
//
//		if (len != 0) {
//			text.append("\n\n");
//		}
//	}
//
//	private static void handleBr(SpannableStringBuilder text) {
//		text.append("\n");
//	}
//
//	private static Object getLast(Spanned text, Class kind) {
//		/*
//		 * This knows that the last returned object from getSpans() will be the
//		 * most recently added.
//		 */
//		Object[] objs = text.getSpans(0, text.length(), kind);
//
//		if (objs.length == 0) {
//			return null;
//		} else {
//			return objs[objs.length - 1];
//		}
//	}
//
//	private static void start(SpannableStringBuilder text, Object mark) {
//		int len = text.length();
//		text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
//	}
//
//	private static void end(SpannableStringBuilder text, Class kind, Object repl) {
//		int len = text.length();
//		Object obj = getLast(text, kind);
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		if (where != len) {
//			text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//
//		return;
//	}
//
//	private static void startImg(SpannableStringBuilder text,
//			Attributes attributes, HtmlUtil.ImageGetter img) {
//		String src = attributes.getValue("", "src");
//		Drawable d = null;
//
//		if (img != null) {
//			d = img.getDrawable(src);
//		}
//
//		if (d == null) {
//			d = Resources.getSystem().getDrawable(
//					com.android.internal.R.drawable.unknown_image);
//			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//		}
//
//		int len = text.length();
//		text.append("\uFFFC");
//
//		text.setSpan(new ImageSpan(d, src), len, text.length(),
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//	}
//
//	private static void startFont(SpannableStringBuilder text,
//			Attributes attributes) {
//		String color = attributes.getValue("", "color");
//		String face = attributes.getValue("", "face");
//
//		int len = text.length();
//		text.setSpan(new Font(color, face), len, len, Spannable.SPAN_MARK_MARK);
//	}
//
//	private static void endFont(SpannableStringBuilder text) {
//		int len = text.length();
//		Object obj = getLast(text, Font.class);
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		if (where != len) {
//			Font f = (Font) obj;
//			if (!TextUtils.isEmpty(f.mColor)) {
//				if (f.mColor.startsWith("@")) {
//					Resources res = Resources.getSystem();
//					String name = f.mColor.substring(1);
//					int colorRes = res.getIdentifier(name, "color", "android");
//					if (colorRes != 0) {
//						ColorStateList colors = res.getColorStateList(colorRes);
//						text.setSpan(new TextAppearanceSpan(null, 0, 0, colors,
//								null), where, len,
//								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					}
//				} else {
//					int c = getHtmlColor(f.mColor);
//					// System.out.println("f.mColor: " + c);
//					// System.out.println("where: " + where + ", len: " + len);
//					if (c != -1) {
//						text.setSpan(new ForegroundColorSpan(c | 0xFF000000),
//								where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					}
//				}
//			}
//
//			if (f.mFace != null) {
//				text.setSpan(new TypefaceSpan(f.mFace), where, len,
//						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			}
//		}
//	}
//
//	private static void startA(SpannableStringBuilder text,
//			Attributes attributes) {
//		String href = attributes.getValue("", "href");
//
//		int len = text.length();
//		text.setSpan(new Href(href), len, len, Spannable.SPAN_MARK_MARK);
//	}
//
//	private static void endA(SpannableStringBuilder text) {
//		int len = text.length();
//		Object obj = getLast(text, Href.class);
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		if (where != len) {
//			Href h = (Href) obj;
//
//			if (h.mHref != null) {
//				text.setSpan(new URLSpan(h.mHref), where, len,
//						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			}
//		}
//	}
//
//	private static void endHeader(SpannableStringBuilder text) {
//		int len = text.length();
//		Object obj = getLast(text, Header.class);
//
//		int where = text.getSpanStart(obj);
//
//		text.removeSpan(obj);
//
//		// Back off not to change only the text, not the blank line.
//		while (len > where && text.charAt(len - 1) == '\n') {
//			len--;
//		}
//
//		if (where != len) {
//			Header h = (Header) obj;
//
//			text.setSpan(new RelativeSizeSpan(HEADER_SIZES[h.mLevel]), where,
//					len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			text.setSpan(new StyleSpan(Typeface.BOLD), where, len,
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//	}
//
//	public void setDocumentLocator(Locator locator) {
//	}
//
//	public void startDocument() throws SAXException {
//	}
//
//	public void endDocument() throws SAXException {
//	}
//
//	public void startPrefixMapping(String prefix, String uri)
//			throws SAXException {
//	}
//
//	public void endPrefixMapping(String prefix) throws SAXException {
//	}
//
//	public void startElement(String uri, String localName, String qName,
//			Attributes attributes) throws SAXException {
//		handleStartTag(localName, attributes);
//	}
//
//	public void endElement(String uri, String localName, String qName)
//			throws SAXException {
//		handleEndTag(localName);
//	}
//
//	public void characters(char ch[], int start, int length)
//			throws SAXException {
//		StringBuilder sb = new StringBuilder();
//
//		/*
//		 * Ignore whitespace that immediately follows other whitespace; newlines
//		 * count as spaces.
//		 */
//
//		for (int i = 0; i < length; i++) {
//			char c = ch[i + start];
//
//			if (c == ' ' || c == '\n') {
//				char pred;
//				int len = sb.length();
//
//				if (len == 0) {
//					len = mSpannableStringBuilder.length();
//
//					if (len == 0) {
//						pred = '\n';
//					} else {
//						pred = mSpannableStringBuilder.charAt(len - 1);
//					}
//				} else {
//					pred = sb.charAt(len - 1);
//				}
//
//				if (pred != ' ' && pred != '\n') {
//					sb.append(' ');
//				}
//			} else {
//				sb.append(c);
//			}
//		}
//
//		mSpannableStringBuilder.append(sb);
//	}
//
//	public void ignorableWhitespace(char ch[], int start, int length)
//			throws SAXException {
//	}
//
//	public void processingInstruction(String target, String data)
//			throws SAXException {
//	}
//
//	public void skippedEntity(String name) throws SAXException {
//	}
//
//	private static class Bold {
//		private String style;
//
//		public Bold(String style) {
//			super();
//			this.style = style;
//		}
//	}
//
//	private static class Italic {
//	}
//
//	private static class Underline {
//	}
//
//	private static class Big {
//	}
//
//	private static class Small {
//	}
//
//	private static class Monospace {
//	}
//
//	private static class Blockquote {
//	}
//
//	private static class Super {
//	}
//
//	private static class Sub {
//	}
//
//	private static class Font {
//		public String mColor;
//		public String mFace;
//
//		public Font(String color, String face) {
//			mColor = color;
//			mFace = face;
//		}
//	}
//
//	private static class Href {
//		public String mHref;
//
//		public Href(String href) {
//			mHref = href;
//		}
//	}
//
//	private static class Header {
//		private int mLevel;
//
//		public Header(int level) {
//			mLevel = level;
//		}
//	}
//
//	private static class Body {
//		private String background;
//		private String textColor;
//
//		public Body(String background, String textColor) {
//			super();
//			this.background = background;
//			this.textColor = textColor;
//		}
//	}
//
//	private static HashMap<String, Integer> COLORS = buildColorMap();
//
//	private static HashMap<String, Integer> buildColorMap() {
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		map.put("aqua", 0x00FFFF);
//		map.put("black", 0xFF000000);
//		map.put("blue", 0x0000FF);
//		map.put("fuchsia", 0xFF00FF);
//		map.put("green", 0x008000);
//		map.put("grey", 0x808080);
//		map.put("gray", 0x808080);
//		map.put("lime", 0x00FF00);
//		map.put("maroon", 0x800000);
//		map.put("navy", 0x000080);
//		map.put("olive", 0x808000);
//		map.put("purple", 0x800080);
//		map.put("red", 0xFF0000);
//		map.put("silver", 0xC0C0C0);
//		map.put("teal", 0x008080);
//		map.put("white", 0xFFFFFF);
//		map.put("yellow", 0xFFFF00);
//		return map;
//	}
//
//	/**
//	 * Converts an HTML color (named or numeric) to an integer RGB value.
//	 *
//	 * @param color
//	 *            Non-null color string.
//	 * @return A color value, or {@code -1} if the color string could not be
//	 *         interpreted.
//	 */
//	private static int getHtmlColor(String color) {
//		Integer i = COLORS.get(color.toLowerCase());
//		if (i != null) {
//			return i;
//		} else {
//			try {
//				return XmlUtils.convertValueToInt(color, -1);
//			} catch (NumberFormatException nfe) {
//				return -1;
//			}
//		}
//	}
//
//}
