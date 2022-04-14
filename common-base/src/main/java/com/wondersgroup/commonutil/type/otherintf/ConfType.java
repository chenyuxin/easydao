package com.wondersgroup.commonutil.type.otherintf;

/**
 * 配置类型接口<br>
 * 配置类型的枚举实现此接口 
 */
public interface ConfType {
	
	/**
	 * 获取配置
	 */
	int getMask();
	
	/**
	 * 根据枚举顺序获取配置类型
	 * @param order
	 * @return
	 */
	ConfType getConfType(int order);
	
	/**
	 * 配置是否开启
	 * @param confs
	 * @return
	 */
	boolean isEnabled(int confs);
	
	/**
	 * 开关某个配置返回配置结果
	 * @param confs 原配置
	 * @param state 开启或关闭
	 * @return
	 */
	int config(int confs, boolean state);
	
	/**
	 * 配置类型数组计算配置结果并返回
	 * @param confTypes
	 * @return
	 */
	public static int of(ConfType... confTypes) {
		if (confTypes == null) {
			return 0;
		}
		int value = 0;
		for (ConfType confType: confTypes) {
			value |= confType.getMask();
		}
		return value;
	}

}
