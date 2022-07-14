package com.jxrisesun.rstool.core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

/**
 * html 工具类
 * @author zhangzl
 *
 */
public class HtmlUtils {

	/**
	 * 解析为  html 文档
	 * @param html
	 * @param baseUri
	 * @return
	 */
	public static Document parseHtmlDocument(String html, String baseUri) {
		if(StringUtils.isEmpty(baseUri)) {
			return Jsoup.parse(html);
		} else {
			return Jsoup.parse(html, baseUri);
		}
	}
	
	/**
	 * 解析为 html body 文档
	 * @param bodyHtml
	 * @param baseUri
	 * @return
	 */
	public static Document parseHtmlBody(String bodyHtml, String baseUri) {
		if(StringUtils.isEmpty(baseUri)) {
			return Jsoup.parseBodyFragment(bodyHtml);
		} else {
			return Jsoup.parseBodyFragment(bodyHtml, baseUri);
		}
	}
	
	/**
	 * 清洗 html body
	 * @param bodyHtml
	 * @param whitelist 白名单列表
	 * @param baseUri
	 * @return
	 */
	public static String cleanHtmlBody(String bodyHtml, Safelist whitelist, String baseUri) {
		if(whitelist == null) {
			whitelist = Safelist.none();
		}
		if(StringUtils.isEmpty(baseUri)) {
			return Jsoup.clean(bodyHtml, whitelist);
		} else {
			return Jsoup.clean(bodyHtml, baseUri, whitelist);
		}
	}
	
	/**
	 * 根据id获取元素
	 * @param element
	 * @param id
	 * @return
	 */
	public static Element getElementById(Element element, String id) {
		return element != null ? element.getElementById(id) : null;
	}
	
	/**
	 * 根据 tag 获取元素集合
	 * @param element
	 * @param tagName
	 * @return
	 */
	public static Elements getElementsByTag(Element element, String tagName) {
		return element != null ? element.getElementsByTag(tagName) : null;
	}
	
	/**
	 * 根据 class 获取元素集合 
	 * @param element
	 * @param className class名称
	 * @return
	 */
	public static Elements getElementsByClass(Element element, String className) {
		return element != null ? element.getElementsByClass(className) : null;
	}
	
	/**
	 * 根据属性名称获取元素集合
	 * @param element
	 * @param key
	 * @return
	 */
	public static Elements getElementsByAttribute(Element element, String key) {
		return element != null ? element.getElementsByAttribute(key) : null;
	}
	
	/**
	 * 根据属性名称和值获取元素集合
	 * @param element
	 * @param key
	 * @param value
	 * @return
	 */
	public static Elements getElementsByAttributeValue(Element element, String key, String value) {
		return element != null ? element.getElementsByAttributeValue(key, value) : null;
	}
	
	/**
	 * 根据 css 选择器获取元素
	 * @param element
	 * @param cssQuery css选择器
	 * @return
	 */
	public static Element getElementByCssQuery(Element element, String cssQuery) {
		Elements elements = getElementsByCssQuery(element, cssQuery);
		return (elements != null && !elements.isEmpty()) ? elements.get(0) : null;
	}
	
	/**
	 * 根据 css 选择器获取元素集合
	 * @param element
	 * @param cssQuery css选择器
	 * @return
	 */
	public static Elements getElementsByCssQuery(Element element, String cssQuery) {
		return element != null ? element.select(cssQuery) : null;
	}
}
