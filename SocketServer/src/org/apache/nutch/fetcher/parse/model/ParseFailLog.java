package org.apache.nutch.fetcher.parse.model;

/**
 * 页面解析失败日志
 * @author ysc
 *
 */
public class ParseFailLog   implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private ParseResult parseResult;
	//解析页面的URL
	private String url;
	//解析页面的URL模式
	private String urlPattern;
	//页面模板
	private String templateName;
	//解析页面的CSS路径
	private String cssPath;
	//CSS路径下的解析函数
	private String parseExpression;
	// 解析出的内容保存到的表的名称
	private String tableName;
	//解析出的内容保存到的字段名称
	private String fieldName;
	//解析出的内容保存到的字段描述，仅作注释使用
	private String fieldDescription;
	
//	public ParseResult getParseResult() {
//		return parseResult;
//	}
//	public void setParseResult(ParseResult parseResult) {
//		this.parseResult = parseResult;
//	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getCssPath() {
		return cssPath;
	}
	public void setCssPath(String cssPath) {
		this.cssPath = cssPath;
	}
	public String getParseExpression() {
		return parseExpression;
	}
	public void setParseExpression(String parseExpression) {
		this.parseExpression = parseExpression;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldDescription() {
		return fieldDescription;
	}
	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}
	@Override
	public String toString() {
		return "ParseFailLog [\nurl=" + url + ", \nurlPattern=" + urlPattern
				+ ", \ntemplateName=" + templateName + ", \ncssPath=" + cssPath
				+ ", \nparseExpression=" + parseExpression + ", \ntableName="
				+ tableName + ", \nfieldName=" + fieldName
				+ ", \nfieldDescription=" + fieldDescription + "]";
	}
}