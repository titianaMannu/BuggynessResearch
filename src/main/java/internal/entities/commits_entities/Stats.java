
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stats implements Serializable {

	@SerializedName("total")
	@Expose
	private int total;
	@SerializedName("additions")
	@Expose
	private int additions;
	@SerializedName("deletions")
	@Expose
	private int deletions;
	private static final long serialVersionUID = -3854233843815242852L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Stats() {
	}

	/**
	 * 
	 * @param total
	 * @param additions
	 * @param deletions
	 */
	public Stats(int total, int additions, int deletions) {
		super();
		this.total = total;
		this.additions = additions;
		this.deletions = deletions;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Stats withTotal(int total) {
		this.total = total;
		return this;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		if (additions < 0) {
			this.additions = 0;
		} else {
			this.additions = additions;
		}

	}

	public Stats withAdditions(int additions) {
		this.additions = additions;
		return this;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		if (deletions < 0) {
			this.deletions = 0;
		} else {
			this.deletions = deletions;
		}
	}

	public Stats withDeletions(int deletions) {
		this.deletions = deletions;
		return this;
	}

}
