package model;

public class AddRequest {

	private String fileName;
	private long fileSize;
	private String realPath;
	private String description;
	
	public String getFileName() {
		return fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public String getRealPath() {
		return realPath;
	}
	public String getDescription() {
		return description;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public PdsItem toPdsItem() {
		PdsItem item = new PdsItem();
		item.setFileName(fileName);
		item.setFileSize(fileSize);
		item.setRealPath(realPath);
		item.setDescription(description);
		return item;
	}
	
}
