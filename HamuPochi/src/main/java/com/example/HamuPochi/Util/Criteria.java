package com.example.HamuPochi.Util;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Criteria {
	private int pageNum; //페이지 번호
	private int amount; // 1페이지 출력하는 레코드 개수
	private String type; // 검색 조건 title, content
	private String keyword; // 검색 키워드
	private long category;// 카테고리
	private int status;//주문목록 상태
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	public Criteria() { // 생성자
		this(1,5);
	}

	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	// offset 계산 메서드 추가
    // 스타트 위치
	public int getOffset() {
        return (this.pageNum - 1) * this.amount;
    }
	
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public long getCategory() { return category; }

	public void setCategory(long category) {this.category = category; }

	public int getStatus() { return status; }

	public void setStatus(int status) {this.status = status; }

	public LocalDate getStartDate() { return startDate; }

	public void setStartDate(LocalDate startDate) {this.startDate = startDate; }

	public LocalDate getEndDate() { return endDate; }

	public void setEndDate(LocalDate endDate) {this.endDate = endDate; }


	

}
