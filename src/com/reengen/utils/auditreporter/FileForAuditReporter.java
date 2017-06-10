package com.reengen.utils.auditreporter;

public class FileForAuditReporter implements Comparable<FileForAuditReporter> {

	String fileID;
	long fileSize;
	String fileName;
	long ownerUserID;
	
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getOwnerUserID() {
		return ownerUserID;
	}
	public void setOwnerUserID(long ownerUserID) {
		this.ownerUserID = ownerUserID;
	}
	
	@Override
	public int compareTo(FileForAuditReporter arg0) {
		return Long.compare(this.getFileSize(), arg0.getFileSize());
	}
	
	
}
