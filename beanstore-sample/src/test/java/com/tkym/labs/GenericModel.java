package com.tkym.labs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.tkym.labs.uke.Iyakuhin;


public class GenericModel {
	private final Iyakuhin senpatsu;
	private List<Iyakuhin> genericList = new ArrayList<Iyakuhin>();
	private static final DecimalFormat formatter = new DecimalFormat("###0000.00#");
	GenericModel(Iyakuhin senpatsu){
		this.senpatsu = senpatsu;
	}
	public Iyakuhin getSenpatsu(){
		return senpatsu;
	}
	public void addGeneric(Iyakuhin generic){
		genericList.add(generic);
	}
	public int genericCount(){
		return genericList.size();
	}
	public double inexpencive(){
		double inexpencive = senpatsu.getKingaku();
		for (Iyakuhin generic : genericList)
			if (generic.getKingaku() < inexpencive)
				inexpencive = generic.getKingaku(); 
		return inexpencive;
	}
	public String kingaku(){
		return formatter.format(senpatsu.getKingaku())+"("+formatter.format(inexpencive())+")";
	}
}
