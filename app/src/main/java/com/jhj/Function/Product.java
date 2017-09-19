package com.jhj.Function;

/**
 * 产品实体类
 * @author lb
 *
 */
public class Product {

	private String product_imgUrl;//图片
	private String product_name;//产品名称
	private String product_synopsis;//简介
	private Boolean product_state;//状态


	public Product(String product_imgUrl, String product_name,
			String product_synopsis, Boolean product_state) {
		// TODO Auto-generated constructor stub
		this.product_imgUrl=product_imgUrl;
		this.product_name=product_name;
		this.product_synopsis=product_synopsis;
		this.product_state=product_state;
	}
	public Product(){

	}
	public String getProduct_imgUrl() {
		return product_imgUrl;
	}
	public void setProduct_imgUrl(String product_imgUrl) {
		this.product_imgUrl = product_imgUrl;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_synopsis() {
		return product_synopsis;
	}
	public void setProduct_synopsis(String product_synopsis) {
		this.product_synopsis = product_synopsis;
	}
	public Boolean getProduct_state() {
		return product_state;
	}
	public void setProduct_state(Boolean product_state) {
		this.product_state = product_state;
	}



}
