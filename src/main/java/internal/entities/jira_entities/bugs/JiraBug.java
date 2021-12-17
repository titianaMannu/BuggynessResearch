
package internal.entities.jira_entities.bugs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class JiraBug implements Serializable {

	@SerializedName("expand")
	@Expose
	private String selfExpand;
	@SerializedName("id")
	@Expose
	private String jiraBugId;
	@SerializedName("self")
	@Expose
	private String jiraBugSelf;
	@SerializedName("key")
	@Expose
	private String jiraBugKey;
	@SerializedName("fields")
	@Expose
	private Fields jiraBugFields;
	private static final long serialVersionUID = 8581268398546119351L;

	public String getSelfExpand() {
		return selfExpand;
	}

	public void setSelfExpand(String selfExpand) {
		this.selfExpand = selfExpand;
	}

	public String getJiraBugId() {
		return jiraBugId;
	}

	public void setJiraBugId(String jiraBugId) {
		this.jiraBugId = jiraBugId;
	}

	public String getJiraBugSelf() {
		return jiraBugSelf;
	}

	public void setJiraBugSelf(String jiraBugSelf) {
		this.jiraBugSelf = jiraBugSelf;
	}

	public String getJiraBugKey() {
		return jiraBugKey;
	}

	public void setJiraBugKey(String jiraBugKey) {
		this.jiraBugKey = jiraBugKey;
	}

	public Fields getJiraBugFields() {
		return jiraBugFields;
	}

	public void setJiraBugFields(Fields jiraBugFields) {
		this.jiraBugFields = jiraBugFields;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("expand", selfExpand).append("id", jiraBugId)
				.append("self", jiraBugSelf).append("key", jiraBugKey).append("fields", jiraBugFields).toString();
	}

}
