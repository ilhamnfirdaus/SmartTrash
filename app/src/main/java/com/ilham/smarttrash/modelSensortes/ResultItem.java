package com.ilham.smarttrash.modelSensortes;

public class ResultItem{
	private Integer ultrasonik;
	private String waktu;
	private Float suhu;
	private String id;
	private Float kelembaban;

	public void setUltrasonik(Integer ultrasonik){
		this.ultrasonik = ultrasonik;
	}

	public Integer getUltrasonik(){
		return ultrasonik;
	}

	public void setWaktu(String waktu){
		this.waktu = waktu;
	}

	public String getWaktu(){
		return waktu;
	}

	public void setSuhu(Float suhu){
		this.suhu = suhu;
	}

	public Float getSuhu(){
		return suhu;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setKelembaban(Float kelembaban){
		this.kelembaban = kelembaban;
	}

	public Float getKelembaban(){
		return kelembaban;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"ultrasonik = '" + ultrasonik + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",suhu = '" + suhu + '\'' + 
			",id = '" + id + '\'' + 
			",kelembaban = '" + kelembaban + '\'' + 
			"}";
		}
}
