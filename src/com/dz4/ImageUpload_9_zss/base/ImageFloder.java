package com.dz4.ImageUpload_9_zss.base;
public class ImageFloder
{  
	/**  
	 * 鍥剧墖鐨勬枃浠跺す璺緞
	 */  
	private String dir;

	/**
	 * 绗竴寮犲浘鐗囩殑璺緞 
	 */
	private String firstImagePath;

	/**
	 * 鏂囦欢澶圭殑鍚嶇О
	 */
	private String name;

	/**
	 * 鍥剧墖鐨勬暟閲�
	 */
	private int count;

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	

}
