package model;

public class PdsItem {
	
	private int id;
	private String fileName;
	private String realPath;
	private long fileSize;
	private int downloadCount;
	private String description;
	public int getId() {
		return id;
	}
	public String getFileName() {
		return fileName;
	}
	public String getRealPath() {
		return realPath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public int getDownloadCount() {
		return downloadCount;
	}
	public String getDescription() {
		return description;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
