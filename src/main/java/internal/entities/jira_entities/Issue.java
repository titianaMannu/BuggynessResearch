
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Issue implements Serializable {

	@SerializedName("expand")
	@Expose
	private String expand;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("self")
	@Expose
	private String self;
	@SerializedName("key")
	@Expose
	private String key;
	@SerializedName("fields")
	@Expose
	private Fields fields;
	private static final long serialVersionUID = -7443529615358457595L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Issue() {
	}

	/**
	 * 
	 * @param expand
	 * @param self
	 * @param id
	 * @param fields
	 * @param key
	 */
	public Issue(String expand, String id, String self, String key, Fields fields) {
		super();
		this.expand = expand;
		this.id = id;
		this.self = self;
		this.key = key;
		this.fields = fields;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public Issue withExpand(String expand) {
		this.expand = expand;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Issue withId(String id) {
		this.id = id;
		return this;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public Issue withSelf(String self) {
		this.self = self;
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Issue withKey(String key) {
		this.key = key;
		return this;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}

	public Issue withFields(Fields fields) {
		this.fields = fields;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("expand", expand).append("id", id).append("self", self)
				.append("key", key).append("fields", fields).toString();
	}

}
