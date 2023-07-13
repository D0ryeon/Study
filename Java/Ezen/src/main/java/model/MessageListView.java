package model;

import java.util.List;

public class MessageListView {

	private int messageTotalCount;
	private int currentPageNumber;
	private List<Message> messageList;
	private int pageTotalCount;
	private int messageCountPerPage;
	private int firstRow;
	private int endRow;
	
	public MessageListView(List<Message> messageList, int messageTotalCount, int currentPageNumber,
			 int messageCountPerPage,int firstRow, int endRow) {
		this.messageList = messageList;
		this.messageTotalCount = messageTotalCount;
		this.currentPageNumber = currentPageNumber;
		this.messageCountPerPage = messageCountPerPage;
		this.firstRow = firstRow;
		this.endRow = endRow;
		
		calculatePageTotalCount();
		
	}
	
	private void calculatePageTotalCount() {
		if(messageTotalCount == 0) {
			pageTotalCount = 0;
		} else {
			pageTotalCount = messageTotalCount / messageCountPerPage;
			if(messageTotalCount % messageCountPerPage > 0) {
				pageTotalCount++;
			}
		}
	}

	public int getMessageTotalCount() {
		return messageTotalCount;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public int getPageTotalCount() {
		return pageTotalCount;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getMessageCountPerPage() {
		return messageCountPerPage;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setMessageTotalCount(int messageTotalCount) {
		this.messageTotalCount = messageTotalCount;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public void setPageTotalCount(int pageTotalCount) {
		this.pageTotalCount = pageTotalCount;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public void setMessageCountPerPage(int messageCountPerPage) {
		this.messageCountPerPage = messageCountPerPage;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	
	public boolean isEmpty() {
		return messageTotalCount == 0;
	}
	
	
	
}
