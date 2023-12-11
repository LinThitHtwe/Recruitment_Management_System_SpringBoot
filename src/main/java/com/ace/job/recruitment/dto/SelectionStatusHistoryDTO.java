package com.ace.job.recruitment.dto;

public class SelectionStatusHistoryDTO {
	private String selectionStatusChangeDate;
	private String selectionStatus;
	private String changedUserName;

	public String getSelectionStatusChangeDate() {
		return selectionStatusChangeDate;
	}

	public void setSelectionStatusChangeDate(String selectionStatusChangeDate) {
		this.selectionStatusChangeDate = selectionStatusChangeDate;
	}

	public String getSelectionStatus() {
		return selectionStatus;
	}

	public void setSelectionStatus(String selectionStatus) {
		this.selectionStatus = selectionStatus;
	}

	public String getChangedUserName() {
		return changedUserName;
	}

	public void setChangedUserName(String changedUserName) {
		this.changedUserName = changedUserName;
	}
}
