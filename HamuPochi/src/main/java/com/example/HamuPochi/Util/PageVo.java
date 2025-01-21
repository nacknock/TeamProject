package com.example.HamuPochi.Util;

public class PageVo {
	private int startPage;
	private int endPage;
	private boolean prev, next;
	private int total;
	private Criteria cri; // Criteria 변수를 사용할 수 있다.
	
	public PageVo(Criteria cri, int total) {
		this.cri =cri;
		this.total = total;
		// 2페이지 일때 엔드페잊 10, 3페이지 일때 엔드페잊 10,
		this.endPage = (int) Math.ceil((cri.getPageNum()/5.0))*5;
		// 끝페이지 먼저 구할 것
		// 끝 페이지 10이라면 스타트 페이지 1
		this.startPage =endPage-4;
		// 실젶이지 게시글 33개면 4개까지 밖에 안나옴. 한페이지에 10개인데
		int realEnd =(int)(Math.ceil((total*1.0)/cri.getAmount()));
		//레코드 게수 33개 real End는 4
		if(realEnd < this.endPage) {// 4<10
			this.endPage = realEnd;
		}
		this.prev = this.startPage>1;
		this.next = this.endPage < realEnd;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Criteria getCri() {
		return cri;
	}

	public void setCri(Criteria cri) {
		this.cri = cri;
	}
}
