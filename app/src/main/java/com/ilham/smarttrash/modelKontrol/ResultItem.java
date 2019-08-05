package com.ilham.smarttrash.modelKontrol;

public class ResultItem{
	private String nilai;
	private String waktu;
	private String waktuBerakhir;
	private String id;
	private String kontrol;

	public void setNilai(String nilai){
		this.nilai = nilai;
	}

	public String getNilai(){
		return nilai;
	}

	public void setWaktu(String waktu){
		this.waktu = waktu;
	}

	public String getWaktu(){
		return waktu;
	}

	public void setWaktuBerakhir(String waktuBerakhir){
		this.waktuBerakhir = waktuBerakhir;
	}

	public String getWaktuBerakhir(){
		return waktuBerakhir;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setKontrol(String kontrol){
		this.kontrol = kontrol;
	}

	public String getKontrol(){
		return kontrol;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"nilai = '" + nilai + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",waktu_berakhir = '" + waktuBerakhir + '\'' + 
			",id = '" + id + '\'' + 
			",kontrol = '" + kontrol + '\'' + 
			"}";
		}
}
