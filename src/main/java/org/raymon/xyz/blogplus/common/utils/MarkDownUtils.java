package org.raymon.xyz.blogplus.common.utils;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

/**
 * Created by lilm on 18-3-21.
 */
public class MarkDownUtils {
	
	public static String parseMarkDown2Html(String markdown) {
		MutableDataSet options = new MutableDataSet();
		
		// uncomment to set optional extensions
		//options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
		
		// uncomment to convert soft-breaks to hard breaks
		//options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
		
		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		
		// You can re-use parser and renderer instances
		Node document = parser.parse(markdown);
		String html = renderer.render(document);
		return html;
	}
	
}
