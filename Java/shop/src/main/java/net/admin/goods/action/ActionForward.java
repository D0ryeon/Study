package net.admin.goods.action;

public class ActionForward {
	private boolean isRedirect = false;
	private String path = null;
	
	public boolean isRedirect() {
		return isRedirect;
	}
	public String getPath() {
		return path;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
