package com.dz4.ishop.listener;
/**
 * 用于监听标题栏的信息变化
 * @author MZone
 *
 */
public interface TitlechangeListener {
	public void changeTitle(String title);
	public void changeTitle(int titleRes);
	public void changePage(int page);
}
