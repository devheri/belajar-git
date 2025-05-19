package com.example.iso8583server;

import com.google.gson.annotations.SerializedName;

public class ResponseParse{

	@SerializedName("DE")
	private String dE;

	@SerializedName("MTI")
	private String mTI;

	@SerializedName("BIN")
	private String bIN;

	public void setDE(String dE){
		this.dE = dE;
	}

	public String getDE(){
		return dE;
	}

	public void setMTI(String mTI){
		this.mTI = mTI;
	}

	public String getMTI(){
		return mTI;
	}

	public void setBIN(String bIN){
		this.bIN = bIN;
	}

	public String getBIN(){
		return bIN;
	}
}