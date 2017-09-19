package com.jhj.Function;

/**
 * 推荐产品实体类
 * @author Administrator
 *
 */
public class Recommend {

	private String productName;//产品名称
	private String productId;//产品id
	
	public Recommend(){
		
	}
	public Recommend(String productName, String productId) {
		// TODO Auto-generated constructor stub
		this.productName = productName;
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
