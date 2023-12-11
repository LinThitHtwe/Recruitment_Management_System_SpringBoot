package com.ace.job.recruitment.dto;

public class ChartDTO {

	private String name;
	private int year;
	private long count;

	public ChartDTO() {
		super();
	}

	public ChartDTO(String name, long count) {
		super();
		this.name = name;
		this.count = count;
	}

	public ChartDTO(String name, int year, long count) {
		super();
		this.name = name;
		this.year = year;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}