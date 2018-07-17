package cn.rongcapital.mc2.event.sdk.core;

/**
 * sdk事件可以类型枚举
 * @author 英博
 *
 */
public enum EventObjectType {
	
	CART, // 购物车
	CART_ITEM, // 购物车项
	ORDER, // 订单
	ORDER_ITEM, // 订单项
	PRODUCT, // 商品
	PRODUCT_SKU, // SKU
	PRODUCT_CATEGORY, // 商品品类
	SHOPPING_CART, // 购物车
	PAY_ORDER, // 支付单
	COUPON, // 优惠券
	ORDER_DELIVERY, // 物流单
	REFUND,//退款
	USER; //用户
}
