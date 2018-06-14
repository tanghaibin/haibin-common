package com.haibin.common.enums;

/**
 * 时间枚举类，单位统一为：毫秒
 */
public enum DateTypeEnum {
	DAY("天") {
		@Override
		public Integer toValue() {
			return 24 * HOUR.toValue();
		}
	},
	HOUR("小时") {
		@Override
		public Integer toValue() {
			return 60 * MINUTE.toValue();
		}
	},
	MINUTE("分") {
		@Override
		public Integer toValue() {
			return 60 * SECOND.toValue();
		}
	},
	SECOND("秒") {
		@Override
		public Integer toValue() {
			return 1000;
		}
	};

	/**
	 * 描述，例如天、小时、分、秒
	 */
	private final String title;

	public abstract Integer toValue();

	private DateTypeEnum(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
