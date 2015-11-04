package com.dz4.ishop.view;

import com.dz4.ishopping.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout {
	/*
	    <attr  name="titleText" format="string"/>
            <attr  name="titleSize" format="dimension"/>
            <attr  name="titleColor" format="color"/>
	    <attr  name="titlebarbackground" format="reference"/>
	    <attr  name="rightButtonImage" format="reference"/>
	    <attr  name="leftButtonImage" format="reference"/>
	    <attr  name="rightButtonVisible" format="boolean"/>
	    <attr  name="leftButtonVisible" format="boolean"/>
		    */
	private CharSequence titleText;
	private float titleSize;
	private int titleColor;
	private Drawable rightButtonImage;
	private Drawable leftButtonImage;
	private int rightButtonVisible;
	private int leftButtonVisible;
	
	private TextView title;
	private ImageButton rightButton;
	private ImageButton leftButton;
	
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	private LayoutParams titleParams;
	private onTopBarbtnclickListener mlistener;
	public TopBar(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		getResource(attrs);
		createView(context);
	}
	
	protected void getResource(AttributeSet attrs){
		
		TypedArray ta =this.getContext().obtainStyledAttributes(attrs,R.styleable.topbar);
		
		titleText = ta.getString(R.styleable.topbar_titleText); 
		titleColor = ta.getColor(R.styleable.topbar_titleColor, 0x00000000);
		//titleSize = ta.getLayoutDimension(R.styleable.topbar_titleSize, 10);
		titleSize = ta.getDimension(R.styleable.topbar_titleSize, 10);
		rightButtonImage = ta.getDrawable(R.styleable.topbar_rightButtonImage);
		leftButtonImage = ta.getDrawable(R.styleable.topbar_leftButtonImage);
		rightButtonVisible = ta.getInt(R.styleable.topbar_rightButtonVisible, View.GONE);
		leftButtonVisible = ta.getInt(R.styleable.topbar_leftButtonVisible, View.GONE);
		
		ta.recycle();
	}
	@SuppressLint("NewApi")
	protected void createView(Context context){
		
		title=new TextView(context);
		rightButton=new ImageButton(context);
		leftButton = new ImageButton(context);
		
		title.setTextColor(titleColor);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP,titleSize);
		title.setText(titleText);
		title.setGravity(Gravity.CENTER);
		
		rightButton.setBackground(rightButtonImage);
		//rightButton.setImageDrawable(rightButtonImage);
		rightButton.setVisibility(rightButtonVisible);
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(mlistener!=null)
				mlistener.rightbtnclick(v);
			}
		});
		leftButton.setBackground(leftButtonImage);
		//leftButton.setImageDrawable(leftButtonImage);
		leftButton.setVisibility(leftButtonVisible);
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(mlistener!=null)
				mlistener.leftbtnclick(v);
			}
		});
		
		leftParams=new LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()),
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
		addView(leftButton,leftParams);
		
		rightParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
		rightParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
		addView(rightButton,rightParams);
		
		titleParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
		addView(title,titleParams);
		setPadding(15, 10, 15, 10);
	}
	
	public CharSequence getTitleText() {
		return this.titleText;
	}

	public void setTitleText(CharSequence titleText) {
		this.titleText = titleText;
		title.setText(titleText);
	}

	public float getTitleSize() {
		return this.titleSize;
	}

	public void setTitleSize(float titleSize) {
		this.titleSize = titleSize;
		title.setTextSize(titleSize);
	}

	public int getTitleColor() {
		return this.titleColor;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
		title.setTextColor(titleColor);
	}

	public Drawable getRightButtonImage() {
		return this.rightButtonImage;
	}

	public void setRightButtonImage(Drawable rightButtonImage) {
		this.rightButtonImage = rightButtonImage;
		rightButton.setImageDrawable(rightButtonImage);
	}

	public Drawable getLeftButtonImage() {
		return this.leftButtonImage;
	}

	public void setLeftButtonImage(Drawable leftButtonImage) {
		this.leftButtonImage = leftButtonImage;
		leftButton.setImageDrawable(leftButtonImage);
	}

	public int getRightButtonVisible() {
		return this.rightButtonVisible;
	}

	public void setRightButtonVisible(int rightButtonVisible) {
		this.rightButtonVisible = rightButtonVisible;
		rightButton.setVisibility(rightButtonVisible);
	}

	public int getLeftButtonVisible() {
		return this.leftButtonVisible;
	}

	public void setLeftButtonVisible(int leftButtonVisible) {
		this.leftButtonVisible = leftButtonVisible;
		leftButton.setVisibility(leftButtonVisible);
	}

	public TextView getTitle() {
		return this.title;
	}

	public ImageButton getRightButton() {
		return this.rightButton;
	}

	public ImageButton getLeftButton() {
		return this.leftButton;
	}

	public void setLeftButton(ImageButton leftButton) {
		this.leftButton = leftButton;
	}

	public void setTopBarbtnclickListener(onTopBarbtnclickListener mlistener){
		this.mlistener = mlistener;
	}
	
	public interface onTopBarbtnclickListener{
		public void rightbtnclick(View v);
		public void leftbtnclick(View v);
	}
}
